package com.lu5je0.lsocks2.protocol;

import io.netty.buffer.ByteBuf;

public interface LSocksMessage {
    ByteBuf getAsByteBuf();
}
