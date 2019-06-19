package com.walm.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.StringJoiner;

/**
 * <p>HttpURLConnectionUtils</p>
 * <p>
 * <p>HttpURLConnection 是JAVA net 包下的网络请求工具类，使用配置都比较简单，并且支持系统层面的连接池概念</p>
 *
 * @author wangjn
 * @date 2019/6/19
 */
@Slf4j
public class HttpURLConnectionUtils {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CHARSET = "UTF-8";

    /**
     * get
     *
     * @param url
     * @param params
     * @return
     */
    public static String get(String url, Map<String, Object> params) {
        HttpURLConnection conn = null;
        try {
            conn = getConn(getCompleteUrl(url, params));
            conn.setRequestMethod("GET");
            return execute(conn);
        } catch (IOException e) {
            log.error("error_HttpURLConnectionUtils_get, url = {}", url, e);
        } finally {
            conn.disconnect();
        }
        return null;
    }

    /**
     * post 方式提交表单数据
     *
     * @param url
     * @param params
     * @return
     */
    public static String postForm(String url, Map<String, Object> params) {
        HttpURLConnection conn = null;
        try {
            conn = getConn(url);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());

            // 拼接 body key value 表单请求体
            if (params != null) {
                StringJoiner stringJoiner = new StringJoiner("&");
                params.forEach((k, v) -> stringJoiner.add(k + "=" + v));
                String paramStr = stringJoiner.toString();
                outputStream.writeBytes(paramStr);
            }
            return execute(conn);
        } catch (IOException e) {
            log.error("error_HttpURLConnectionUtils_postForm, url = {}", url, e);
        }
        return null;
    }

    /**
     * post 方式提交 json数据
     *
     * @param url
     * @param params
     * @return
     */
    public static String postJson(String url, Map<String, Object> params) {
        HttpURLConnection conn = null;
        try {
            conn = getConn(url);
            // 设置请求方法为post
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            if (params != null) {
                DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
                outputStream.writeBytes(JsonUtils.toJson(params));
                outputStream.flush();
                outputStream.close();
            }
            return execute(conn);
        } catch (IOException e) {
            log.error("error_HttpURLConnectionUtils_postJson, url = {}", url, e);
        } finally {
            conn.disconnect();
        }
        return null;
    }

    /**
     * 获取 Http 连接信息
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static HttpURLConnection getConn(String url) throws IOException {
        HttpURLConnection conn = null;
        URL postUrl = new URL(url);

        conn = (HttpURLConnection) postUrl.openConnection();

        // 设置读取超时时间为5秒
        conn.setReadTimeout(5000);
        // 设置连接超时时间为10秒
        conn.setConnectTimeout(10000);
        // 设置允许输入
        conn.setDoInput(true);
        // 设置允许输出
        conn.setDoOutput(true);
        // 设置不用缓存
        conn.setUseCaches(false);
        // 设置请求头
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", CHARSET);
        conn.setRequestProperty("User-Agent", USER_AGENT);
        return conn;
    }

    private static String execute(HttpURLConnection conn) throws IOException {
        if (conn == null) {
            log.error("conn is null");
            return null;
        }
        conn.connect();
        if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
            InputStream in = conn.getInputStream();
            StringBuilder response = new StringBuilder();
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = in.read(buff)) != -1) {
                response.append(new String(buff, 0, length, CHARSET));
            }
            return response.toString();
        }
        return null;
    }

    /**
     * 获取完整链接
     *
     * @param url
     * @param params 请求参数
     * @return
     */
    private static String getCompleteUrl(String url, Map<String, Object> params) {
        if (params == null) {
            return url;
        }
        StringJoiner stringJoiner = new StringJoiner("&");
        params.forEach((k, v) -> stringJoiner.add(k + "=" + v));
        String paramUrl = stringJoiner.toString();
        return url + "?" + paramUrl;
    }
}
