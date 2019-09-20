package lsocks2.common.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import lsocks2.protocol.LSocksInitRequest;
import lsocks2.common.encoder.LSocksInitialRequestDecoder.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LSocksInitialRequestDecoder extends ReplayingDecoder<State> {
    private static Logger logger = LoggerFactory.getLogger(LSocksInitialRequestDecoder.class);

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
                out.add(new LSocksInitRequest(dstAddr, port));
                checkpoint(State.SUCCESS);
                logger.info("收到LSocksInitRequest [{}:{}]", dstAddr, port);
            }
            case SUCCESS: {
                logger.info("收到客户端数据");
                int readableBytes = actualReadableBytes();
                if (readableBytes > 0) {
                    out.add(in.readRetainedSlice(readableBytes));
                }
            }
        }
    }
}
