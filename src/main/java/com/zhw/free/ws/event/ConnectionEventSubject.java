package com.zhw.free.ws.event;

import com.zhw.free.ws.ConnectionEventType;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 观察者模式当中的主题
 * @author zhw
 */
public class ConnectionEventSubject {

    /**
     * 存放不同类型的观察者
     */
    private ConcurrentHashMap<ConnectionEventType, List<EventObsever>> processors = new ConcurrentHashMap<>(3);

    /**
     * 执行观察该主题的的观察者定义的事件
     * @param connectionEventType
     */
    public void onEvent(ConnectionEventType connectionEventType) {
        List<EventObsever> connectionEventObsevers = this.processors.get(connectionEventType);
        connectionEventObsevers.forEach(eventObsever -> eventObsever.onEvent());
    }

    /**
     * 添加新的观察者
     * @param connectionEventType
     * @param eventObsever
     */
    public void addEventObsever(ConnectionEventType connectionEventType, EventObsever eventObsever) {
        List<EventObsever> eventObsevers = this.processors.get(connectionEventType);
        if (eventObsevers == null) {
            this.processors.putIfAbsent(connectionEventType, new CopyOnWriteArrayList<EventObsever>());
            eventObsevers = this.processors.get(connectionEventType);
        }
        eventObsevers.add(eventObsever);
    }
}
