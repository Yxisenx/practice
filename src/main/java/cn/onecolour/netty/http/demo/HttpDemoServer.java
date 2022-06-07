package cn.onecolour.netty.http.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author yang
 * @date 2022/6/7
 * @description
 */
public class HttpDemoServer {

    private final static int DEFAULT_PORT = 8080;
    private Integer port;

    private void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(16);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpRequestDecoder());
                            p.addLast(new HttpResponseEncoder());
                            p.addLast(new HttpDemoServerHandler());
                        }
                    });
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
        HttpDemoServer server = new HttpDemoServer();
        server.port = port;
        server.run();
    }
}
