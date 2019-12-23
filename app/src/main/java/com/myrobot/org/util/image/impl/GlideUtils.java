package com.myrobot.org.util.image.impl;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.myrobot.org.base.BaseUtils;
import com.myrobot.org.util.image.BaseImageLoader;

/**
 * Created by lixingxing on 2019/6/12.
 */
public class GlideUtils implements BaseImageLoader {
    @Override
    public  void load_http_image(ImageView view , String url){
        Glide.with(BaseUtils.getContext())
                .load(url).into(view);
    }
    @Override
    public  void load_http_image(ImageView view, String url, int defaultPic){
        RequestOptions requestOptions= new RequestOptions();
        requestOptions.placeholder(defaultPic);
        requestOptions.error(defaultPic);
        requestOptions.centerInside();
        Glide.with(BaseUtils.getContext())
                .load(url)
                .thumbnail(0.1f)
                .apply(requestOptions)
                .into(view);
    }

    // 加载圆形图片
    @Override
    public  void loadCircleImage(ImageView imageView,String url) {
        loadCircleImage(BaseUtils.getContext(),imageView,url,0);
    }
    @Override
    public  void loadCircleImage(ImageView imageView,String url,int defaultPic) {
        loadCircleImage(BaseUtils.getContext(),imageView,url,defaultPic);
    }
    @Override
    public  void loadCircleImage(Context context, ImageView imageView, String url, int defaultPic) {
        RequestOptions options = new RequestOptions();
        if(defaultPic != 0){
            options.placeholder(defaultPic)
                    .error(defaultPic);
        }
        options.circleCrop();
        Glide.with(BaseUtils.getContext()).load(url).apply(options).into(imageView);
    }

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
    @Override
    public  void loadRoundImage(ImageView imageView,String url) {
        loadRoundImage(BaseUtils.getContext(),imageView,url,0);
    }
    @Override
    public  void loadRoundImage(ImageView imageView,String url,int defaultPic) {
        loadRoundImage(BaseUtils.getContext(),imageView,url,defaultPic);
    }
    @Override
    public  void loadRoundImage(Context context,ImageView imageView, String url,int defaultPic) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(20))
                .placeholder(defaultPic)
                .error(defaultPic);
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}
