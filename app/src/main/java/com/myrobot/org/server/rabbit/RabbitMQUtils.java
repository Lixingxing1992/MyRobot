package com.myrobot.org.server.rabbit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.myrobot.org.common.AppConfig;
import com.myrobot.org.server.zmq.subscriber.ZmqSubscriberListener;
import com.rabbitmq.client.*;
import org.zeromq.ZMQ;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author Lixingxing
 */
public class RabbitMQUtils {
    static final String TAG = "RabbitMQUtils";
    static ConnectionFactory factory;

    /**
     * 连接设置
     */
    private static void setupConnectionFactory() {
        if (factory == null) {
            factory = new ConnectionFactory();
            factory.setHost(AppConfig.SERVER_RABBIT_IP);
            factory.setPort(AppConfig.SERVER_RABBIT_PORT);
            factory.setUsername(AppConfig.SERVER_RABBIT_USERNAME);
            factory.setPassword(AppConfig.SERVER_RABBIT_PWD);
        }
    }

    /**
     * 收消息（从发布者那边订阅消息）
     */
    public static void subscribe(final String queueName, final String key, final Handler handler) {
        setupConnectionFactory();
        try {
            //连接
            Connection connection = factory.newConnection();
            //通道
            final Channel channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(queueName, true, false, false, null);

            String routingKey = queueName + "." + key;

            String EXCHANGER_NAME = queueName + ".exchange";

            Log.e(TAG, "订阅的队列是 : [" + queueName + "]，routingKey = [" + routingKey + "]");

            //绑定队列到交换机 转发器
            channel.queueBind(queueName, EXCHANGER_NAME, routingKey);

            //保证一次只发一个
            channel.basicQos(1);
            //实现Consumer的最简单方法是将便捷类DefaultConsumer子类化。可以在basicConsume 调用上传递此子类的对象以设置订阅：
            channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("客户端接收的是: [" + msg + "]");
//                    Map<String, Object> headers = properties.getHeaders();
//                    System.out.println("客户端接收的 id 是: [" + headers.get("id") + "]");
                    long deliveryTag = envelope.getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    //从message池中获取msg对象更高效
                    Message uimsg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", msg);
                    uimsg.setData(bundle);
                    handler.sendMessage(uimsg);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


}
