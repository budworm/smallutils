package com.budworm.smallutils.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;


/**
 * Media Scan Utils
 * author zx
 * version 1.0
 * since 2016/12/15 10:46
 */
public class FileBroadcastUtil {

    private static String TAG = FileBroadcastUtil.class.getSimpleName();
    private static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";


    /**
     * scanFileAsync
     * author zx
     * version 1.0
     * since 2017/6/15 14:00
     */
    public static void scanFileAsync(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(intent);
    }


    /**
     * scanFileAsync
     * author zx
     * version 1.0
     * since 2017/6/15 14:00
     */
    public static void scanFileAsync(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }


    /**
     * scanDirAsync
     * author zx
     * version 1.0
     * since 2017/6/15 14:00
     */
    public static void scanDirAsync(Context context, String dir) {
        Intent intent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        intent.setData(Uri.fromFile(new File(dir)));
        context.sendBroadcast(intent);
    }


    /**
     * scanDirAsync
     * author zx
     * version 1.0
     * since 2017/6/15 14:00
     */
    public static void scanDirAsync(Context context, File file) {
        Intent intent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }


}
