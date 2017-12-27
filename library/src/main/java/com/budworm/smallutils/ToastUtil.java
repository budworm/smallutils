package com.budworm.smallutils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具类
 * @date 2017/5/19
 */
public class ToastUtil {

    private static Toast toast = null;

    public static void showShort(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
        }
        toast.setText(resId);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showShort(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLong(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
        }
        toast.setText(resId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showLong(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
        }
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
