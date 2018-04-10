package com.budworm.smallutils.base;

import android.content.Context;
import android.os.PowerManager;

import com.budworm.smallutils.SystemUtil;


/**
 * 休眠锁
 * author zx
 * version 1.0
 * since 2017/7/25  .
 */
public class WakeLockUtil {
    protected PowerManager.WakeLock wakeLock;// 休眠锁
    protected PowerManager powerManager;
    protected Context context;


    public WakeLockUtil(Context context) {
        this.context = context;
        initWakeLock("My Lock");
    }


    /**
     * 休眠锁
     * author zx
     * version 1.0
     * since 2017/1/17 15:25
     */
    protected void initWakeLock(String tag) {
        powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, tag);
        // 非计数模式
        wakeLock.setReferenceCounted(false);
        // 如果亮度是自动,关闭自动调整
        int mode = SystemUtil.getScreenMode(context);
        if (mode == 1) {
        }
    }


    /**
     * 禁止休眠
     * author zx
     * version 1.0
     * since 2017/1/17 17:52
     */
    public void acquireWakeLock() {
        if (wakeLock == null) {
            initWakeLock("My Lock");
        }
        wakeLock.acquire(); // 禁止休眠
    }


    /**
     * 允许休眠
     * author zx
     * version 1.0
     * since 2017/1/17 17:52
     */
    public void releaseWakeLock() {
        if (wakeLock != null) { // 允许休眠
            wakeLock.release();
        }
    }


}
