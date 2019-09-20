package lsocks2;

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
import lsocks2.local.config.LocalConfig;
import lsocks2.local.handler.Socks5CommandRequestHandler;
import lsocks2.local.handler.Socks5InitialRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalProxyServer {
    private static final Logger logger = LoggerFactory.getLogger(LocalProxyServer.class);

    private int port;

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private boolean enableNettyLogging = true;

    public void loadConfig(LocalConfig config) {
        if (config == null) {
            logger.warn("未指定LocalConfig，将使用默认配置");
            config = LocalConfig.defaultConfig();
        }
        this.port = config.getPort();
        this.enableNettyLogging = config.isEnableNettyLogging();
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(10);
        logger.info("ProxyServer initialized");
    }

    public void loadConfig() {
        loadConfig(null);
    }

    public void start() {
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (enableNettyLogging) {
                                p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            }
                            p.addLast(Socks5ServerEncoder.DEFAULT);
                            p.addLast(new Socks5InitialRequestDecoder());
                            p.addLast(new Socks5CommandRequestDecoder());

                            p.addLast(new Socks5InitialRequestHandler());
                            p.addLast(new Socks5CommandRequestHandler(workerGroup));
                        }
                    });

            ChannelFuture bindFuture = bootstrap.bind(port).sync();
            bindFuture.addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("server bind on {}", port);
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

    public static void main(String[] args) {
        LocalProxyServer localProxyServer = new LocalProxyServer();
        localProxyServer.loadConfig();
        localProxyServer.start();
    }
}
