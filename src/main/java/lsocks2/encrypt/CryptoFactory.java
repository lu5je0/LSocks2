package lsocks2.encrypt;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class CryptoFactory {
    private static final HashMap<String, String> ciphers = new HashMap<>();

    static {
        ciphers.putAll(AeadCrypto.ciphers());
    }

    public static ICrypto getCrypt(String encryptMethod, String password) throws NoSuchAlgorithmException {
        String cryptoName = ciphers.get(encryptMethod);
        if ("lsocks2.encrypt.AeadCrypto".equals(cryptoName)) {
            return new AeadCrypto(encryptMethod, password);
        } else {
            throw new NoSuchAlgorithmException();
        }
    }
}
