package com.lu5je0.lsocks2.encrypt;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class CryptoFactory {
    private static final HashMap<String, Class<? extends ICrypto>> ciphers = new HashMap<>();

    static {
        for (String s : AesAeadCrypto.ciphers()) {
            ciphers.put(s, AesAeadCrypto.class);
        }
        ciphers.put("NONE", NoneCrypto.class);
    }

    public static ICrypto getCrypt(String encryptMethod, String password) throws Exception {
        Class<? extends ICrypto> cryptoClass = ciphers.get(encryptMethod);
        Constructor<? extends ICrypto> constructor = cryptoClass.getConstructor(String.class, String.class);
        return constructor.newInstance(encryptMethod, password);
    }
}
