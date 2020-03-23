package com.lu5je0.lsocks2.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import com.lu5je0.lsocks2.encrypt.ICrypto;

public class DecryptHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static ICrypto crypto;

    public DecryptHandler(ICrypto crypto) {
        DecryptHandler.crypto = crypto;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Decrypt");
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);

        ByteBuf encryptData = ctx.alloc().buffer();
        encryptData.writeBytes(crypto.decrypt(data));

        ctx.fireChannelRead(encryptData);
    }
}
