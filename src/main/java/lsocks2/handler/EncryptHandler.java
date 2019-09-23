package lsocks2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lsocks2.encrypt.ICrypto;

import java.util.List;

public class EncryptHandler extends MessageToMessageEncoder<ByteBuf> {
    private static ICrypto crypto;

    public EncryptHandler(ICrypto crypto) {
        EncryptHandler.crypto = crypto;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println("加密");
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);
        out.add(crypto.encrypt(data));
    }
}
