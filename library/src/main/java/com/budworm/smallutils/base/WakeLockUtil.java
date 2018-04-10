package com.budworm.smallutils.base;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

import com.budworm.smallutils.SystemUtil;


/**
 * 休眠锁
 * author zx
 * version 1.0
 * since 2017/7/25  .
 */
public class WakeLockUtil {
    private PowerManager.WakeLock wakeLock;// 休眠锁
    private PowerManager powerManager;
    private Context context;


    public static WakeLockUtil build(Context context) {
        return new WakeLockUtil(context, "MyWakeLock");
    }


    private WakeLockUtil(Context context, String Tag) {
        this.context = context;
        initWakeLock(Tag);
    }


    /**
     * 休眠锁
     * author zx
     * version 1.0
     * since 2017/1/17 15:25
     */
    private void initWakeLock(String tag) {
        powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, tag); // 6.0以后失效
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
        if (wakeLock != null && !wakeLock.isHeld()) { // 禁止休眠
            wakeLock.acquire();
        }
        addFlag(context);
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
        clearFlag(context);
    }


    /**
     * 屏幕常亮
     * author zx
     * version 1.0
     * since 2017/1/17 17:52
     */
    public void addFlag(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    /**
     * 屏幕自动
     * author zx
     * version 1.0
     * since 2017/1/17 17:52
     */
    public void clearFlag(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


}
