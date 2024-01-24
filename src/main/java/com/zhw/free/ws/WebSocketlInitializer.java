package com.zhw.free.ws;

import com.zhw.free.ws.event.ConnectionEventHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Bruce
 */
public class WebSocketlInitializer extends ChannelInitializer {

    private ConnectionEventHandler connectionEventHandler;

    public WebSocketlInitializer(ConnectionEventHandler connectionEventHandler) {
        this.connectionEventHandler = connectionEventHandler;
    }


    /**
     * 初始化子通道
     * 当父通道 成功接收 一个请求连接，并成功创建一个子通道之后，
     * 就会初始化子通道，就会调用此处的ChannelInitializer的实例，调用初始化方法。
     * @param ch            the {@link Channel} which was registered.
     */
    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // 因为基于http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());
        // http是以块方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler())
                .addLast("log-handler", new LoggingHandler(LogLevel.WARN))

                // http数据在传输过程中是分段, HttpObjectAggregator , 就是可以将多个段聚合
                .addLast(new HttpObjectAggregator(8192));
        // WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
        // 服务端逻辑处理
        pipeline.addLast("webSocketServerHandler", new WebSocketServerHandler());
        // 事件通知逻辑
        pipeline.addLast("connectionEventHandler", connectionEventHandler);
        pipeline.fireUserEventTriggered(ConnectionEventType.CONNECT);

    }
}
