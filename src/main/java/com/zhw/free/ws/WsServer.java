package com.zhw.free.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WsServer {

    private static final Logger LOG = LoggerFactory.getLogger(WsServer.class);

    public static void main(String[] args) {
        // 用于接收客户端的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 工作线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
        // 创建引导
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(new WebSocketlnitializer());

            ChannelFuture channelFuture = bootstrap.bind(9998).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
