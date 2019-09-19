package lsocks2.protocol;

public class LSocksStatus {
    public static final LSocksStatus SUCCESS = new LSocksStatus(0x00, "SUCCESS");
    public static final LSocksStatus FAILURE = new LSocksStatus(0x01, "FAILURE");
    public static final LSocksStatus UNKNOWN = new LSocksStatus(0x99, "UNKNOWN");

    private final byte byteValue;
    
    private final String name;

    public byte getByteValue() {
        return byteValue;
    }

    public static LSocksStatus valueOf(byte b) {
        switch (b) {
            case 0x00:
                return SUCCESS;
            case 0x01:
                return FAILURE;
            default:
                return UNKNOWN;
        }
    }

    public LSocksStatus(int byteValue, String name) {
        this.byteValue = ((byte) byteValue);
        this.name = name;
    }
}
