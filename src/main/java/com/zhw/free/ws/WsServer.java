package com.zhw.free.ws;

import com.zhw.free.ws.event.ConnectionEventHandler;
import com.zhw.free.ws.event.ConnectionEventSubject;
import com.zhw.free.ws.event.EventObsever;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class WsServer {

    private ConnectionEventSubject connectionEventSubject = new ConnectionEventSubject();

    private ConnectionEventHandler connectionEventHandler;


    private static final Logger LOG = LoggerFactory.getLogger(WsServer.class);

    public void addObsever(ConnectionEventType connectionEventType, EventObsever eventObsever) {
        this.connectionEventSubject.addEventObsever(connectionEventType, eventObsever);
    }

    public void init() {
        connectionEventHandler = new ConnectionEventHandler(connectionEventSubject);
    }

    public void start() {

        init();

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
                    .childHandler(new WebSocketlnitializer(connectionEventHandler));

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
