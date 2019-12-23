package com.myrobot.org.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.myrobot.org.common.SysConfig;
import com.myrobot.org.server.rabbit.RabbitMQUtils;
import com.myrobot.org.server.redis.RedisUtils;


/**
 * @author Lixingxing
 */
public class RobotCheckService extends IntentService {
    public static final String TAG = "RobotCheckService";
    private static boolean isWork = true;
    public RobotCheckService() {
        super("RobotCheckService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isWork = true;
    }

    public static void startService(Context context){
        if(isWork == false){
            context.startService(new Intent(context,RobotCheckService.class));
        }
    }
    public static void stopService(){
        isWork = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        RabbitMQUtils.subscribe("qqkj_xiaofan_wanxiang","", handler);
        while (isWork){
            try {
                String jsonStr = RedisUtils.getReJsonValue("state-tree",".robot.schedule.current_scene");
                jsonStr = jsonStr.replace("\"","");
                SysConfig.SCENE_TYPE = jsonStr;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isWork = false;
    }



    private Handler handler = new Handler(msg ->
            false);
}
