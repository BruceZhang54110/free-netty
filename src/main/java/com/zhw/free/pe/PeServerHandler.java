package com.zhw.free.pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;


public class PeServerHandler extends SimpleChannelInboundHandler {


    private  int counter;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8);
        System.out.println("-----start------\n"+ body + "\n------end------");

        String content = "receive" + ++counter;
        ByteBuf resp = Unpooled.copiedBuffer(content.getBytes());
        ctx.writeAndFlush(resp);


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channelActive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
