package com.myrobot.org.server.redis;

import android.util.Log;
import com.myrobot.org.common.AppConfig;
import org.redisson.Redisson;
import org.redisson.api.RScript;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

/**
 * @author Lixingxing
 */
public class RedisUtils {
    public static final String TAG = "RedisUtils";

    private static RedissonClient redissonClient;
    static synchronized RedissonClient redisson() {
        Config config = new Config();
        config.setCodec(new StringCodec());
        config.useSingleServer()
                .setAddress("redis://"+AppConfig.SERVER_REDIS_IP+":"+AppConfig.SERVER_REDIS_PORT);
        return Redisson.create(config);
    }
    public static void initClient(){
        try {
            redissonClient = redisson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getRedisValue(String key){
        if (redissonClient == null){
            return  null;
        }
        String result = (String) redissonClient.getBucket(key).get();
        if (result == null){
            Log.w(TAG, "redis get failed, key = [ "+key+" ]");
        }else {
            Log.i(TAG, "redis get success,key = ["+key+"], result = ["+result+"]");
        }
        return result;
    }
    public Boolean setRedisValue(String key,String value){
        if (redissonClient == null){
            Log.w(TAG, "redis set failed, key = ["+key+"], value = ["+value+"]");
            return false;
        }
        try {
            redissonClient.getBucket(key).set(value);
            if (redissonClient ==null) {
                Log.i(TAG, "redis set success, key = ["+key+"], value = ["+value+"]");
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "redis set error, key = ["+key+"], value = ["+value+"]", e);
            return false;
        }
    }

    public static String getReJsonValue(String key ,String path){
        if(redissonClient == null){
            initClient();
        }
        if (redissonClient == null){
            return null;
        }
        try {
            String result =
                    (String)redissonClient.getScript()
                            .evalAsync(RScript.Mode.READ_ONLY,"return redis.call('JSON.GET','"+key+"','NOESCAPE','"+path+"')",RScript.ReturnType.VALUE).get();
            if (result == null){
                Log.w(TAG, "redis get failed, key = [ "+key+" ],path = ["+path+"]");
            }else {
                Log.i(TAG, "redis get success,key = ["+key+"],path = ["+path+"], result = ["+result+"]");
            }
            return result;
        } catch (Exception e) {
            Log.e(TAG, "ReJson get error, key = ["+key+"],path = ["+path+"]", e);
            return null;
        }
    }
    public static Boolean setReJsonValue(String key,String path,String value){
        if (redissonClient == null){
            Log.w(TAG, "redis set failed, key = ["+key+"], value = ["+value+"]");
            return false;
        }
        try {
            redissonClient.getScript().evalAsync(RScript.Mode.READ_WRITE,"return redis.call('JSON.SET','"+key+"','"+path+"','"+value+"')",RScript.ReturnType.STATUS).get();
            return true;
        }catch (Exception e){
            Log.e(TAG, "redis set error, key = ["+key+"],path = ["+path+"] ,value = ["+value+"]", e);
            return false;
        }
    }

    public static Long publishTopic(String topic,String message){
        if(redissonClient == null){
            initClient();
        }
        if (redissonClient == null){
            Log.w(TAG, "redis publish failed, topic = ["+topic+"], message = ["+message+"]");
            return  -1L;
        }
        try {
            Long result = redissonClient.getTopic(topic).publish(message);
            if (result == null){
                Log.w(TAG, "redis publish failed, topic = ["+topic+"], message = ["+message+"]");
            }else {
                Log.i(TAG,"redis publish success, topic = ["+topic+"], " +
                        "message = ["+message+"], [ "+result+" ] client received it");
            }
            return result;
        }catch (Exception e){
            Log.e(TAG, "redis publish error, topic = ["+topic+"], message = ["+message+"]", e);
            return -1L;
        }
    }
    public static Boolean subscribeTopic(String topic){
        if(redissonClient == null){
            initClient();
        }
        if (topic == null){
            Log.w(TAG, "subscribe topic [ "+topic+" ] failed, topic is null");
            return false;
        }
        if (redissonClient ==null){
            Log.e(TAG, "subscribe topic [ "+topic+" ] failed");
            return false;
        }
        try {
            RTopic rTopic = redissonClient.getTopic(topic);
            if(rTopic.countListeners() <= 0){
                rTopic.addListener(String.class, new MessageListener<String>() {
                    @Override
                    public void onMessage(CharSequence channel, String msg) {
//                        receivedTopic(channel.toString(), msg);
                    }
                });
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static Boolean subscribeTopic(String topic,MessageListener messageListener){
        if(redissonClient == null){
            initClient();
        }
        if (topic == null){
            Log.w(TAG, "subscribe topic [ "+topic+" ] failed, topic is null");
            return false;
        }
        if (redissonClient ==null){
            Log.e(TAG, "subscribe topic [ "+topic+" ] failed");
            return false;
        }
        try {
            RTopic rTopic = redissonClient.getTopic(topic);
            Log.e(TAG, "subscribe topic [ "+topic+" ] begin");
            if(rTopic.countListeners() <= 0){
                rTopic.addListener(String.class, messageListener);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static Boolean unSubscribeTopic(String topic,MessageListener messageListener){
        if(redissonClient == null){
            initClient();
        }
        if (topic == null){
            Log.w(TAG, "subscribe topic [ "+topic+" ] failed, topic is null");
            return false;
        }
        if (redissonClient ==null){
            Log.e(TAG, "subscribe topic [ "+topic+" ] failed");
            return false;
        }
        try {
            RTopic rTopic = redissonClient.getTopic(topic);
            Log.e(TAG, "subscribe topic [ "+topic+" ] begin");
            if(rTopic.countListeners() > 0){
                rTopic.removeListener(messageListener);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
