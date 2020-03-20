package com.ruoyi.common.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenm
 * @create 2019-08-01 15:22
 */
public class GsonUtil {
    private static Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }


    public static String toJson(Object object, String dateFormat) {
        GsonBuilder builder = new GsonBuilder().setDateFormat(dateFormat);
        return builder.create().toJson(object);
    }



    public static <T> T getEntity(String json, Class<T> clazz) {
        if (StringUtils.isNotBlank(json)) {
            return gson.fromJson(json, clazz);
        } else {
            return null;
        }
    }

    public static <T> List<T> getEntites(String json, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        if (StringUtils.isNotBlank(json)) {
            try {
                List<JsonObject> temp = gson.fromJson(json, new TypeToken<List<JsonObject>>() {
                }.getType());
                for (JsonObject jo : temp) {
                    list.add(gson.fromJson(jo, clazz));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Map<String, Object> getKeyMap(String json) {
        Map<String, Object> list = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(json)) {
            try {
                list = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<Map<String, Object>> getListKeyMaps(String json) {
        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        if (StringUtils.isNotBlank(json)) {
            try {
                map = gson.fromJson(json, new TypeToken<List<Map<String, Object>>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
