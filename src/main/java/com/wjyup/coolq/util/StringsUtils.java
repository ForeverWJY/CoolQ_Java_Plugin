package com.wjyup.coolq.util;

/**
 * 字符串处理工具类
 * Created by WJY on 2017/8/2.
 */
public class StringsUtils {
    /**
     * 首字母大写
     *
     * @param string
     * @return
     */
    public static String toUpperCase4Index(String string) {
        char[] methodName = string.toCharArray();
        methodName[0] = toUpperCase(methodName[0]);
        return String.valueOf(methodName);
    }

    /**
     * 首字母小写
     *
     * @param string
     * @return
     */
    public static String toLowerCase4Index(String string) {
        char[] methodName = string.toCharArray();
        methodName[0] = toLowerCase(methodName[0]);
        return String.valueOf(methodName);
    }

    /**
     * 字符转成大写
     *
     * @param chars
     * @return
     */
    private static char toUpperCase(char chars) {
        if (97 <= chars && chars <= 122) {
            chars ^= 32;
        }
        return chars;
    }

    /**
     * 字符转成小写
     *
     * @param chars
     * @return
     */
    private static char toLowerCase(char chars) {
        if (65 <= chars && chars <= 90) {
            chars |= 32;
        }
        return chars;
    }
}
