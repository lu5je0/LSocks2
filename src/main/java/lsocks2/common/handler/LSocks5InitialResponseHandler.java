package lsocks2.common.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lsocks2.protocol.LSocksStatus;

public class LSocks5InitialResponseHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private enum State {
        INIT,
        SUCCESS
    }

    private State state;

    public LSocks5InitialResponseHandler() {
        state = State.INIT;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        switch (state) {
            case INIT: {
                LSocksStatus status = LSocksStatus.valueOf(msg.readByte());
                if (status == LSocksStatus.SUCCESS) {
                    state = State.SUCCESS;
                }
            }
            case SUCCESS: {
                ctx.fireChannelRead(msg.retain());
            }
        }
    }
}
