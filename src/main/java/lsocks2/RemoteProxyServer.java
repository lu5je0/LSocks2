package lsocks2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lsocks2.config.ConfigLoader;
import lsocks2.config.JsonConfigLoader;
import lsocks2.encoder.LSocksMessageEncoder;
import lsocks2.encoder.LSocksInitialRequestDecoder;
import lsocks2.encrypt.CryptoFactory;
import lsocks2.encrypt.ICrypto;
import lsocks2.handler.DecryptHandler;
import lsocks2.handler.EncryptHandler;
import lsocks2.handler.server.LSocksInitRequestHandler;
import lsocks2.config.ConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

public class RemoteProxyServer {
    private static final Logger logger = LoggerFactory.getLogger(LocalProxyServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public RemoteProxyServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
    }

    public void start() throws NoSuchAlgorithmException {
        ICrypto crypto = CryptoFactory.getCrypt(ConfigHolder.SERVER_CONFIG.getEncryptMethod(),
                ConfigHolder.SERVER_CONFIG.getPassword());

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (ConfigHolder.SERVER_CONFIG.getEnableNettyLogging()) {
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        }

                        // pipeline.addLast(new EncryptHandler(crypto));
                        // pipeline.addLast(new DecryptHandler(crypto));

                        pipeline.addLast(new LSocksMessageEncoder());

                        pipeline.addLast(new LSocksInitialRequestDecoder());
                        pipeline.addLast(new LSocksInitRequestHandler(workerGroup));
                    }
                });
        serverBootstrap.bind(ConfigHolder.SERVER_CONFIG.getPort());
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        RemoteProxyServer server = new RemoteProxyServer();

        ConfigLoader configLoader = new JsonConfigLoader();
        try {
            configLoader.loadServerConfig();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }

        server.start();
    }
}
