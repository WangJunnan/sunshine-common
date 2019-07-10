package com.walm.common.util;

import java.util.UUID;

/**
 * <p>UUIDUtils</p>
 *
 * @author wangjn
 * @date 2019/7/10
 */
public class UUIDUtils {

    private static char charDigits[] = {
            '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 生成10位UUID
     *
     * @return
     */
    public static String getID() {
        UUID uuid = UUID.randomUUID();

        // 改变uuid的生成规则
        return convertToHashStr(uuid.getMostSignificantBits(), 5)
                + convertToHashStr(uuid.getLeastSignificantBits(), 5);
    }

    public static String convertToHashStr(long hid, int len) {
        StringBuffer sb = new StringBuffer();

        for(int i=0; i<len; i++) {
            char c = charDigits[(int) ((hid&0xff) % charDigits.length)];
            sb.append(c);
            hid = hid >> 6;
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getID());
        System.out.println(getID());
        System.out.println(getID());
    }
}
