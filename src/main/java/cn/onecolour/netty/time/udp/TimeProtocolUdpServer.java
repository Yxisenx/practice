package cn.onecolour.netty.time.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;


/**
 * <p></p>https://datatracker.ietf.org/doc/html/rfc868</p>
 * <p>S: Listen on port 37 (45 octal).</p>
 * <p>U: Send an empty datagram to port 37.</p>
 * <p>S: Receive the empty datagram.</p>
 * <p>S: Send a datagram containing the time as a 32-bit binary number.</p>
 * <p>U: Receive the time datagram.</p>
 * @author yang
 * @date 2022/6/2
 * @description rfc868 时间协议服务 udp 实现

 */
public class TimeProtocolUdpServer {
    private final static int DEFAULT_PORT = 37;
    private int port;

    private void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(TimeProtocolUpdServerHandler.getInstance());
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void start() throws InterruptedException {
        start(DEFAULT_PORT);
    }

    public static void start(int port) throws InterruptedException {
        // check port
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("server port is illegal.");
        }
        TimeProtocolUdpServer server = new TimeProtocolUdpServer();
        server.port = port;
        server.run();
    }
}

@ChannelHandler.Sharable
class TimeProtocolUpdServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private final static TimeProtocolUpdServerHandler INSTANCE = new TimeProtocolUpdServerHandler();

    public static TimeProtocolUpdServerHandler getInstance() {
        return INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        InetSocketAddress sender = msg.sender();
        msg.content().clear();
        ByteBuf buf;
        // 创建4字节的byteBuf
        buf = ctx.alloc().buffer(4);
        long sendContent = System.currentTimeMillis() / 1000 + 2208988800L;
        // 转为32位无符号整数
        buf.writeBytes(toByteArray(sendContent));
        ctx.writeAndFlush(new DatagramPacket(buf, sender));
    }

    public static byte[] toByteArray(long value) {
        byte[] result = new byte[4];

        for (int i = 3; i >= 0; --i) {
            result[i] = (byte) ((int) (value & 255L));
            value >>= 8;
        }

        return result;
    }
}
