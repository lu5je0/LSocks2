package com.lu5je0.lsocks2.encrypt;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Arrays;
import java.util.List;

public class AesAeadCrypto extends AbstractCrypto {
    private final Cipher encryptCipher;

    private final Cipher decryptCipher;

    private byte[] key;

    public AesAeadCrypto(String encryptMethod, String password) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        super(encryptMethod, password);
        key = new byte[] {1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6};

        SecretKey secretKey = new SecretKeySpec(key, "AES");
        encryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
        decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");

        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, key));
        decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, key));
    }


    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        byte[] cipherText = encryptCipher.doFinal(data);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + key.length + cipherText.length);
        byteBuffer.putInt(key.length);
        byteBuffer.put(key);
        byteBuffer.put(cipherText);
        return byteBuffer.array();
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        return decryptCipher.doFinal(data);
    }

    public static List<String> ciphers() {
        return Arrays.asList("AES");
    }
}
