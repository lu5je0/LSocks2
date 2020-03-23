package com.lu5je0.lsocks2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import com.lu5je0.lsocks2.config.AbstractConfigLoader;
import com.lu5je0.lsocks2.config.ConfigHolder;
import com.lu5je0.lsocks2.config.JsonConfigLoader;
import com.lu5je0.lsocks2.encoder.*;
import com.lu5je0.lsocks2.encrypt.CryptoFactory;
import com.lu5je0.lsocks2.encrypt.ICrypto;
import com.lu5je0.lsocks2.server.handler.LSocksInitRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocksServer {
    private static final Logger logger = LoggerFactory.getLogger(LocksServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public LocksServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
    }

    public void start() throws Exception {
        ICrypto crypto = CryptoFactory.getCrypt(ConfigHolder.SERVER_CONFIG.getEncryptMethod(),
                ConfigHolder.SERVER_CONFIG.getPassword());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new AeadMessageEncryptHandler(crypto));
                        pipeline.addLast(new AeadMessageDecryptHandler(crypto));

                        if (ConfigHolder.SERVER_CONFIG.getEnableNettyLogging()) {
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        }

                        pipeline.addLast(new LSocksMessageEncoder());

                        pipeline.addLast(new LSocksInitialRequestDecoder());
                        pipeline.addLast(new LSocksInitRequestHandler(workerGroup));
                    }
                });
        serverBootstrap.bind(ConfigHolder.SERVER_CONFIG.getPort());
    }

    public static void main(String[] args) throws Exception {
        logger.info("Starting Locks2 server");
        LocksServer server = new LocksServer();

        AbstractConfigLoader abstractConfigLoader = new JsonConfigLoader();
        try {
            abstractConfigLoader.loadServerConfig();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }

        server.start();
    }
}
