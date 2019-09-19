package lsocks2.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class LSocksInitRequest implements LSocksMessage {
    private final int dstLength;

    private final String dstAddr;

    private final int dstPort;

    public LSocksInitRequest(String dstAddr, int dstPort) {
        this.dstLength = dstAddr.length();
        this.dstAddr = dstAddr;
        this.dstPort = dstPort;
    }

    public int getDstLength() {
        return dstLength;
    }

    public String getDstAddr() {
        return dstAddr;
    }

    public int getDstPort() {
        return dstPort;
    }

    public ByteBuf getAsByteBuf() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(((byte) dstLength));
        byteBuf.writeBytes(dstAddr.getBytes(CharsetUtil.US_ASCII));
        byteBuf.writeShort(dstPort);
        return byteBuf;
    }
}
