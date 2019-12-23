package com.myrobot.org.common;

/**
 * @author Lixingxing
 */
public class AppConfig {

    // 商品图片url
    public static final String URL_IMAGE = "https://pro.eventec.shop/oss/goods/";
    // 购买url
    public static final String URL_BUY_CODE_URL = "http://139.196.51.102:8083/goods/purchase?params=";
    // ZMQ
    public static final String SERVER_ZMQ_IP = "192.168.199.17";
    public static final int SERVER_ZMQ_PORT = 6666;
    // REDIS
    public static final String SERVER_REDIS_IP = "192.168.199.38";
    public static final int SERVER_REDIS_PORT = 6379;

    // RABBIT
    public static final String SERVER_RABBIT_IP = "129.204.127.138";
    public static final int SERVER_RABBIT_PORT = 5672;
    public static final String SERVER_RABBIT_USERNAME = "gongzhou";
    public static final String SERVER_RABBIT_PWD = "123456";


    // 最大闲置个数
    public static final int SERVER_REDIS_MAXIDLE = 30;
    // 最小闲置个数
    public static final int SERVER_REDIS_MINIDLE = 10;
    // 最大连接数
    public static final int SERVER_REDIS_MAXTOTAL = 50;


}
