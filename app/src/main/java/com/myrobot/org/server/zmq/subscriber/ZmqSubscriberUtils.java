package com.myrobot.org.server.zmq.subscriber;

import com.myrobot.org.common.AppConfig;
import org.zeromq.ZMQ;

/**
 * @author Lixingxing
 */
public class ZmqSubscriberUtils {

    public static boolean subscribe(String key,ZmqSubscriberListener zmqSubscriberListener) {
        if(key == null){
            if(zmqSubscriberListener!=null){
                zmqSubscriberListener.onFail("key is null");
            }
            return false;
        }
        try{
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket socket = context.socket(ZMQ.SUB);
            socket.connect("tcp://"+ AppConfig.SERVER_ZMQ_IP +":"+AppConfig.SERVER_ZMQ_PORT);
            socket.subscribe(key.getBytes());
            boolean flag = true;
            while (!Thread.currentThread ().isInterrupted () && flag) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                byte[] reply = socket.recv();
                if(reply!=null && reply.length > 0){
                    flag = false;
                    if(zmqSubscriberListener!=null){
                        zmqSubscriberListener.onSuccess(reply);
                    }
                }
            }
            socket.close();
            context.term();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            if(zmqSubscriberListener!=null){
                zmqSubscriberListener.onFail("some Exception :" + e.getMessage());
            }
            return false;
        }
    }
}
