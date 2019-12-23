package com.myrobot.org.buy;

import com.google.gson.Gson;
import com.myrobot.org.buy.model.GoodModel;
import com.myrobot.org.server.redis.RedisUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 商品工具类
 *
 * @author Lixingxing
 */
public class GoodUtils {
    // 获取商品列表（带详情）
    public static List<GoodModel> getGoodModels() {
        return getGoodModels2();
    }
    private static List<GoodModel> getGoodModels2() {
        List<GoodModel> list = new ArrayList<>();
        String price = RedisUtils.getReJsonValue("state-tree", ".robot.stocks.price");
        String pos  = RedisUtils.getReJsonValue("state-tree", ".robot.stocks.pos");
        try {
            JSONObject priceObject = new JSONObject(price);

            for (Iterator<String> it = priceObject.keys(); it.hasNext(); ) {
                String s = it.next();
                int priceNum = priceObject.getInt(s);
                s = s.replace("gcode", "");
                GoodModel goodModel = getGoodModel(s);
                if (goodModel != null) {
                    goodModel.setRealPrice(priceNum);
                    getStockNums(goodModel,pos);
                    list.add(goodModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static GoodModel getGoodModel(String code) {
        GoodModel goodModel = null;
        try {
            String sn = RedisUtils.getReJsonValue("sys:stocks:" + code, ".");
            goodModel = new Gson().fromJson(sn, GoodModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodModel;
    }

    // 获取商品剩余库存
    public static int getStockNums(GoodModel goodModel,String posStr) {
        if (goodModel != null) {
            try {
                if(posStr == null || "".equals(posStr)){
                    posStr = RedisUtils.getReJsonValue("state-tree", ".robot.stocks.pos");
                }
                JSONArray posArray = new JSONArray(posStr);
                int stock = 0;
                for (int i = 0; i < posArray.length(); i++) {
                    String po = posArray.get(i).toString();
                    if (goodModel.getGoodsCode().equals(po)) {
                        stock += 1;
                    }
                }
                goodModel.setStock(stock);
                return stock;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
