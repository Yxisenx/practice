package cn.onecolour.netty.time.udp;

import cn.onecolour.utils.TimeUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author yang
 * @date 2022/6/2
 * @description
 */
public class TimeProtocolUdpClient {

    private final static int DEFAULT_PORT = 37;
    private final static String DEFAULT_HOST = "127.0.0.1";

    private void run(InetSocketAddress address, @Nullable BiConsumer<ChannelHandlerContext, DatagramPacket> readConsumer) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new TimeProtocolUdpClientHandler(readConsumer));
            // port 0 means an available port to send udp packet
            Channel channel = bootstrap.bind(0).sync().channel();
            //noinspection InfiniteLoopStatement
            while (true) {
                channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("a".getBytes(StandardCharsets.UTF_8)), address));
                TimeUnit.SECONDS.sleep(5);
            }
        } finally {
            group.shutdownGracefully();
        }
    }


    public static void run(@Nullable BiConsumer<ChannelHandlerContext, DatagramPacket> readConsumer) throws InterruptedException {
        run(DEFAULT_HOST, readConsumer);
    }


    public static void run(String host, @Nullable BiConsumer<ChannelHandlerContext, DatagramPacket> readConsumer) throws InterruptedException {
        run(host, DEFAULT_PORT, readConsumer);
    }

    public static void run() throws InterruptedException {
        run(DEFAULT_HOST);
    }

    public static void run(String host) throws InterruptedException {
        run(host, DEFAULT_PORT);
    }

    public static void run(String host, int port) throws InterruptedException {
        run(host, port, null);
    }

    public static void run(String host, int port, @Nullable BiConsumer<ChannelHandlerContext, DatagramPacket> readConsumer) throws InterruptedException {
        new TimeProtocolUdpClient().run(new InetSocketAddress(host, port), readConsumer);
    }
}

class TimeProtocolUdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final static BiConsumer<ChannelHandlerContext, DatagramPacket> DEFAULT_CHANNEL_READ_CONSUMER = (ctx, msg) -> {
        long timestamp = msg.content().readUnsignedInt();
        String timeString = TimeUtils.timeFormat(timestamp - 2208988800L, TimeUnit.SECONDS);
        System.out.println("[Server Response] " + timeString);
    };
    private final BiConsumer<ChannelHandlerContext, DatagramPacket> readConsumer;

    public TimeProtocolUdpClientHandler(BiConsumer<ChannelHandlerContext, DatagramPacket> readConsumer) {
        if (readConsumer != null) {
            this.readConsumer = readConsumer;
        } else {
            this.readConsumer = DEFAULT_CHANNEL_READ_CONSUMER;
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        readConsumer.accept(ctx, msg);
    }
}
