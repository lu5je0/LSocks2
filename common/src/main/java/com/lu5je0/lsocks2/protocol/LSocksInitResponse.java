package com.lu5je0.lsocks2.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LSocksInitResponse implements LSocksMessage {
    private final LSocksStatus status;

    public LSocksInitResponse(LSocksStatus status) {
        this.status = status;
    }

    @Override
    public ByteBuf getAsByteBuf() {
        ByteBuf byteBuf = Unpooled.buffer();
        return byteBuf.writeByte(status.getByteValue());
    }
}
