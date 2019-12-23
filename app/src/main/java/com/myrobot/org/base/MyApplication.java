package com.myrobot.org.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.myrobot.org.BuildConfig;
import com.myrobot.org.common.SysConfig;
import com.myrobot.org.server.redis.RedisUtils;
import com.myrobot.org.service.RobotCheckService;
import com.myrobot.org.util.image.BaseImageUtils;
import com.myrobot.org.util.image.impl.GlideUtils;
import com.xhttp.lib.BaseHttpUtils;
import com.xhttp.lib.impl.data.JsonDataListener;
import com.xhttp.lib.impl.service.JsonHttpService;
import io.paperdb.Paper;

/**
 * @author Lixingxing
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        BaseUtils.init(getApplicationContext());
        Paper.init(getApplicationContext());
        BaseImageUtils.init(new GlideUtils());


        // 初始化 redis服务
        RedisUtils.initClient();
        // 初始化参数信息
        SysConfig.initSysConfig();
        // 初始化 redis服务
//        MyRedisUtils.initClient();
        // 初始化程序长服务
        RobotCheckService.startService(getApplicationContext());

        // 初始化http请求
        BaseHttpUtils.init(this, BuildConfig.DEBUG);
        BaseHttpUtils.init(JsonHttpService.class, JsonDataListener.class);


        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        Log.e("phoneInfo","metrics.widthPixels = " + metrics.widthPixels +
                ",metrics.heightPixels = " + metrics.heightPixels +
                ",metrics.densityDpi = " + metrics.densityDpi +
                ", metrics.density = " + metrics.density
        );
    }
}
