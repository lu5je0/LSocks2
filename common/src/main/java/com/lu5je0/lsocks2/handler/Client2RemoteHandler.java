package com.lu5je0.lsocks2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class Client2RemoteHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Channel remoteChannel;

    public Client2RemoteHandler(Channel remoteChannel) {
        this.remoteChannel = remoteChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        log.info("转发客户端请求:\n{}", msg);
        remoteChannel.writeAndFlush(msg.retain());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
//        log.info("客户端已断开与远程服务器{}的连接", ctx.channel().remoteAddress());
        remoteChannel.close();
    }
}
