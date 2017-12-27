package com.budworm.smallutils;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by zhuxing on 2017/1/3.
 */
public class ViewUtil {


    /**
     * Set ImageView Resource
     * author zx
     * version 1.0
     * since 2016/12/30 16:28
     */
    public static void setViewRes(ImageView imageView, boolean or, int img1, int img2) {
        if (imageView != null) {
            if (or) {
                imageView.setImageResource(img1);
            } else {
                imageView.setImageResource(img2);
            }
        }
    }


    /**
     * set BTN BG
     * author zx
     * version 1.0
     * since 2017/5/12 16:13
     */
    public static void setViewBg(View view, boolean or, int nol, int focus) {
        if (view != null) {
            if (or) {
                view.setBackgroundResource(nol);
            } else {
                view.setBackgroundResource(focus);
            }
        }
    }


    /**
     * 显示View
     * author zx
     * version 1.0
     * since 2017/1/18 14:23
     */
    public static void visibleView(View view) {
        if (view != null && (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE)) {
            view.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 隐藏View
     * author zx
     * version 1.0
     * since 2017/1/18 14:23
     */
    public static void invisibleView(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 隐藏View
     * author zx
     * version 1.0
     * since 2017/1/18 14:23
     */
    public static void goneView(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

}
