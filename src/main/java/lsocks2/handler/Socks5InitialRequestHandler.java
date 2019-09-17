package lsocks2.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5InitialRequest> {
    private static final Logger logger = LoggerFactory.getLogger(Socks5InitialRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5InitialRequest msg) {
        logger.info("初始化Socks5:{}", msg);
    }
}
