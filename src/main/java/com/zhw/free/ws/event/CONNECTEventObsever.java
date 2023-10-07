package com.zhw.free.ws.event;

public class CONNECTEventObsever implements EventObsever {
    @Override
    public void onEvent() {
        System.out.println("连接创建事件发生， 调用方法。。。");
    }
}
