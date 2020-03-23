package com.lu5je0.lsocks2.encoder;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import com.lu5je0.lsocks2.encrypt.ICrypto;
import com.lu5je0.lsocks2.util.ByteUtil;

import java.util.List;

public class AeadMessageDecryptHandler extends ReplayingDecoder {
    private static ICrypto crypto;

    public AeadMessageDecryptHandler(ICrypto crypto) {
        AeadMessageDecryptHandler.crypto = crypto;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] encryptedLength = new byte[30];
        in.readBytes(encryptedLength);
        short len = 0;
        System.out.println(Base64.encodeBase64String(encryptedLength));
        len = ByteUtil.byteToShort(crypto.decrypt(encryptedLength));

        byte[] data = new byte[28 + len];
        in.readBytes(data);

        out.add(ctx.alloc().buffer().writeBytes(crypto.decrypt(data)));
    }
}
