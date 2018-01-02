/*   
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * Founder. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Founder.   
 * 
 * 2016年5月10日
 */
/**
 *
 */
package com.budworm.smallutils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;


/**
 * author zx
 * version 1.0
 * since 2017/7/26  .
 */
public class RetrieverUtils {
    private static ContentResolver contentResolver;

    /**
     * 判断视频是否能播放
     *
     * @return boolean
     * @author zx
     */
    public static boolean videoIsPlay(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            File file = new File(path);
            if (file.exists()) {
                retriever.setDataSource(file.getAbsolutePath());
                String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int dur = Integer.parseInt(duration);
                if (dur != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
                retriever = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 获取视频时长
     *
     * @return boolean
     * @author zx
     */
    public static int getDuration(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            File file = new File(path);
            if (file.exists()) {
                retriever.setDataSource(file.getAbsolutePath());
                String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int dur = Integer.parseInt(duration);
                if (dur != 0) {
                    return dur;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
                retriever = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    /**
     * 视频文件路径获取缩略图
     *
     * @return Bitmap
     * @author zx
     */
    public static Bitmap getThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                retriever.setDataSource(filePath);
                bitmap = retriever.getFrameAtTime();
                // 弱引用
                WeakReference<Bitmap> wy = new WeakReference<Bitmap>(bitmap);
                return wy.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
                retriever = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 视频文件路径获取Frame图
     *
     * @return Bitmap
     * @author zx
     */
    public static Bitmap getFrame(String filePath, long position) {
        if (position == 0) {
            position = 1;
        }
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                retriever.setDataSource(filePath);
                bitmap = retriever.getFrameAtTime(position, MediaMetadataRetriever.OPTION_CLOSEST);
                WeakReference<Bitmap> wy = new WeakReference<Bitmap>(bitmap);// 弱引用
                return wy.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
                retriever = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 视频文件路径获取屏幕尺寸
     *
     * @return Bitmap
     * @author zx
     */
    public static HashMap getVideoScreen(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                retriever.setDataSource(filePath);
                String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).trim();
                String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).trim();
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("width", Integer.parseInt(width));
                map.put("height", Integer.parseInt(height));
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
                retriever = null;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 数据库方式获取缩略图
     * author zx
     * version 1.0
     * since 2017/7/26  .
     */
    private static final int TASK_GROUP_ID = 1999;

    public static Bitmap getThumbnail(Context context, final long id) {
        try {
            if (contentResolver == null) {
                contentResolver = context.getContentResolver();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            final Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, TASK_GROUP_ID, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            /*Bitmap newBitmap = bitmap;
            if (bitmap != null) {
				newBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
				bitmap.recycle();
			}
			return newBitmap;*/
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
