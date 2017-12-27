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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池单列
 * author zx
 * version 1.0
 * since 2017/7/26.
 */
public class ExecutorUtil {
    private static ExecutorUtil executorUtil = null;
    private static ExecutorService threadPool = null;

    private ExecutorUtil() {
    }


    public static synchronized ExecutorUtil getInstance() {
        if (executorUtil == null) {
            executorUtil = new ExecutorUtil();
        }
        return executorUtil;
    }


    /**
     * 创建线程池
     *
     * @return ExecutorService
     * @author zx
     */
    public ExecutorService getThreadPool() {
        if (threadPool == null) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }


    /**
     * 取消线程池
     *
     * @return void
     * @author zx
     */
    public void shutdownThreadPool() {
        if (threadPool != null) {
            threadPool.shutdownNow();
            threadPool = null;
        }
    }


}
