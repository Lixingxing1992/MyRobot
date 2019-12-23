package com.myrobot.org.util;


import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.myrobot.org.base.BaseUtils;

/**
 * Toast显示工具类
 * Created by lixingxing
 */
public class BaseToastUtils {

    static Handler handler = new Handler(Looper.getMainLooper());

    static String strs = "";
    static long time = 0L;

    public static final long maxTime = 1000L;

    public static void toast(final String str,final int type){
        if(strs.equals(str) && (System.currentTimeMillis() - time) < maxTime){
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                strs = str;
                time = System.currentTimeMillis();
                Toast.makeText(BaseUtils.getContext(),str, type == 0?Toast.LENGTH_SHORT:Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void toast(final String str){
        toast(str,0);
    }
    public static void toastLong(final String str){
        toast(str,1);
    }
}
