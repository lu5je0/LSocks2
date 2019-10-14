package lsocks2.encrypt;

import java.util.HashMap;
import java.util.Map;

public class NoneCrypto extends AbstractCrypto {
    public NoneCrypto(String encryptMethod, String password) {
        super(encryptMethod, password);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        return data;
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        return data;
    }

    public static Map<String, String> ciphers() {
        HashMap<String, String> map = new HashMap<>();
        map.put("none", NoneCrypto.class.getName());
        return map;
    }
}
