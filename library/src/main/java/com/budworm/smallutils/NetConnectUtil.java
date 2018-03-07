package com.budworm.smallutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.text.format.Formatter;
import android.util.Log;

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


    /**
     * 流量监控
     * author zx
     * version 1.0
     * since 2018/1/12  .
     */
    public void getAppTraffic(Context context, Object tag) {
        Log.e("getTraffic", tag + ":监控开始**************↓↓↓");
        try {
            //获取系统应用包管理
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            if (info != null && info.applicationInfo.uid != 0) {
                long rxBytes = TrafficStats.getUidRxBytes(info.applicationInfo.uid);//获取某个网络UID的接受字节数
                long txBytes = TrafficStats.getUidTxBytes(info.applicationInfo.uid);//获取某个网络UID的发送字节数
                Log.e("getTraffic", tag + ":接受消耗的流量--:" + Formatter.formatFileSize(context, rxBytes));
                Log.e("getTraffic", tag + ":发送消耗的流量--:" + Formatter.formatFileSize(context, txBytes));
            } else {
                Log.e("getTraffic", tag + ":消耗的流量--:0");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getTraffic", tag + ":异常--");
            e.printStackTrace();
        }
    }

}
