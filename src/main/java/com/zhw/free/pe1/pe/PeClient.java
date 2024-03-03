package com.zhw.free.pe1.pe;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientChannelInitializer());
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect(address, port).sync();
            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello  world, I'm online\n".getBytes()));
            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello  world, I'm online\n".getBytes()));
            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello  world, I'm online\n".getBytes()));
            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello  world, I'm online\n".getBytes()));
            future.channel().writeAndFlush(Unpooled.copiedBuffer("hello  world, I'm online\n".getBytes()));
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
