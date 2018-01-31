package com.budworm.smallutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * author zx
 * version 1.0
 * since 2018/1/4  .
 */
public class NetConnectUtil {


    /**
     * 检查网络连接
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetConnect(Context context) {
        boolean result = false;
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                result = mNetworkInfo.isAvailable();
            }
        }
        return result;
    }


    /**
     * 连接WIFI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    private Boolean isNetWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }


}
