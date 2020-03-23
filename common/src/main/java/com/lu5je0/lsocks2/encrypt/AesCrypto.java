package com.lu5je0.lsocks2.encrypt;

public class AesCrypto extends AbstractCrypto {
    public AesCrypto(String encryptMethod, String password) {
        super(encryptMethod, password);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        return new byte[0];
    }
}
