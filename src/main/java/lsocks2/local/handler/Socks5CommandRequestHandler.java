package lsocks2.local.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.logging.LoggingHandler;
import lsocks2.common.encoder.LSocksMessageEncoder;
import lsocks2.common.handler.LSocks5InitialResponseHandler;
import lsocks2.common.handler.Remote2ClientHandler;
import lsocks2.protocol.LSocksInitRequest;
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
                        pipeline.addLast(new LoggingHandler());
                        pipeline.addLast(new LSocks5InitialResponseHandler(parentCtx));
                        pipeline.addLast(new Remote2ClientHandler(parentCtx.channel()));
                        pipeline.addLast(new LSocksMessageEncoder());
                    }
                });
        ChannelFuture connectFuture = bootstrap.connect("127.0.0.1", 20443);

        // 连接上LSocksServer后，发送发送LSocksInitRequest
        connectFuture.addListener(future -> {
            if (future.isSuccess()) {
                logger.info("发送LSocksInitRequest");
                connectFuture.channel().writeAndFlush(new LSocksInitRequest(msg.dstAddr(), msg.dstPort()));
            }
        });
    }
}
