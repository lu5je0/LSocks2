package lsocks2.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import lsocks2.server.handler.LSocksInitialRequestDecoder.State;
import lsocks2.protocol.LSocksInitResponse;
import lsocks2.protocol.LSocksStatus;

import java.util.List;

public class LSocksInitialRequestDecoder extends ReplayingDecoder<State> {
    enum State {
        INIT,
        SUCCESS,
        FAILURE
    }

    public LSocksInitialRequestDecoder() {
        super(State.INIT);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        switch (state()) {
            case INIT: {
                final int dstLength = in.readUnsignedByte();
                final String dstAddr = in.toString(in.readerIndex(), dstLength, CharsetUtil.US_ASCII);
                in.skipBytes(dstLength);
                final int port = in.readUnsignedShort();
                out.add(new LSocksInitResponse(LSocksStatus.SUCCESS));
                checkpoint(State.SUCCESS);
            }
            case SUCCESS: {
                int readableBytes = actualReadableBytes();
                if (readableBytes > 0) {
                    out.add(in.readRetainedSlice(readableBytes));
                }
            }
        }
    }
}
