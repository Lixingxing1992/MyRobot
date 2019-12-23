package com.myrobot.org.util.image;

import com.myrobot.org.util.image.impl.GlideUtils;

/**
 * 图片加载工具类
 *
 * 初始化设置 init
 * 调用 create()
 * 调用时设置 create(BaseImageLoader imageShowInterfaces)
 * Created by lixingxing
 */
public class BaseImageUtils {
    static BaseImageLoader imageShowInterface;
    private BaseImageUtils() {
        throw new RuntimeException(" only static method ");
    }

    public static void init(BaseImageLoader imageShowInterfaces){
        if(imageShowInterfaces == null){
            imageShowInterfaces = new GlideUtils();
        }
        imageShowInterface = imageShowInterfaces;
    }

    // 默认是 GlideUtils
    public static BaseImageLoader create(){
        if(imageShowInterface == null){
            imageShowInterface = new GlideUtils();
        }
        return imageShowInterface;
    }
    //默认是 GlideUtils
    private static BaseImageLoader create(BaseImageLoader imageShowInterfaces) {
        if(imageShowInterfaces == null){
            imageShowInterfaces = new GlideUtils();
        }
        return imageShowInterfaces;
    }

}
