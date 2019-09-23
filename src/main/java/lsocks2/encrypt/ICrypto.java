package lsocks2.encrypt;

public interface ICrypto {
    byte[] encrypt(byte[] data) throws Exception;

    byte[] decrypt(byte[] data) throws Exception;
}
