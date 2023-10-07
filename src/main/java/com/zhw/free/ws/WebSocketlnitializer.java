package com.zhw.free.ws;

import com.zhw.free.ws.event.ConnectionEventHandler;
import com.zhw.free.ws.event.ConnectionEventSubject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Bruce
 */
public class WebSocketlnitializer extends ChannelInitializer {

    private ConnectionEventHandler connectionEventHandler;

    public WebSocketlnitializer(ConnectionEventHandler connectionEventHandler) {
        this.connectionEventHandler = connectionEventHandler;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 因为基于http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());
        // http是以块方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());
        // http数据在传输过程中是分段, HttpObjectAggregator , 就是可以将多个段聚合
        pipeline.addLast(new HttpObjectAggregator(8192));
        // WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
        // 服务端逻辑处理
        pipeline.addLast("webSocketServerHandler", new WebSocketServerHandler());
        // 事件通知逻辑
        pipeline.addLast("connectionEventHandler", connectionEventHandler);
        pipeline.fireUserEventTriggered(ConnectionEventType.CONNECT);

    }
}
