package lsocks2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lsocks2.local.LocalProxyServer;
import lsocks2.server.config.ServerConfig;
import lsocks2.server.handler.LSocksInitialRequestDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteProxyServer {
    private static final Logger logger = LoggerFactory.getLogger(LocalProxyServer.class);

    /**
     * 远程服务暴露的端口
     */
    private int locksPort;

    private boolean enableNettyLogging;

    private ServerBootstrap serverBootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public void loadConfig() {
        loadConfig(null);
    }

    public void loadConfig(ServerConfig config) {
        if (config == null) {
            logger.warn("ServerConfig未指定，将使用默认配置");
            config = ServerConfig.defaultConfig();
        }
        this.locksPort = config.getPort();
        this.enableNettyLogging = config.isEnableNettyLogging();

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
    }

    public void start() {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LSocksInitialRequestDecoder());
                    }
                });
    }

    public static void main(String[] args) {
        RemoteProxyServer server = new RemoteProxyServer();
        server.loadConfig();
        server.start();
    }
}
