package cn.onecolour.netty.time.tcp;

import cn.onecolour.utils.TimeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author yang
 * @date 2022/6/6
 * @description
 */
public class TimeProtocolTcpClientHandler extends ChannelInboundHandlerAdapter {
    private final static BiConsumer<ChannelHandlerContext, ByteBuf> DEFAULT_CONSUMER = (ctx, msg) -> {
        try {
            long timestamp = msg.readUnsignedInt();
            String timeString = TimeUtils.timeFormat(timestamp - 2208988800L, TimeUnit.SECONDS);
            System.out.println("[Server Response] " + timeString);
            ctx.close();
        } finally {
            msg.release();
        }
    };

    private final BiConsumer<ChannelHandlerContext, ByteBuf> readConsumer;

    public TimeProtocolTcpClientHandler(BiConsumer<ChannelHandlerContext, ByteBuf> readConsumer) {
        if (readConsumer == null) {
            readConsumer = DEFAULT_CONSUMER;
        }
        this.readConsumer = readConsumer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        readConsumer.accept(ctx, (ByteBuf) msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
