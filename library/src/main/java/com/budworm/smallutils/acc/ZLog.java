package com.budworm.smallutils.acc;

import android.util.Log;

/**
 * Created by zx on 2016/12/14.
 */
public class ZLog {

    // true-打印；
    private static boolean status = true;

    public static void setStatus(boolean status) {
        ZLog.status = status;
    }

    public static boolean isStatus() {
        return status;
    }

    public static int i(String tag, String msg) {
        if (status) {
            return Log.i(tag, msg);
        } else {
            return -1;
        }
    }


    public static int d(String tag, String msg) {
        if (status) {
            return Log.d(tag, msg);
        } else {
            return -1;
        }
    }


    public static int w(String tag, String msg) {
        if (status) {
            return Log.w(tag, msg);
        } else {
            return -1;
        }
    }


    public static int e(String tag, String msg) {
        if (status) {
            return Log.e(tag, msg);
        } else {
            return -1;
        }
    }


}
