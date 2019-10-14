package lsocks2.encrypt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AesCryptoTest {
    private ICrypto cryptoA;
    private ICrypto cryptoB;

    @Before
    public void setUp() throws Exception {
        cryptoA = new AesCrypto("AES", "12345678");
        cryptoB = new AesCrypto("AES", "12345678");
    }

    @Test
    public void aesCryptoTest() throws Exception {
        String message = "test test";
        byte[] decryptData = cryptoA.decrypt(cryptoB.encrypt(message.getBytes()));
        Assert.assertArrayEquals(message.getBytes(), decryptData);

        byte[] decryptData1 = cryptoB.decrypt(cryptoA.encrypt(message.getBytes()));
        Assert.assertArrayEquals(message.getBytes(), decryptData1);
    }

    @Test
    public void aesCryptoTest1() throws Exception {
        String message = "test test";
        byte[] decryptData = cryptoA.decrypt(cryptoA.encrypt(message.getBytes()));
        Assert.assertArrayEquals(message.getBytes(), decryptData);
    }
}