package com.walm.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>OKHttpUtils</p>
 * <p>
 * <p>OKHttp 支持相同host连接复用 异步同步请求</p>
 *
 * @author wangjn
 * @date 2019/6/19
 */
@Slf4j
public class OKHttpUtils {

    private static final OkHttpClient client;

    static {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(64);
        dispatcher.setMaxRequestsPerHost(5);

        client = new OkHttpClient().newBuilder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                //.addInterceptor()
                //.cookieJar() 实现cookie，OkHttp 默认不支持cookie
                .dispatcher(dispatcher)
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .build();
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    /**
     * get请求
     *
     * @param url     请求url
     * @param params  请求参数
     * @param headers 请求头
     * @return
     */
    public static String get(String url, Map<String, Object> params, Map<String, Object> headers) {
        String completeUrl = HttpURLConnectionUtils.getCompleteUrl(url, params);
        Request.Builder builder = new Request.Builder().url(completeUrl);
        if (headers != null) {
            headers.forEach((key, value) -> builder.header(key, String.valueOf(value)));
        }
        return execute(builder.build());
    }

    /**
     * get请求
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     */
    public static String get(String url, Map<String, Object> params) {
        return get(url, params, null);
    }

    /**
     * JSON 格式提交 POST请求
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     */
    public static String postJson(String url, Map<String, Object> params, Map<String, Object> headers) {
        JSONObject jsonObject = new JSONObject(params);
        String jsonString = jsonObject.toJSONString();
        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (headers != null) {
            headers.forEach((key, value) -> builder.header(key, String.valueOf(value)));
        }
        return execute(builder.build());
    }

    /**
     * postJson
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     */
    public static String postJson(String url, Map<String, Object> params) {
        return postJson(url, params, null);
    }

    /**
     * post 方式提交表单数据
     *
     * @param url     请求url
     * @param params  请求参数
     * @param headers 请求头
     * @return
     */
    public static String postForm(String url, Map<String, Object> params, Map<String, Object> headers) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            params.forEach((key, value) -> formBuilder.add(key, String.valueOf(value)));
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(formBuilder.build());
        if (headers != null) {
            headers.forEach((key, value) -> requestBuilder.header(key, String.valueOf(value)));
        }
        return execute(requestBuilder.build());
    }

    /**
     * postForm
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     */
    public static String postForm(String url, Map<String, Object> params) {
        return postForm(url, params, null);
    }


    /**
     * 异步get请求
     *
     * @param url      请求url
     * @param params   请求参数
     * @param headers  请求头
     * @param callback 异步回调函数
     */
    public static void asyncGet(String url, Map<String, Object> params, Map<String, Object> headers, Callback callback) {
        String completeUrl = HttpURLConnectionUtils.getCompleteUrl(url, params);
        Request.Builder builder = new Request.Builder().url(completeUrl);
        if (headers != null) {
            headers.forEach((key, value) -> builder.header(key, String.valueOf(value)));
        }
        Call call = client.newCall(builder.build());
        call.enqueue(callback);
    }

    /**
     * 异步请求
     *
     * @param url      请求url
     * @param params   请求参数
     * @param callback 异步回调函数
     */
    public static void asyncGet(String url, Map<String, Object> params, Callback callback) {
        asyncGet(url, params, null, callback);
    }

    /**
     * 异步postJson
     *
     * @param url      请求url
     * @param params   请求参数
     * @param headers  请求头
     * @param callback 异步回调函数
     */
    public static void asyncPostJson(String url, Map<String, Object> params, Map<String, Object> headers, Callback callback) {
        JSONObject jsonObject = new JSONObject(params);
        String jsonString = jsonObject.toJSONString();
        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (headers != null) {
            headers.forEach((key, value) -> builder.header(key, String.valueOf(value)));
        }
        client.newCall(builder.build()).enqueue(callback);
    }

    /**
     * 异步post请求
     *
     * @param url      请求url
     * @param params   请求参数
     * @param callback 异步回调函数
     */
    public static void asyncPostJson(String url, Map<String, Object> params, Callback callback) {
        asyncPostJson(url, params, null, callback);
    }

    /**
     * 异步postFrom
     *
     * @param url      请求url
     * @param params   请求参数
     * @param headers  请求头
     * @param callback 异步回调函数
     */
    public static void asyncPostFrom(String url, Map<String, Object> params, Map<String, Object> headers, Callback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            params.forEach((key, value) -> formBuilder.add(key, String.valueOf(value)));
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(formBuilder.build());
        if (headers != null) {
            headers.forEach((key, value) -> requestBuilder.header(key, String.valueOf(value)));
        }
        client.newCall(requestBuilder.build()).enqueue(callback);
    }

    /**
     * 异步postFrom
     *
     * @param url      请求url
     * @param params   请求参数
     * @param callback 异步回调函数
     */
    public static void asyncPostFrom(String url, Map<String, Object> params, Callback callback) {
        asyncPostFrom(url, params, null, callback);
    }

    /**
     * 同步
     *
     * @param request
     * @return
     */
    public static String execute(Request request) {
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            return body != null ? body.string() : null;
        } catch (IOException e) {
            log.error("http请求异常！" + request, e);
        }
        return null;
    }

    public static void main(String[] args) {
        asyncGet("http://mercury.proxy.dasouche.com/api/v1/member/getMemberInfo.json", null, null);
        System.out.println("同步获取: " + get("http://mercury.proxy.dasouche.com/api/v1/member/getMemberInfo.json", null, null));
    }
}
