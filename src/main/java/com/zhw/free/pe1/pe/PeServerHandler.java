package com.zhw.free.pe1.pe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;


public class PeServerHandler extends ChannelInboundHandlerAdapter {


    private  int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("PeServerHandler: channelRead0");
        ByteBuf in =(ByteBuf)msg;
        String string = in.toString(CharsetUtil.UTF_8);
        System.out.println("-----start------\n"+ string + "\n------end------");
        ctx.writeAndFlush(msg);


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("PeServerHandler: server channelActive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
