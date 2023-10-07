package com.zhw.free.ws;

import com.zhw.free.ws.event.CONNECTEventObsever;

public class WsServerMain {

    CONNECTEventObsever connectEventObsever = new CONNECTEventObsever();


    public WsServerMain() {
        WsServer wsServer = new WsServer();
        wsServer.addObsever(ConnectionEventType.CONNECT, connectEventObsever);
        wsServer.start();
    }


    public static void main(String[] args) {
        new WsServerMain();
    }


}
