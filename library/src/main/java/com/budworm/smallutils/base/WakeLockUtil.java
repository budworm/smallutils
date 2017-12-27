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
        initWakeLock();
    }


    /**
     * 休眠锁
     * author zx
     * version 1.0
     * since 2017/1/17 15:25
     */
    protected void initWakeLock() {
        powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
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
        if (wakeLock != null && !wakeLock.isHeld()) { // 禁止休眠
            wakeLock.acquire();
        }
    }


    /**
     * 允许休眠
     * author zx
     * version 1.0
     * since 2017/1/17 17:52
     */
    public void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) { // 允许休眠
            wakeLock.release();
        }
    }


}
