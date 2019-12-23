package com.myrobot.org.server.zmq.subscriber;

/**
 * @author Lixingxing
 */
public interface ZmqSubscriberListener {
    void onSuccess(byte[] data);

    void onFail(String error);
}
