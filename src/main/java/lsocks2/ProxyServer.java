package lsocks2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lsocks2.config.ProxyServerConfig;
import lsocks2.handler.Socks5CommandRequestHandler;
import lsocks2.handler.Socks5InitialRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyServer {
    private static final Logger logger = LoggerFactory.getLogger(ProxyServer.class);

    private int port;

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private boolean logging = true;

    public void init(ProxyServerConfig config) {
        if (config == null) {
            config = ProxyServerConfig.defaultProxyServerConfig();
        }
        this.port = config.getPort();
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        logger.info("ProxyServer initialized");
    }

    public void init() {
        init(null);
    }

    public void start() {
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            if (logging) {
                                p.addLast(new LoggingHandler(LogLevel.INFO));
                            }
                            p.addLast(Socks5ServerEncoder.DEFAULT);
                            p.addLast(new Socks5InitialRequestDecoder());
                            p.addLast(new Socks5CommandRequestDecoder());
                            p.addLast(new Socks5InitialRequestHandler());
                            p.addLast(new Socks5CommandRequestHandler());
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
        ProxyServer proxyServer = new ProxyServer();
        proxyServer.init();
        proxyServer.start();
    }
}
