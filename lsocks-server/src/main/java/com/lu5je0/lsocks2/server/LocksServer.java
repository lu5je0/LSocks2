package com.lu5je0.lsocks2.server;

import com.lu5je0.lsocks2.config.ConfigLoader;
import com.lu5je0.lsocks2.server.config.ServerConfig;
import com.lu5je0.lsocks2.server.config.ServerConfigLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import com.lu5je0.lsocks2.config.JsonConfigReader;
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
        ICrypto crypto = CryptoFactory.getCrypt(ServerConfig.INSTANCE.getEncryptMethod(),
                ServerConfig.INSTANCE.getPassword());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new AeadMessageEncryptHandler(crypto));
                        pipeline.addLast(new AeadMessageDecryptHandler(crypto));

                        if (ServerConfig.INSTANCE.getEnableNettyLogging()) {
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        }

                        pipeline.addLast(new LSocksMessageEncoder());

                        pipeline.addLast(new LSocksInitialRequestDecoder());
                        pipeline.addLast(new LSocksInitRequestHandler(workerGroup));
                    }
                });
        serverBootstrap.bind(ServerConfig.INSTANCE.getPort());
    }

    public static void main(String[] args) throws Exception {
        logger.info("Starting Locks2 server");
        LocksServer server = new LocksServer();

        ConfigLoader<ServerConfig> configLoader = new ServerConfigLoader(new JsonConfigReader<>());
        configLoader.loadConfig("server_config.json", ServerConfig.class);

        server.start();
    }
}
