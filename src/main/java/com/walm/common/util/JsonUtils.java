package com.walm.common.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
/**
 * JsonUtils
 *
 * @author wangjn
 * @date 2019/4/19
 */
public class JsonUtils {

    private static final Gson gson = new GsonBuilder()
            // 不过滤空值
            .serializeNulls()
            // 设置字段命名转换规则 --- 驼峰 eg: user_name -> userName userName -> user_name
            //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            // 设置字段序列化/反序列化过滤规则
            .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
            .create();


    /**
     * 对象序列化Json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Json字符串反序列化对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * Json字符串反序列化成泛型对象
     *
     * eg: List<String> ->  List<String> ss = </>fromJson(json, new TypeToken<List<String>>() {}.getType())
     *     Demo<A> ->  Demo<A> de = fromJson(json, new TypeToken<Demo<A>>() {}.getType())
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static void main(String[] args) {
        String s = "{\"user_name\":\"wangjunnan\",\"age_ad\":\"ds\"}";
        A a = fromJson(s, A.class);
        System.out.println(a);
        System.out.println(toJson(a));
    }

    @Data
    public static class A {
        private String userName;
        private String ageAd;
    }
}
