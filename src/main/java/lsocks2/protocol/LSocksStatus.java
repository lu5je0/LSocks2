package lsocks2.protocol;

public class LSocksStatus {
    public static final LSocksStatus SUCCESS = new LSocksStatus(0x00, "SUCCESS");
    public static final LSocksStatus FAILURE = new LSocksStatus(0x01, "FAILURE");

    private final byte byteValue;
    
    private final String name;

    public LSocksStatus(int byteValue, String name) {
        this.byteValue = ((byte) byteValue);
        this.name = name;
    }
}
