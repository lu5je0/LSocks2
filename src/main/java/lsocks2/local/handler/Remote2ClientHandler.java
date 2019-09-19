package lsocks2.local.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lsocks2.protocol.LSocksStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Remote2ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    private State state;

    private static Logger logger = LoggerFactory.getLogger(Remote2ClientHandler.class);

    private Channel clientChannel;

    public Remote2ClientHandler(Channel clientChannel) {
        this.clientChannel = clientChannel;
        this.state = State.INIT;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
//        logger.info("远程服务器响应:{}", msg.toString(StandardCharsets.UTF_8));
        switch (state) {
            case INIT: {
                LSocksStatus lSocksStatus = LSocksStatus.valueOf(msg.readByte());
                if (lSocksStatus == LSocksStatus.SUCCESS) {
                    state = State.SUCCESS;
                }
            }
            case SUCCESS: {
                // todo msg.retain()具体会做些什么？
                clientChannel.writeAndFlush(msg.retain());
            }
            case FAILURE: {
                // todo
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
//        logger.info("远程服务器{}的连接已断开", ctx.channel().remoteAddress());
        clientChannel.close();
    }
}
