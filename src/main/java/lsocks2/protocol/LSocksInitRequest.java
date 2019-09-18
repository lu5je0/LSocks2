package lsocks2.protocol;

public class LSocksInitRequest implements LSocksMessage {
    private final int dstLength;

    private final String dstAddr;

    private final String dstPort;

    public LSocksInitRequest(int dstLength, String dstAddr, String dstPort) {
        this.dstLength = dstLength;
        this.dstAddr = dstAddr;
        this.dstPort = dstPort;
    }

    public int getDstLength() {
        return dstLength;
    }

    public String getDstAddr() {
        return dstAddr;
    }

    public String getDstPort() {
        return dstPort;
    }
}
