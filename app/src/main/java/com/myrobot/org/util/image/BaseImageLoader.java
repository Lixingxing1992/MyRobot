package com.myrobot.org.util.image;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by lixingxing on 2019/6/12.
 */
public interface BaseImageLoader {
    public void load_http_image(ImageView view , String url);
    public void load_http_image(ImageView view, String url, int defaultPic);

    // 加载圆形图片
    public  void loadCircleImage(ImageView imageView,String url);
    public  void loadCircleImage(ImageView imageView,String url,int defaultPic);
    public  void loadCircleImage(Context context, ImageView imageView, String url, int defaultPic);

    /**
     * 加载圆形带圆边图片
     * @param context
     * @param url
     * @param imageView
     */
//        public  void loadCircleWithFrameImage(Context context, String url, ImageView imageView,int borderWidth,int borderColor,int defaultPic) {
//            RequestOptions options = RequestOptions
//                    .bitmapTransform(new GlideCircleWithFrameTransform(borderWidth,borderColor))
//                    .placeholder(defaultPic)
//                    .error(defaultPic);
//            Glide.with(context).load(url).apply(options).into(imageView);
//        }

    /**
     * 加载圆角图片
     *
     */
    public  void loadRoundImage(ImageView imageView,String url);
    public  void loadRoundImage(ImageView imageView,String url,int defaultPic);
    public  void loadRoundImage(Context context,ImageView imageView, String url,int defaultPic);
}
