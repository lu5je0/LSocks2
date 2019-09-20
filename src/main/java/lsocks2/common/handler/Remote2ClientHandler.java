package lsocks2.common.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Remote2ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static Logger logger = LoggerFactory.getLogger(Remote2ClientHandler.class);

    private Channel clientChannel;

    public Remote2ClientHandler(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        logger.info("远程服务器响应:{}", msg.toString(CharsetUtil.UTF_8));
        clientChannel.writeAndFlush(msg.retain());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        clientChannel.close();
    }
}
