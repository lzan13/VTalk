package com.vmloft.develop.app.vtalk.common;

import com.vmloft.develop.library.tools.VMCallback;

/**
 * Create by lzan13 on 18/8/14 下午4:26
 */
public abstract class Callback implements VMCallback {
    @Override
    public void onDone(Object object) {

    }

    @Override
    public void onError(int code, String desc) {

    }

    @Override
    public void onProgress(int progress, String desc) {

    }
}
