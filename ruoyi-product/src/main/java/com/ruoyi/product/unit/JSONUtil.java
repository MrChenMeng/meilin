package com.ruoyi.product.unit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author chenm
 * @create 2019-09-20 9:44
 */
public class JSONUtil {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name","张三");
        map.put("code","201809001");
        String jsonString= JSON.toJSONString(map);
    }

    public static String mapToJson(Map<String,Object> map){
        return JSON.toJSONString(map);
    }

    /**
     * 转义json字符串里面的value的特殊字符，key不用管
     *
     * @param jsonStr
     * @return 返回替换后的字符串
     */
    public static String replaceJsonValue(String jsonStr) {
        JSONObject obj = JSON.parseObject(jsonStr);
        replaceValue(obj);
        return obj.toJSONString();
    }

    /**
     * 递归转义value的值，目前是将所有的value的结尾都添加一个“$”，具体实现是，根据具体的需求来。
     *
     * @param obj
     */
    private static void replaceValue(JSONObject obj) {
        Set<Map.Entry<String, Object>> keys = obj.entrySet();
        keys.forEach(key -> {
            Object value = obj.get(key.getKey());
            if (value instanceof JSONObject) { //如果还是JSONObject，继续递归遍历
                replaceValue((JSONObject) value);
            } else if (value instanceof String) {//如果是String（这里没有处理其他类型，如int，double等），表示为具体的value值
                obj.put(key.getKey(), value+"$");
            }
        });
    }


}
