package com.myrobot.org.util;

/**
 * 字符串处理工具类
 * Created by lixingxing
 */
public class BaseStringUtils {

    public static boolean isEmpty(String str){
        return null == str || "".equals(str) || str.replace(" " ,"").length() == 0;
    }

    public static boolean isEmpty(String str,String msg){
        boolean flag = isEmpty(str);
        if(flag && null != msg && !"".equals(msg)){
            BaseToastUtils.toast(msg);
        }
        return flag;
    }

    public static void check(){

    }

    /**
     * 检查是否是图片后缀
     * @param path
     * @return
     */
    public static boolean checkIsPicture(String path){
        path = path.toLowerCase();
        return path.endsWith(".jpg")
                || path.endsWith(".png")
                || path.endsWith(".jpeg");
    }
}
