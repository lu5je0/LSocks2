package com.lu5je0.lsocks2.encoder;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import com.lu5je0.lsocks2.protocol.LSocksMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LSocksMessageEncoder extends ChannelOutboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(LSocksMessageEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof LSocksMessage) {
            ctx.write(((LSocksMessage) msg).getAsByteBuf(), promise);
        } else {
            ctx.write(msg, promise);
        }
    }
}
