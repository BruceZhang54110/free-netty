package com.zhw.free.ws.event;

import com.zhw.free.ws.ConnectionEventType;
import com.zhw.free.ws.NamedThreadFactory;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhw
 */
@Sharable
public class ConnectionEventHandler extends ChannelDuplexHandler {

    private static final Logger log = LoggerFactory.getLogger(ConnectionEventHandler.class);

    public ConnectionEventHandler(ConnectionEventSubject connectionEventSubject) {
        // 注入主题
        this.connectionEventSubject = connectionEventSubject;
        if (this.eventExecutor == null) {
            this.eventExecutor = new ConnectionEventExecutor();
        }

    }

    /**
     * 观察事件的主题
     */
    private ConnectionEventSubject connectionEventSubject;

    /**
     * 事件执行器
     */
    private ConnectionEventExecutor eventExecutor;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof ConnectionEventType) {
            ConnectionEventType connectionEventType = (ConnectionEventType) evt;
            switch (connectionEventType) {
                case CONNECT:
                    onEvent(ConnectionEventType.CONNECT);
                case CLOSE:
                    onEvent(ConnectionEventType.CLOSE);
            }
        }
    }

    /**
     * 触发事件
     * @param type
     */
    private void onEvent(final ConnectionEventType type) {
        log.info("收到事件， onEvent start");
        if (this.connectionEventSubject != null) {
            this.eventExecutor.onEvent(() -> ConnectionEventHandler.this.connectionEventSubject.onEvent(type));
        }
        log.info("onEvent end");
    }

    public class ConnectionEventExecutor {

        private static final Logger log = LoggerFactory.getLogger(ConnectionEventExecutor.class);

        ExecutorService executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10000),
                new NamedThreadFactory("Bolt-conn-event-executor", true));

        /**
         * Process event.
         *
         * @param runnable Runnable
         */
        public void onEvent(Runnable runnable) {
            try {
                executor.execute(runnable);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        }
    }


}
