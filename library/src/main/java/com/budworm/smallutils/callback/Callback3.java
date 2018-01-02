package com.budworm.smallutils.callback;

/**
 * msg
 * author zx
 * version 1.0
 * since 2017/9/13  .
 */
public abstract class Callback3<X, Y, Z> extends BaseCallback {

    public abstract void onMsg(X msg1, Y msg2, Z msg3);

}
