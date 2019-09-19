package lsocks2.common.encoder;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lsocks2.protocol.LSocksMessage;

public class LSocksMessageEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof LSocksMessage) {
            ctx.write(((LSocksMessage) msg).getAsByteBuf(), promise);
        } else {
            ctx.write(msg, promise);
        }
    }
}
