/*   
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 * 
 * 2016年5月10日
 */
/**
 *
 */
package com.budworm.smallutils;

/**
 * Class
 * author zx
 * version 1.0
 * since 2017/7/26.
 */
public class ClassUtil {

    /**
     * 创建对象
     * author zx
     * version 1.0
     * since 2018/1/18  .
     */
    public static <T> T create(Class<T> tClass) {
        T instance = null;
        try {
            instance = tClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }


}
