package com.lu5je0.lsocks2.client.handler;

import com.lu5je0.lsocks2.client.config.ClientConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import com.lu5je0.lsocks2.encoder.AeadMessageDecryptHandler;
import com.lu5je0.lsocks2.encoder.AeadMessageEncryptHandler;
import com.lu5je0.lsocks2.encoder.LSocksMessageEncoder;
import com.lu5je0.lsocks2.encrypt.CryptoFactory;
import com.lu5je0.lsocks2.encrypt.ICrypto;
import com.lu5je0.lsocks2.handler.LSocks5InitialResponseHandler;
import com.lu5je0.lsocks2.handler.Remote2ClientHandler;
import com.lu5je0.lsocks2.protocol.LSocksInitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {
    private static final Logger logger = LoggerFactory.getLogger(Socks5CommandRequestHandler.class);

    private EventLoopGroup eventLoopGroup;

    public Socks5CommandRequestHandler(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext parentCtx, DefaultSocks5CommandRequest msg) throws Exception {
        logger.info("客户端准备连接至{}:{}", msg.dstAddr(), msg.dstPort());
        ICrypto crypto = CryptoFactory.getCrypt(ClientConfig.INSTANCE.getEncryptMethod(),
                ClientConfig.INSTANCE.getPassword());
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new AeadMessageEncryptHandler(crypto));
                        pipeline.addLast(new AeadMessageDecryptHandler(crypto));

                        pipeline.addLast(new LSocks5InitialResponseHandler(parentCtx));
                        pipeline.addLast(new Remote2ClientHandler(parentCtx.channel()));
                        pipeline.addLast(new LSocksMessageEncoder());
                    }
                });
        ChannelFuture connectFuture = bootstrap.connect(ClientConfig.INSTANCE.getServerHost(), 20443);

        // 连接上LSocksServer后，发送LSocksInitRequest
        connectFuture.addListener(future -> {
            if (future.isSuccess()) {
                logger.info("发送LSocksInitRequest");
                connectFuture.channel().writeAndFlush(new LSocksInitRequest(msg.dstAddr(), msg.dstPort()));
            }
        });
    }
}
