package lsocks2.encoder;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lsocks2.encrypt.ICrypto;
import lsocks2.util.ByteUtil;

/**
 * iv | dataLen | iv | data
 * 12 |     2   | 12 | dataLen
 */
public class AeadMessageEncryptHandler extends MessageToByteEncoder<ByteBuf> {
    private static ICrypto crypto;

    public AeadMessageEncryptHandler(ICrypto crypto) {
        AeadMessageEncryptHandler.crypto = crypto;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        short dataLen = (short) msg.readableBytes();
        byte[] encryptLen = crypto.encrypt(ByteUtil.shortToByte(dataLen));
        System.out.println(dataLen + ":" + Base64.encodeBase64String(encryptLen));
        out.writeBytes(encryptLen);

        byte[] data = new byte[dataLen];
        msg.readBytes(data);
        byte[] encryptData = crypto.encrypt(data);
        out.writeBytes(encryptData);
    }
}
