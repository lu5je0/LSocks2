package lsocks2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class Remote2ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static Logger logger = LoggerFactory.getLogger(Remote2ClientHandler.class);

    private Channel clientChannel;

    public Remote2ClientHandler(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
//        logger.info("远程服务器响应:{}", msg.toString(StandardCharsets.UTF_8));
        clientChannel.writeAndFlush(msg.retain());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
//        logger.info("远程服务器{}的连接已断开", ctx.channel().remoteAddress());
        clientChannel.close();
    }
}
