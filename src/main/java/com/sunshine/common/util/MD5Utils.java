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
            byte[] bs = digest.digest(input.getBytes("utf-8"));

            StringBuffer sb = new StringBuffer();
            for (byte b : bs) {
                // 这里为何要 &xff 可以看我的博客 https://wangjunnan.github.io/2019/01/13/%E6%97%A0%E5%8F%82read%E8%BF%94%E5%9B%9Eint%E7%B1%BB%E5%9E%8B%E4%B8%BA%E4%BD%95%E8%A6%81%E4%B8%8E%E4%B8%8A0xff/
                int temp = b & 0xff;
                if (temp < 16) {
                    // 手动补上一个0
                    sb.append("0");
                }
                sb.append(Integer.toHexString(temp));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
