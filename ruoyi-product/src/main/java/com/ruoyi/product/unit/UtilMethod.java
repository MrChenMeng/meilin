package com.ruoyi.product.unit;

/**
 * @author chenm
 * @create 2019-08-07 13:07
 */
public class UtilMethod {

    /*
     * 把100002首位的1去掉的实现方法：
     * @param str
     * @param start
     * @return
     */
    public static String subStr(String str, int start) {
        if (str == null || str.equals("") || str.length() == 0)
            return "";
        if (start < str.length()) {
            return str.substring(start);
        } else {
            return "";
        }

    }
}
