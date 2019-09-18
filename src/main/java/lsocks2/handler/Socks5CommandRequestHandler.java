package lsocks2.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5CommandRequest> {
    private static final Logger logger = LoggerFactory.getLogger(Socks5CommandRequestHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5CommandRequest msg) {
        logger.info("客户端准备连接至:type:{} addr:{} port:{}", msg.dstAddrType(), msg.dstAddr(), msg.dstPort());
        ctx.writeAndFlush(new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, msg.dstAddrType()));
    }
}
