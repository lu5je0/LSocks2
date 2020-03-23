package com.lu5je0.lsocks2.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.lu5je0.lsocks2.encrypt.ICrypto;

public class EncryptHandler extends MessageToByteEncoder<ByteBuf> {
    private static ICrypto crypto;

    public EncryptHandler(ICrypto crypto) {
        EncryptHandler.crypto = crypto;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        System.out.println("Encrypt");
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        System.out.println(new String(data));

        out.writeBytes(crypto.encrypt(data));
    }
}
