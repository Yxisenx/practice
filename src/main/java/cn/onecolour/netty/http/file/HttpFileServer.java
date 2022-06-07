package cn.onecolour.netty.http.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author yang
 * @date 2022/6/7
 * @description
 */
public class HttpFileServer {
    private final static int DEFAULT_PORT = 8080;
    private Integer port;

    private void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new HttpFileChannelInitializer());
            Channel ch = bootstrap.bind(port).sync().channel();

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void startUp() throws InterruptedException {
        startUp(DEFAULT_PORT);
    }

    public static void startUp(int port) throws InterruptedException {
        HttpFileServer server = new HttpFileServer();
        server.port = port;
        server.run();
    }
}
