package com.budworm.smallutils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.Locale;

/**
 * author zx
 * version 1.0
 * since 2018/1/4  .
 */
public class SystemUtil {

    private String Tag = SystemUtil.class.getSimpleName();


    /**
     * 杀掉应用
     * author zx
     * version 1.0
     * since 2017/4/6 13:57
     */
    public static void exitApp(Activity activity) {
        System.exit(0);
    }


    /**
     * 启动应用的设置
     * author zx
     * version 1.0
     * since 2018/1/4  .
     */
    public static void startAppSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }


    /**
     * 启动应用的设置详情
     * author zx
     * version 1.0
     * since 2018/1/4  .
     */
    public static void startAppSettingDetails(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }


    /**
     * 判断语言
     * author zx
     * version 1.0
     * since 2017/6/12 16:46
     */
    public static boolean isChinese(Context context) {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            String country = locale.getCountry().toLowerCase();
            String strLocale = locale.toString();
            if (language.equalsIgnoreCase("zh")) { //大中华语言
                if (country.trim().equalsIgnoreCase("CN") || strLocale.indexOf("#Hans") != -1) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 判断语言
     * author zx
     * version 1.0
     * since 2017/6/12 16:20
     */
    public static boolean isAdaptCountries(Context context) {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            String country = locale.getCountry().toLowerCase();
            String strLocale = locale.toString();

            Boolean res = language.equalsIgnoreCase("ru") ||
                    language.equalsIgnoreCase("es") ||
                    language.equalsIgnoreCase("fr") ||
                    language.equalsIgnoreCase("de") ||
                    language.equalsIgnoreCase("cs");
            if (res) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取Sys缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static long getSysCacheSize(Context context) {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return cacheSize;
    }


    /**
     * 清除Sys缓存
     *
     * @param context
     */
    public static void clearSysCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }


    /**
     * 删除文件夹
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }


    /**
     * 获取文件大小
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     * author zx
     * version 1.0
     * since 2016/12/29 17:44
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 隐藏系统栏即全屏
     * author zx
     * version 1.0
     * since 2016/12/29 17:53
     */
    public static void hideSystemUI(Activity activity) {
        if (null != activity && activity instanceof Activity) {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }


    /**
     * 隐藏系统栏即全屏
     * author zx
     * version 1.0
     * since 2016/12/29 17:53
     */
    public static void hideSystemUI(View view) {
        if (null != view) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            view.setSystemUiVisibility(uiFlags);
        }
    }


    /**
     * 隐藏导航栏
     * author zx
     * version 1.0
     * since 2016/12/29 17:54
     */
    public static void hideNavigationBar(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }


    /**
     * 显示系统栏
     * author zx
     * version 1.0
     * since 2016/12/29 17:53
     */
    public static void showSystemUI(View view) {
        if (null != view) {
            view.setSystemUiVisibility(0);
        }
    }


    /**
     * 硬件屏宽
     * author zx
     * version 1.0
     * since 2016/12/29 17:54
     */
    public static long getScreenWidth(Activity activity) {
        long width = 0;
        //
        if (null != activity && activity instanceof Activity) {
            WindowManager wm = activity.getWindowManager();
            //width = wm.getDefaultDisplay().getWidth();
            // 包含状态栏
            DisplayMetrics metrics = new DisplayMetrics();// 包含状态栏
            wm.getDefaultDisplay().getRealMetrics(metrics);
            width = metrics.widthPixels;
        }
        return width;
    }


    /**
     * 硬件屏高
     * author zx
     * version 1.0
     * since 2016/12/29 17:54
     */
    public static long getScreenHeight(Activity activity) {
        long height = 0;
        if (null != activity && activity instanceof Activity) {
            WindowManager wm = activity.getWindowManager();
            //height = wm.getDefaultDisplay().getHeight();
            DisplayMetrics metrics = new DisplayMetrics();// 包含状态栏
            wm.getDefaultDisplay().getRealMetrics(metrics);
            height = metrics.heightPixels;
        }
        return height;
    }


    /**
     * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     * author zx
     * version 1.0
     * since 2017/1/17 15:27
     */
    public static int getScreenMode(Context context) {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }


    /**
     * 获取系统亮度
     * author zx
     * version 1.0
     * since 2017/5/12 10:09
     */
    public static int getScreenBrightness(Activity activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {

        }
        return value;
    }


    /**
     * 得到屏幕旋转的状态
     *
     * @param context
     * @return
     */
    public static int getRotationStatus(Context context) {
        int status = 0;
        try {
            status = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * 判断屏幕方向【横True|竖False】
     *
     * @return void
     * @author zx
     */
    public static boolean isLandscape(Context context) {
        try {
            // 竖1|横!=1
            if (context.getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 判断屏幕方向
     *
     * @return void
     * @author zx
     */
    public static boolean isPortrait(Context context) {
        try {
            if (context.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 广播暂停音乐播放器|4.4以下
     * author zx
     * version 1.0
     * since 2017/1/17 15:32
     */
    public static void pauseMusic(Context context) {
        if (null != context) {
            Intent freshIntent = new Intent();
            freshIntent.setAction("com.android.music.musicservicecommand.pause");
            //freshIntent.setAction("com.android.music.musicservicecommand");
            freshIntent.putExtra("command", "pause");
            context.sendBroadcast(freshIntent);
        }
    }


    /**
     * 更新文件到媒体库
     * author zx
     * version 1.0
     * since 2017/3/14 17:25
     */
    public static void senderFile(Context context, File file) {
        final Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
        Log.e("", "senderFile: end !");

    }


    /**
     * 读取系统磁盘剩余空间
     * author zx
     * version 1.0
     * since 2017/4/12 13:45
     */
    public static long readSystem() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        //Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
        //Log.d("", "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
        //Log.d("", "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize + "B");
        long size = availCount * blockSize / 1024;
        return size;
    }


    /**
     * 根据路径获取内存状态
     * author zx
     * version 1.0
     * since 2017/4/12 13:57
     */
    public static long getMemoryInfo(Context context, File path) {
        long blockSize = 0;   // 获得一个扇区的大小
        long availableBlocks = 0;   // 获得可用的扇区数量
        try {
            // 获得一个磁盘状态对象
            StatFs stat = new StatFs(path.getPath());
            blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();    // 获得扇区的总数
            availableBlocks = stat.getAvailableBlocks();
            // 总空间
            String totalMemory = Formatter.formatFileSize(context, totalBlocks * blockSize);
            // 可用空间
            String availableMemory = Formatter.formatFileSize(context, availableBlocks * blockSize);
            //return "总空间: " + totalMemory + "\n可用空间: " + availableMemory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return availableBlocks * blockSize;
    }


    /**
     * 关闭软键盘
     * author zx
     * version 1.0
     * since 2017/4/12 13:57
     */
    public static void hideInputWin(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


}
