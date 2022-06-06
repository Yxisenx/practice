package cn.onecolour.netty.time.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * @author yang
 * @date 2022/6/6
 * @description
 */
public class TimeProtocolTcpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 连接成功, 向客户端发送时间后断开连接
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.printf("[Server] %s:%s connected.\n", address.getAddress().getHostAddress(), address.getPort());
        final ByteBuf timeBuf = ctx.alloc().buffer(4);
        timeBuf.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        final ChannelFuture channelFuture = ctx.writeAndFlush(timeBuf);
        channelFuture.addListener((ChannelFutureListener) future -> {
            assert channelFuture == future;
            ctx.close();
            System.out.printf("[Server] %s:%s disconnected.\n", address.getAddress().getHostAddress(), address.getPort());
        });
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
