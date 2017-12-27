package com.budworm.smallutils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * author zx
 * version 1.0
 * since 2017/7/25  .
 */
public class SysCrashUtil implements Thread.UncaughtExceptionHandler {

    public static final String TAG = SysCrashUtil.class.getSimpleName();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static SysCrashUtil INSTANCE = new SysCrashUtil();
    // Context对象
    private Context context;
    private File folder;
    // 是否记录过日志
    private static Boolean orWriteLog = false;
    // 是否处理过异常
    private static boolean orHandleException = false;

    private SysCrashUtil() {
    }


    /**
     * 单列获取对象
     * author zx
     * version 1.0
     * since 2016/12/29 14:09
     */
    public synchronized static SysCrashUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SysCrashUtil();
        }
        return INSTANCE;
    }


    /**
     * author zx
     * version 1.0
     * since 2016/12/29 14:47
     */
    public void init(Context context, File folder) {
        this.context = context;
        this.folder = folder;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    /**
     * UncaughtException发生时会转入该函数来处理
     * author zx
     * version 1.0
     * since 2016/12/29 14:11
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d(TAG, "Sys crash exception ." + formatter.format(new Date()));
        ex.printStackTrace();
        if (!orWriteLog) {
            orWriteLog = true;
            orHandleException = handleException(ex);
            if (!orHandleException && mDefaultHandler != null) {
                //如果用户没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    Log.e(TAG, "error : ", e);
                }
                //退出程序
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }
    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                //Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                //收集设备参数信息
                collectDeviceInfo(context);
                //保存日志文件
                saveCrashLogFile(ex);
                Looper.loop();
            }
        }.start();
        return true;
    }


    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }


    /**
     * 保存日志
     * author zx
     * version 1.0
     * since 2016/12/29 14:25
     */
    private String saveCrashLogFile(Throwable ex) {
        try {
            if (folder == null || !folder.exists() || !folder.isDirectory()) {
                return null;
            }
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }
            sb.append("\n" + "Error:" + "\n");

            // 记录异常Log——1
            String msgError = ex.getMessage();
            sb.append(msgError);
            sb.append("\n");
            // 读出异常
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            Throwable cause = ex.getCause();
            if (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            sb.append(writer.toString());
            printWriter.close();
            writer.close();
            // 创建目录
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return null;
            }
            // 创建日志文件
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String log = folder.getAbsolutePath() + File.separator + "wzplay-crash-" + time + "-" + timestamp + ".txt";
            File logFile = new File(log);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            // 保存文件
            //FileUtils.writeStringToFile(logFile, sb.toString());
            return log;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            return null;
        }
    }

}
