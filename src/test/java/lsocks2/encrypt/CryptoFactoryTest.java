package lsocks2.encrypt;

import org.junit.Test;

import static org.junit.Assert.*;

public class CryptoFactoryTest {
    @Test
    public void getAesCrypt() throws Exception {
        ICrypto crypto = CryptoFactory.getCrypt("AES", "123456");
        assertEquals(crypto.getClass(), AesAeadCrypto.class);
    }

    @Test
    public void getNoneCrypt() throws Exception {
        ICrypto crypto = CryptoFactory.getCrypt("NONE", "123456");
        assertEquals(crypto.getClass(), NoneCrypto.class);
    }
}