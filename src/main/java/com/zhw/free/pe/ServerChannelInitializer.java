package com.zhw.free.pe;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast(new LineBasedFrameDecoder(2048));
//        pipeline.addLast("decoder", new StringDecoder());
//        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast(new PeServerHandler());
    }
}
