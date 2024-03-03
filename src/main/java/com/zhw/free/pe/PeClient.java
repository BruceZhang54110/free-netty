package com.zhw.free.pe;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

public class PeClient {

    private int port;
    private String address;

    public PeClient(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap()
                .group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ClientChannelInitializer());

        try {
            ChannelFuture future = bootstrap.connect(address, port).sync();
            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello  world, I'm online".getBytes()));
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        PeClient peClient = new PeClient(7788, "127.0.0.1");
        peClient.start();
    }
}
