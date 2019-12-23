package com.myrobot.org.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import kotlin.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Lixingxing
 */
public class JsonUtils {

    public static String getJson(Pair<String,Object>... pairs){
        JSONObject jsonObject = new JSONObject();
        for (Pair pair : pairs) {
            try {
                if(pair.getSecond() instanceof List<?>){
                    String string = new Gson().toJson(pair.getSecond());
                    JSONArray job = new JSONArray(string);
                    jsonObject.put((String)pair.getFirst(), job);
                }else{
                    jsonObject.put((String) pair.getFirst(),pair.getSecond());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    public  static <T extends Object>T getObj(String json,String key){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            return (T)jsonObject.opt(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
