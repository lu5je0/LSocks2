package lsocks2.local.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {
    private static final Logger logger = LoggerFactory.getLogger(Socks5CommandRequestHandler.class);

    private EventLoopGroup eventLoopGroup;

    public Socks5CommandRequestHandler(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext parentCtx, DefaultSocks5CommandRequest msg) {
        logger.info("客户端准备连接至{}:{}", msg.dstAddr(), msg.dstPort());
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new Remote2ClientHandler(parentCtx.channel()));
                    }
                });
        ChannelFuture connectFuture = bootstrap.connect(msg.dstAddr(), msg.dstPort());

        // 这里必须在LSocks2连接上远程服务器后才能将Client2RemoteHandler添加到pipeline中
        // 最后再将Socks5的CommandResponse返回给客户端
        connectFuture.addListener(future -> {
            if (future.isSuccess()) {
                parentCtx.pipeline().addLast(new Client2RemoteHandler(connectFuture.channel()));
                parentCtx.writeAndFlush(new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, msg.dstAddrType()));
            }
        });
    }
}
