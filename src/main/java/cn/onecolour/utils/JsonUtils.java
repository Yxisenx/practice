package cn.onecolour.utils;


import com.alibaba.fastjson.JSONObject;

/**
 * @author yang
 * @date 2022/6/7
 * @description
 */
public class JsonUtils {

    public static <T> String toJsonString (T obj) {
        return JSONObject.toJSONString(obj);
    }
}
