package com.lu5je0.lsocks2.util;

public class ByteUtil {
    public static byte[] shortToByte(short len) {
        int i = len >>> 8;
        byte h = (byte) i;
        byte l = (byte) (len & 0xff);
        return new byte[] {h, l};
    }

    public static short byteToShort(byte[] bytes) {
        assert bytes.length == 2;
        byte l = bytes[1];
        byte h = bytes[0];
        return (short) ((l & 0xff) + h * 256);
    }
}
