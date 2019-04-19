package com.sunshine.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * JsonUtils
 *
 * @author wangjn
 * @date 2019/4/19
 */
public class JsonUtils {

    /**
     * 对象序列化Json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 对象序列化Json字符串
     *
     * @param obj
     * @param features
     * @return
     */
    public static String toJson(Object obj, SerializeFilter serializeFilter, SerializerFeature... features) {
        return JSON.toJSONString(obj,serializeFilter, features);
    }

    /**
     * Json字符串反序列化对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * Json字符串反序列化成List
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> clazz){
        return JSON.parseArray(json, clazz);
    }
}
