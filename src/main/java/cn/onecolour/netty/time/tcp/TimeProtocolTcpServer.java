package cn.onecolour.netty.time.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <p>https://datatracker.ietf.org/doc/html/rfc868</p>
 * S: Listen on port 37 (45 octal).
 * <p>U: Connect to port 37.</p>
 * <p>S: Send the time as a 32 bit binary number.</p>
 * <p>U: Receive the time.</p>
 * <p>U: Close the connection.</p>
 * <p>S: Close the connection.</p>
 *
 * @author yang
 * @date 2022/6/6
 * @description rfc868 时间协议服务 udp 实现
 */
public class TimeProtocolTcpServer {
    private final static int DEFAULT_PORT = 37;
    private int port;

    private void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeProtocolTcpServerHandler());
                        }
                    });
            //绑定端口启动服务
            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("Time Server is starting up.");
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
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
        TimeProtocolTcpServer server = new TimeProtocolTcpServer();
        server.port = port;
        server.run();
    }
}
