package lsocks2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lsocks2.encrypt.ICrypto;

import java.util.List;

public class DecryptHandler extends MessageToMessageDecoder<ByteBuf> {
    private static ICrypto crypto;

    public DecryptHandler(ICrypto crypto) {
        DecryptHandler.crypto = crypto;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println("解密");
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);

        out.add(crypto.decrypt(crypto.decrypt(data)));
    }
}
