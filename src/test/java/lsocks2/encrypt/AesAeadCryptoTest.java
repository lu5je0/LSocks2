package lsocks2.encrypt;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import lsocks2.util.ByteUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.AEADBadTagException;

public class AesAeadCryptoTest {
    private ICrypto cryptoA;
    private ICrypto cryptoB;

    @Before
    public void setUp() throws Exception {
        cryptoA = new AesAeadCrypto("AES", "123456");
        cryptoB = new AesAeadCrypto("AES", "123456");
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

    @Test(expected = AEADBadTagException.class)
    public void tamperTest() throws Exception {
        byte[] encryptData = cryptoA.encrypt("21312".getBytes());
        encryptData[encryptData.length - 2] = 1;
        cryptoA.decrypt(encryptData);
    }

    @Test
    public void aesDecryptTest() throws Exception {
        byte[] encryptData = Base64.decodeBase64("aOHHlLmKnPM0jq0msDNj/ehfHioAlOtSWj+kYLIH");
        byte[] decrypt = cryptoA.decrypt(encryptData);
        System.out.println(decrypt[0]);
        System.out.println(decrypt[1]);
        System.out.println(ByteUtil.byteToShort(decrypt));
        // Assert.assertEquals("testtest", new String(cryptoA.decrypt(encryptData)));
    }
}