package lsocks2.protocol;

import io.netty.buffer.ByteBuf;

public interface LSocksMessage {
    ByteBuf getAsByteBuf();
}
