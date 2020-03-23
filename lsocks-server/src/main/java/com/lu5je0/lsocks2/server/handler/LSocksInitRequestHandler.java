package com.lu5je0.lsocks2.server.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.lu5je0.lsocks2.handler.Client2RemoteHandler;
import com.lu5je0.lsocks2.handler.Remote2ClientHandler;
import com.lu5je0.lsocks2.protocol.LSocksInitRequest;
import com.lu5je0.lsocks2.protocol.LSocksInitResponse;
import com.lu5je0.lsocks2.protocol.LSocksStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LSocksInitRequestHandler extends SimpleChannelInboundHandler<LSocksInitRequest> {
    private static final Logger logger = LoggerFactory.getLogger(LSocksInitRequestHandler.class);

    private EventLoopGroup eventLoopGroup;

    public LSocksInitRequestHandler(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext parentCtx, LSocksInitRequest msg) {
        // 准备连接到目标服务器
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new Remote2ClientHandler(parentCtx.channel()));
                    }
                });
        ChannelFuture connectFuture = bootstrap.connect(msg.getDstAddr(), msg.getDstPort());

        connectFuture.addListener(future -> {
            if (future.isSuccess()) {
                logger.info("连接到[{}, {}]", msg.getDstAddr(), msg.getDstPort());
                parentCtx.pipeline().addLast(new Client2RemoteHandler(connectFuture.channel()));

                parentCtx.writeAndFlush(new LSocksInitResponse(LSocksStatus.SUCCESS)).addListener(f -> {
                    if (f.isSuccess()) {
                        logger.info("成功发送LSocksInitResponse[{}, {}]", msg.getDstAddr(), msg.getDstPort());
                    }
                });
            }
        });
    }
}
