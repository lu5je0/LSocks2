package lsocks2.protocol;

public class LSocksInitResponse implements LSocksMessage {
    private final LSocksStatus status;

    public LSocksInitResponse(LSocksStatus status) {
        this.status = status;
    }
}
