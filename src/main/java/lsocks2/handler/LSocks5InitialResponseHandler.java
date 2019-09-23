package lsocks2.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5AddressType;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import lsocks2.protocol.LSocksStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LSocks5InitialResponseHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Logger logger = LoggerFactory.getLogger(LSocks5InitialResponseHandler.class);

    private enum State {
        INIT,
        SUCCESS
    }

    private State state;

    private ChannelHandlerContext parentCtx;

    public LSocks5InitialResponseHandler(ChannelHandlerContext parentCtx) {
        state = State.INIT;
        this.parentCtx = parentCtx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        switch (state) {
            case INIT: {
                logger.info("收到LSocks5InitialResponse");
                LSocksStatus status = LSocksStatus.valueOf(msg.readByte());
                if (status == LSocksStatus.SUCCESS) {
                    state = State.SUCCESS;
                }

                // 先给本地代理添加转发handler,再发送确认消息
                parentCtx.pipeline().addLast(new Client2RemoteHandler(ctx.channel()));
                // 发送给确认消息给Socks5客户端
                // todo 这里直接返回Socks5AddressType.DOMAIN，可能会出现问题
                parentCtx.writeAndFlush(new DefaultSocks5CommandResponse(Socks5CommandStatus.SUCCESS, Socks5AddressType.DOMAIN));
            }
            case SUCCESS: {
                ctx.fireChannelRead(msg.retain());
            }
        }
    }
}
