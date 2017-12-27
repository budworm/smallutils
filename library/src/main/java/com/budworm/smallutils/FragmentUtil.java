/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 * 2015年8月13日
 */
/**
 *
 */
package com.budworm.smallutils;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;


/**
 * 扫描数据操作
 * author zx
 * version 1.0
 * since 2017/1/4 13:45
 */
public class FragmentUtil {
    private Fragment currentFrag;
    private static FragmentUtil utils;
    private Context context;
    private static FragmentTransaction transaction;

    private FragmentUtil() {}


    /**
     * 获取单列
     * author zx
     * version 1.0
     * since 2017/1/4 13:37
     */
    public static synchronized FragmentUtil getInstance(Activity activity) {
        if (utils == null) {
            utils = new FragmentUtil();
        }
        //transaction = activity.getSupportFragmentManager.fragmentManager.beginTransaction();
        transaction = activity.getFragmentManager().beginTransaction();
        return utils;
    }


    /**
     * 获取单列
     * author zx
     * version 1.0
     * since 2017/1/4 13:37
     */
    public static synchronized FragmentUtil getInstance() {
        if (utils == null) {
            utils = new FragmentUtil();
        }
        return utils;
    }


    /**
     * 显示Fragment
     * author zx
     * version 1.0
     * since 2017/1/6 11:09
     */
    public void showFragment(FragmentTransaction transaction, int containerViewId, Fragment displayFrag, String tag) {
        if (currentFrag != displayFrag) {
            // FragmentTransaction transaction = getSupportFragmentManager.fragmentManager.beginTransaction();
            // transaction.setCustomAnimations(R.anim.fragment_in_from_right, R.anim.fragment_out_to_left);
            if (displayFrag == null)
                return;
            if (displayFrag.isAdded()) {
                transaction.show(displayFrag);
            } else {
                if (tag != null && !"".equals(tag)) {
                    transaction.add(containerViewId, displayFrag, tag);
                } else {
                    transaction.add(containerViewId, displayFrag);
                }
            }
            if (currentFrag != null) {
                transaction.hide(currentFrag);
            }
            currentFrag = displayFrag;
            //设置切换动画，从右边进入，左边退出
            //transaction.commit();
            transaction.commitAllowingStateLoss();
        }
    }


    /**
     * 显示Fragment
     * author zx
     * version 1.0
     * since 2017/1/6 11:09
     */
    public void replaceFragment(FragmentTransaction transaction, int containerViewId, Fragment displayFrag, String tag) {
        if (displayFrag == null)
            return;
        if (displayFrag.isAdded()) {
            transaction.show(displayFrag);
        } else {
            if (tag != null && !"".equals(tag)) {
                transaction.replace(containerViewId, displayFrag, tag);
            } else {
                transaction.replace(containerViewId, displayFrag);
            }
        }
        //设置切换动画，从右边进入，左边退出
        transaction.commit();
    }

}
