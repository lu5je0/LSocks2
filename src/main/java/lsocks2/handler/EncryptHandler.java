package lsocks2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lsocks2.encrypt.ICrypto;

public class EncryptHandler extends MessageToByteEncoder<ByteBuf> {
    private static ICrypto crypto;

    public EncryptHandler(ICrypto crypto) {
        EncryptHandler.crypto = crypto;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf rawData, ByteBuf out) throws Exception {
        byte[] data = new byte[rawData.readableBytes()];
        rawData.readBytes(data);

        out.writeBytes(crypto.encrypt(data));
    }
}
