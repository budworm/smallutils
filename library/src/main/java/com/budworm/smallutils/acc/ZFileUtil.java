package com.budworm.smallutils.acc;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文件工具
 * author zx
 * version 1.0
 * since 2016/12/15 10:46
 */
public class ZFileUtil {

    private static String TAG = ZFileUtil.class.getSimpleName();
    private static ArrayList<File> videoList = new ArrayList<File>();
    private ArrayList<File> scanList;

    public ZFileUtil() {
        if (scanList == null) {
            scanList = new ArrayList<File>();
        }
    }


    /**
     * 文件存在
     *
     * @return boolean
     * @author zx
     */
    public static boolean fileExists(String path) {
        try {
            if (path == null || "".equals(path)) {
                return false;
            }
            File f = new File(path);
            long fileSize = f.length();
            if (!f.exists() || fileSize == 0) {
                return false;
            }
            //Log.d(TAG, "fileExists: " + f.getName());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 文件存在
     *
     * @return boolean
     * @author zx
     */
    public static boolean fileExists(File f) {
        try {
            if (f == null) {
                return false;
            }
            long fileSize = f.length();
            if (!f.exists() || fileSize == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 创建文件夹
     *
     * @return boolean
     * @author zx
     */
    public static boolean makeFileDirs(String folderName) {
        try {
            if (folderName == null || folderName.isEmpty()) {
                return false;
            }
            File folder = new File(folderName);
            Boolean or = (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
            return or;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 创建文件夹
     *
     * @return boolean
     * @author zx
     */
    public static File makeFileDirs(File folder) {
        try {
            Boolean or = (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
            return folder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 创建文件
     *
     * @return boolean
     * @author zx
     */
    public static File createFile(String path) {
        try {
            File file = new File(path);
            File Dir = file.getParentFile();

            Boolean dirRes = (Dir.exists() && Dir.isDirectory()) ? true : Dir.mkdirs();
            if (dirRes) {
                file = new File(path);
                Boolean fileRes = (file.exists() && file.isFile()) ? true : file.createNewFile();
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     * 清除扫描数据|配合递归调用使用
     * author zx
     * version 1.0
     * since 2017/1/5 14:46
     */
    public void cleanScanList() {
        if (scanList != null) {
            scanList.clear();
        }
    }


    /**
     * 获取SD卡根路径
     *
     * @return String
     * @author zx
     */
    public static String getInsideSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }


    /**
     * 获取SD卡路径
     * false 为内置
     * true 为外置
     *
     * @param mContext
     * @param isRemovable
     * @return
     */
    public static String getStorageSDPath(Context mContext, boolean isRemovable) {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method _isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) _isRemovable.invoke(storageVolumeElement);
                if (isRemovable == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件重命名
     * author zx
     * version 1.0
     * since 2017/3/14 14:25
     */
    public static File renameFile(File oldfile, String newname) {
        try {
            if (oldfile.exists()) {
                if (!oldfile.getName().equals(newname)) {
                    String newpath = oldfile.getParentFile() + File.separator + newname;
                    File newfile = new File(newpath);
                    if (!newfile.exists()) {
                        if (oldfile.renameTo(newfile)) {
                            Log.d(TAG, "renameFile: success");
                            return newfile;
                        } else {
                            Log.d(TAG, "renameFile: error");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件重命名
     * author zx
     * version 1.0
     * since 2017/3/14 14:25
     */
    public static File renameFile(File oldfile, File newfile) {
        try {
            if (oldfile.exists()) {
                if (!newfile.exists()) {
                    if (oldfile.renameTo(newfile)) {
                        if (oldfile.exists()) {
                            Log.d(TAG, "renameFile: oldfile exists !");
                            oldfile.delete();
                        } else {
                            Log.d(TAG, "renameFile: oldfile not exists !");
                        }
                        return newfile;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取文件最后修改时间
     * author zx
     * version 1.0
     * since 2017/3/14 14:25
     */
    public static long getFileLastmodified(String path) {
        try {
            if (null == path || "".equals(path)) {
                return 0;
            }
            File file = new File(path);
            if (file.exists()) {
                return file.lastModified();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * URI转path
     * author zx
     * version 1.0
     * since 2016/12/29 17:34
     */
    public static String uriToPath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /**
     * 遍历视频文件|非递归
     * author zx
     * version 1.0
     * since 2017/1/5 14:45
     */
    public static ArrayList<File> traverseVideoFile(File folder) {
        long start = System.currentTimeMillis(); //+++++++++++++++++++++++++++++++++++

        ArrayList<File> resList = new ArrayList<File>();
        int fileNum = 0, folderNum = 0;
        if (null != folder && folder.exists()) {
            LinkedList<File> dirList = new LinkedList<File>();
            File[] files = folder.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        //System.out.println("文件夹:" + file.getAbsolutePath());
                        dirList.add(file);
                        folderNum++;
                    } else {
                        //System.out.println("文件:" + file.getAbsolutePath());
                        fileNum++;
                        if (isVideo(file)) {
                            resList.add(file);
                        }
                    }
                }
                File temp_file;
                while (!dirList.isEmpty()) {
                    temp_file = dirList.removeFirst();
                    files = temp_file.listFiles();
                    for (File file : files) {
                        if (file.isDirectory()) {
                            //System.out.println("文件夹:" + file.getAbsolutePath());
                            dirList.add(file);
                            folderNum++;
                        } else {
                            //System.out.println("文件:" + file.getAbsolutePath());
                            fileNum++;
                            if (isVideo(file)) {
                                resList.add(file);
                            }
                        }
                    }
                }
            }
        } else {
            ZLog.e(ZText.logTip, "扫描磁盘指定文件夹不存在!");
        }

        //ZLog.d(TAG, folder.getName() + "-time...........................△T:" + (System.currentTimeMillis() - start));//+++++++++++++++++++++++++++++++++++
        return resList;
    }


    /**
     * 遍历文件夹|非递归
     * author zx
     * version 1.0
     * since 2017/1/5 14:45
     */
    public void traverseSimpleFolder(String path) {
        int fileNum = 0, folderNum = 0;
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    System.out.println("文件夹:" + file2.getAbsolutePath());
                    list.add(file2);
                    fileNum++;
                } else {
                    System.out.println("文件:" + file2.getAbsolutePath());
                    folderNum++;
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        list.add(file2);
                        fileNum++;
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        folderNum++;
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);

    }


    /**
     * 遍历文件夹下视频文件
     *
     * @param
     */
    public ArrayList<File> scanVideoFile(File folder) {
        try {
            if (scanList == null) {
                scanList = new ArrayList<File>();
            }
            scanList.clear();
            // 当前目录下的所有文件
            final String[] filenames = folder.list();
            // 当前目录的名称
            // final String folderName = folder.getName();
            // 当前目录的绝对路径
            // final String folderPath = folder.getAbsolutePath();
            if (filenames != null) {
                // 遍历当前目录下的所有文件
                for (String name : filenames) {
                    File file = new File(folder, name);
                    // 如果是文件夹则继续递归当前方法
                    if (file.isDirectory()) {
                        scanVideoFile(file);
                    }
                    // 如果是文件则对文件进行相关操作
                    else {
                        String[] str = name.split("\\.");
                        int leng = str.length;
                        if (leng > 1) {
                            // 文件后缀
                            String fileSuffix = str[leng - 1];
                            // 文件名称
                            //String fileSign = str[leng - 2];
                            // 文件路径
                            //String filePath = file.getAbsolutePath();
                            if (ZFileUtil.isVideo(fileSuffix)) {
                                scanList.add(file);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scanList;
    }


    /**
     * 遍历指定文件夹下的资源文件
     * author zx
     * version 1.0
     * since 2016/12/29 17:39
     */
    public static void scanSimpleFile(File folder) {
        Log.e(TAG, "init simpleScanning......................");
        // 指定正则表达式
        Pattern mPattern = Pattern.compile("([^//.]*)//.([^//.]*)");
        Pattern mPattern1 = Pattern.compile("\\w*\\.(mp4|rmvb|flv|mpeg|avi)");
        // 当前目录下的所有文件
        final String[] filenames = folder.list();
        // 当前目录的名称
        // final String folderName = folder.getName();
        // 当前目录的绝对路径
        // final String folderPath = folder.getAbsolutePath();
        if (filenames != null) {
            // 遍历当前目录下的所有文件
            for (String name : filenames) {
                File file = new File(folder, name);
                // 如果是文件夹则继续递归当前方法
                if (file.isDirectory()) {
                    scanSimpleFile(file);
                }
                // 如果是文件则对文件进行相关操作
                else {
                    //Matcher matcher = mPattern.matcher(name);
                    Matcher matcher = mPattern1.matcher(name);
                    if (matcher.matches()) {
                        // 文件名称
                        String fileName = matcher.group(1);
                        // 文件后缀
                        String fileExtension = matcher.group(2);
                        // 文件路径
                        String filePath = file.getAbsolutePath();

                        if (ZFileUtil.isMusic(fileExtension)) {
                            // 初始化音乐文件......................
                            System.out.println("This file is Music File,fileName="
                                    + fileName
                                    + "."
                                    + fileExtension
                                    + ",filePath=" + filePath);

                        }

                        if (ZFileUtil.isPhoto(fileExtension)) {
                            // 初始化图片文件......................
                            System.out
                                    .println("This file is Photo File,fileName="
                                            + fileName
                                            + "."
                                            + fileExtension
                                            + ",filePath=" + filePath);
                        }

                        if (ZFileUtil.isVideo(fileExtension)) {
                            // 初始化视频文件......................
                            Log.e(TAG, "初始化视频文件......................");

                            System.err.println("This file is Video File,fileName="
                                    + fileName
                                    + "."
                                    + fileExtension
                                    + ",filePath=" + filePath);
                        }
                    }
                }
            }
        }
    }


    /**
     * 判断是否是音乐文件
     *
     * @param
     * @return
     */
    public static boolean isMusic(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        if (ext.equals("mp3") || ext.equals("m4a") || ext.equals("wav")
                || ext.equals("amr") || ext.equals("awb") || ext.equals("aac")
                || ext.equals("flac") || ext.equals("mid")
                || ext.equals("midi") || ext.equals("xmf")
                || ext.equals("rtttl") || ext.equals("rtx")
                || ext.equals("ota") || ext.equals("wma") || ext.equals("ra")
                || ext.equals("mka") || ext.equals("m3u") || ext.equals("pls")) {
            return true;
        }
        return false;
    }


    /**
     * 判断后缀是否是视频文件
     * author zx
     * version 1.0
     * since 2017/1/5 15:02
     */
    public static boolean isVideo(File file) {
        if (file != null && file.exists()) {
            String[] str = file.getName().split("\\.");
            if (str.length > 1) {
                String fileSuffix = str[str.length - 1];
                return isVideo(fileSuffix);
            }
        }
        return false;
    }


    /**
     * 判断后缀是否是视频文件
     * author zx
     * version 1.0
     * since 2017/1/5 15:02
     */
    public static boolean isVideo(String extension) {
        if (extension == null)
            return false;
        final String ext = extension.toLowerCase();
        if (ext.endsWith("mpeg") || ext.endsWith("mp4") || ext.endsWith("mov")
                || ext.endsWith("m4v") || ext.endsWith("3gp")
                || ext.endsWith("3gpp") || ext.endsWith("3g2")
                || ext.endsWith("3gpp2") || ext.endsWith("avi")
                || ext.endsWith("divx") || ext.endsWith("wmv")
                || ext.endsWith("asf") || ext.endsWith("flv")
                || ext.endsWith("mkv") || ext.endsWith("mpg")
                || ext.endsWith("rmvb") || ext.endsWith("rm")
                || ext.endsWith("vob") || ext.endsWith("f4v")) {
            return true;
        }
        return false;
    }


    /**
     * 判断是否是图像文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isPhoto(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        if (ext.endsWith("jpg") || ext.endsWith("jpeg") || ext.endsWith("gif")
                || ext.endsWith("png") || ext.endsWith("bmp")
                || ext.endsWith("wbmp")) {
            return true;
        }
        return false;
    }


    /**
     * 枚举所有挂载点
     * author zx
     * version 1.0
     * since 2017/3/17 17:41
     */
    public static String[] getVolumePaths(Context context) {
        String[] paths = null;
        StorageManager mStorageManager;
        Method mMethodGetPaths = null;
        try {
            mStorageManager = (StorageManager) context
                    .getSystemService(context.STORAGE_SERVICE);
            mMethodGetPaths = mStorageManager.getClass().getMethod(
                    "getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paths;
    }


    /**
     * 获取文件的byte[]
     *
     * @param file
     * @return
     */
    public static byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            return null;
        }
        return buffer;
    }

    /**
     * 删除文件/文件夹下
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File temp : fileList) {
                    boolean isSuccess = deleteFile(temp);
                    if (!isSuccess) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * 获取文件/文件夹的大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file == null || !file.exists()) {
            return size;
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File temp : fileList) {
                    size += getFileSize(temp);
                }
            }
        } else {
            size += file.length();
        }
        return size;
    }


    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 根据Uri获取路径
     *
     * @param context
     * @param data
     * @return
     */
    public static String getPathByUri(Context context, Uri data){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getPathByUri4BeforeKitkat(context, data);
        }else {
            return getPathByUri4AfterKitkat(context, data);
        }
    }

    //4.4以前通过Uri获取路径：data是Uri，filename是一个String的字符串，用来保存路径
    private static String getPathByUri4BeforeKitkat(Context context, Uri data) {
        String filename=null;
        if (data.getScheme().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(data, new String[] { "_data" }, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                filename = cursor.getString(0);
            }
        } else if (data.getScheme().compareTo("file") == 0) {// file:///开头的uri
            filename = data.toString().replace("file://", "");// 替换file://
            if (!filename.startsWith("/mnt")) {// 加上"/mnt"头
                filename += "/mnt";
            }
        }
        return filename;
    }

    //4.4以后根据Uri获取路径：
    @SuppressLint("NewApi")
    private static String getPathByUri4AfterKitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
