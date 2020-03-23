package com.lu5je0.lsocks2.client;

import com.lu5je0.lsocks2.client.config.ClientConfig;
import com.lu5je0.lsocks2.client.config.ClientConfigLoader;
import com.lu5je0.lsocks2.client.handler.Socks5CommandRequestHandler;
import com.lu5je0.lsocks2.client.handler.Socks5InitialRequestHandler;
import com.lu5je0.lsocks2.config.JsonConfigReader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocksClient {
    private static final Logger logger = LoggerFactory.getLogger(LocksClient.class);

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public LocksClient() {
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(10);
        logger.info("ProxyServer initialized");
    }

    public void start() {
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (ClientConfig.INSTANCE.isEnableNettyLogging()) {
                                p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            }
                            p.addLast(Socks5ServerEncoder.DEFAULT);
                            p.addLast(new Socks5InitialRequestDecoder());
                            p.addLast(new Socks5CommandRequestDecoder());

                            p.addLast(new Socks5InitialRequestHandler());
                            p.addLast(new Socks5CommandRequestHandler(workerGroup));
                        }
                    });

            ChannelFuture bindFuture = bootstrap.bind(ClientConfig.INSTANCE.getLocalPort()).sync();
            bindFuture.addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("server bind on {}", ClientConfig.INSTANCE.getLocalPort());
                }
            });

            bindFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        logger.info("Starting Locks2 client");
        LocksClient locksClient = new LocksClient();
        ClientConfigLoader clientConfigLoader = new ClientConfigLoader(new JsonConfigReader<>());
        clientConfigLoader.loadConfig("client_config.json", ClientConfig.class);
        locksClient.start();
    }
}
