package com.budworm.smallutils.anim;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * author zx
 * version 1.0
 * since 2017/1/3 14:40
 */
public class AnimationUtil {

    /**
     * 变色提示动画
     * author zx
     * version 1.0
     * since 2017/1/12 13:54
     */
    public static void flashAnimation(View view) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", 0x65242424, 0x00000000);
        colorAnim.setDuration(2000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }


}
