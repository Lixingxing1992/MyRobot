package com.myrobot.org.server.zmq;

/**
 * @author Lixingxing
 */

import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

public class Publisher {
    public static void send() {
        System.out.println("Publisher send");
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REP);
        String url = "tcp://*:5555";
        try {
            socket.bind(url);//绑定地址
        } catch (ZMQException e) {
            throw e;
        }
        boolean wait = true;
        while (wait) {//服务器一直循环
            byte[] request;
            try {
                request = socket.recv(0);//接收的客户端数据
                String getData=new String(request);
                if (getData.equals("lxx")) {
                    socket.send("OK".toString(),1);
                }else{
                    socket.send("error".toString(),1);
                }

            } catch (ZMQException e) {
                throw e;
            }
        }
        socket.close();
        context.term();
    }
}
