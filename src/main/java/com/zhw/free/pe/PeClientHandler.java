package com.zhw.free.pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;


public class PeClientHandler extends SimpleChannelInboundHandler {


    private  int counter;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8);
        System.out.println(body + " count:" + ++counter + "----end----\n");

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channelActive");
//        byte[] req = ("我是一条测试消息，快来读我吧，啦啦啦").getBytes();
//        for (int i = 0; i < 100; i++) {
//            ByteBuf message = Unpooled.buffer(req.length);
//            message.writeBytes(req);
//            ctx.writeAndFlush(message);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client is close");
    }
}
