/*   
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 * 
 * 2015年12月28日
 */
/**
 *
 */
package com.budworm.smallutils.base;


/**
 * Collections Utils
 * author zx
 * version 1.0
 * since 2017/4/10 10:57
 */
public class CollectionsUtil {

    /**
     * char转int的方法 注意：这个是按"GBK"来排序的，千万不要默认或者写"utf-8"，那样排序不准。
     * author zx
     * version 1.0
     * since 2017/4/10 15:57
     */
    public static int getGBCode(char c) {
        byte[] bytes = null;
        try {
            bytes = new StringBuffer().append(c).toString().getBytes("gbk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bytes.length == 1) {
            return bytes[0];
        }
        int a = bytes[0] - 0xA0 + 256;
        int b = bytes[1] - 0xA0 + 256;

        return a * 100 + b;
    }


    /**
     * 取出汉字的编码
     * author zx
     * version 1.0
     * since 2017/4/10 15:57
     */
    private int gbValue(char ch) {
        String str = new String();
        str += ch;
        try {
            byte[] bytes = str.getBytes("GBK");
            if (bytes.length < 2)
                return 0;
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }


}
