package com.budworm.smallutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import com.budworm.smallutils.acc.ZFileUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * author zx
 * version 1.0
 * since 2017/1/3 14:40
 */
public class BitmapUtil {

    /**
     * 计算图片的缩放值
     * author zx
     * version 1.0
     * since 2017/1/3 14:40
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 压缩图片
     * author zx
     * version 1.0
     * since 2017/1/3 14:36
     */
    private void zoomBitmap(Bitmap bitMap) {
        // 图片允许最大空间 单位：KB
        double maxSize = 400.00;
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomBitmap(bitMap, bitMap.getWidth() / Math.sqrt(i), bitMap.getHeight() / Math.sqrt(i));
        }
    }


    /**
     * 压缩图片
     * author zx
     * version 1.0
     * since 2017/1/3 14:36
     */
    public static Bitmap zoomBitmap(Bitmap map, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = map.getWidth();
        float height = map.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(map, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }


    /**
     * 比例压缩图片
     * author zx
     * version 1.0
     * since 2017/4/12 14:01
     */
    public static Bitmap ratioBitmap(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 3;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        // 压缩好比例大小后再进行质量压缩
        // return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }


    /**
     * Bitmap转成本地文件
     * author zx
     * version 1.0
     * since 2017/1/3 14:34
     */
    public static boolean mapToFile(String path, Bitmap mBitmap) {
        FileOutputStream fOut = null;
        try {
            // Bitmap 尺寸压缩
            mBitmap = BitmapUtil.zoomBitmap(mBitmap, 480, 400);
            // 获得Path
            String SDPath = ZFileUtil.getInsideSDPath();
            // SD卡不存在
            if (SDPath == null || mBitmap == null) {
                return false;
            }
            //File f = new File(SDPath + bitName + ".png");
            //path = "/storage/emulated/0/1461910636918.jpg";
            File f = new File(path);
            if (!f.exists()) {
                f.createNewFile();
                fOut = new FileOutputStream(f);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, fOut);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
                // 释放Bitmap
                mBitmap.recycle();
            } catch (Exception e1) {
                e1.printStackTrace();
                return false;
            }
        }
    }


    /**
     * Bitmap转换成字符
     * author zx
     * version 1.0
     * since 2017/1/3 14:33
     */
    public static String mapToString(String filePath) {
        Bitmap bm = fileToMap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    /**
     * 本地文件转成Bitmap|压缩
     * author zx
     * version 1.0
     * since 2017/1/3 14:38
     */
    public static Bitmap fileToMap(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    /**
     * Drawable转成Bitmap|压缩
     * author zx
     * version 1.0
     * since 2017/1/3 14:38
     */
    public static Bitmap resToMap(Resources res, int drawable) {
        // 优化background防内存溢出
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        return BitmapFactory.decodeResource(res, drawable, opts);
    }


    /**
     * 回收Bitmap
     * author zx
     * version 1.0
     * since 2017/1/3 14:31
     */
    public static void recycleMap(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

}
