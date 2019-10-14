package lsocks2.encrypt;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;
import java.util.List;

public class AesCrypto extends AbstractCrypto {
    private SecretKey key;

    private Cipher cipher;

    public AesCrypto(String encryptMethod, String password) throws NoSuchAlgorithmException, NoSuchPaddingException {
        super(encryptMethod, password);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());
        key = new SecretKeySpec(md5.digest(), encryptMethod);
        cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
    }


    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        assert iv.length == 12;
        byte[] encryptData = cipher.doFinal(data);
        assert encryptData.length == data.length + 16;
        byte[] message = new byte[12 + data.length + 16];
        System.arraycopy(iv, 0, message, 0, 12);
        System.arraycopy(encryptData, 0, message, 12, encryptData.length);
        return message;
    }

    @Override
    public byte[] decrypt(byte[] data) throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        if (data.length < 12 + 16) throw new IllegalArgumentException();
        GCMParameterSpec params = new GCMParameterSpec(128, data, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, key, params);
        return cipher.doFinal(data, 12, data.length - 12);
    }

    public static List<String> ciphers() {
        return Arrays.asList("AES");
    }
}
