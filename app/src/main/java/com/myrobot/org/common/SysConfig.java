package com.myrobot.org.common;

import com.myrobot.org.server.redis.RedisUtils;

/**
 * @author Lixingxing
 */
public class SysConfig {
    // 设备号
    public static String KEY_DEVICESN = "device_sn";
    // 商家编号
    public static String KEY_MERCHANTID = "merchant_id";


    public static void initSysConfig(){
        KEY_DEVICESN = RedisUtils.getReJsonValue("sys:config",".device_sn").replace("\"","");
        KEY_MERCHANTID = RedisUtils.getReJsonValue("sys:config",".merchant_id").replace("\"","");
    }


    public static String SCENE_TYPE = "none";
}
