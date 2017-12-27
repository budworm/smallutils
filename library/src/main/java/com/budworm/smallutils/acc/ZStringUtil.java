package com.budworm.smallutils.acc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utils
 * author zx
 * version 1.0
 * since 2016/12/15 10:46
 */
public class ZStringUtil {

    private static String TAG = ZStringUtil.class.getSimpleName();


    /**
     * 判断字符是否为空
     * author zx
     * version 1.0
     * since 2017/5/26 14:43
     */
    public static boolean isEmpty(String str) {
        if (str != null && str.length() != 0) {
            return false;
        }
        return true;
    }


    /**
     * 利用正则表达式判断字符串是否是数字
     * author zx
     * version 1.0
     * since 2017/5/26 14:43
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * String大于0
     * author zx
     * version 1.0
     * since 2017/5/16 15:17
     */
    public static boolean isPositive(String param) {
        try {
            if (!ZStringUtil.isEmpty(param)) {
                if (ZStringUtil.isNumeric(param.trim())) {
                    if (Integer.parseInt(param) > 0) {
                        return true;
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

}
