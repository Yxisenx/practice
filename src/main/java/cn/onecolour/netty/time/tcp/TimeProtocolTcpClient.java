package cn.onecolour.netty.time.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author yang
 * @date 2022/6/6
 * @description
 */
@SuppressWarnings("unused")
public class TimeProtocolTcpClient {

    private final static int DEFAULT_PORT = 37;
    private final static String DEFAULT_HOST = "127.0.0.1";

    private void run(InetSocketAddress address, @Nullable BiConsumer<ChannelHandlerContext, ByteBuf> readConsumer) throws InterruptedException {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeProtocolTcpClientHandler(readConsumer));
                        }
                    });
            //noinspection InfiniteLoopStatement
            while (true) {
                ChannelFuture future = bootstrap.connect(address).sync();
                future.channel().closeFuture().sync();
                TimeUnit.SECONDS.sleep(5);
            }
        } finally {
            workGroup.shutdownGracefully();
        }
    }


    public static void run(@Nullable BiConsumer<ChannelHandlerContext, ByteBuf> readConsumer) throws InterruptedException {
        run(DEFAULT_HOST, readConsumer);
    }


    public static void run(String host, @Nullable BiConsumer<ChannelHandlerContext, ByteBuf> readConsumer) throws InterruptedException {
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

    public static void run(String host, int port, @Nullable BiConsumer<ChannelHandlerContext, ByteBuf> readConsumer) throws InterruptedException {
        new TimeProtocolTcpClient().run(new InetSocketAddress(host, port), readConsumer);
    }
}
