package com.sunshine.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5Utils
 *
 * @author wangjn
 * @date 2019/4/19
 */
public class MD5Utils {

    /**
     * 获取MD5摘要字符串
     *
     * @param input
     * @return
     */
    public static String getMD5(String input) {
        try {
            // 创建消息摘要实例对象
            MessageDigest digest = MessageDigest.getInstance("md5");

            // 调用摘要算法，生成摘要结果，存放在byte数组里面
            byte[] bs = digest.digest(input.getBytes("utf-8"));

            // byte存放的值范围为-128-127，先转换为正数128-255、0-127：
            StringBuffer sb = new StringBuffer();
            for (byte b : bs) {
                int temp = b & 0xff;
                if (temp < 16 && temp >= 0) {
                    // 手动补上一个“0”
                    sb.append("0" + Integer.toHexString(temp));
                } else {
                    sb.append(Integer.toHexString(temp));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
