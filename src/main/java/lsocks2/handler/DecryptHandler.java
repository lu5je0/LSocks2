package lsocks2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lsocks2.encrypt.ICrypto;

public class DecryptHandler extends MessageToByteEncoder<ByteBuf> {
    private static ICrypto crypto;

    public DecryptHandler(ICrypto crypto) {
        DecryptHandler.crypto = crypto;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf encryptByteBuf, ByteBuf out) throws Exception {
        byte[] data = new byte[encryptByteBuf.readableBytes()];
        encryptByteBuf.readBytes(data);

        out.writeBytes(crypto.decrypt(data));
    }
}
