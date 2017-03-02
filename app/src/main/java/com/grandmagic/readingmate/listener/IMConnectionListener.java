package com.grandmagic.readingmate.listener;

import android.content.Context;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;

/**
 * Created by lps on 2017/3/2.
 * 链接状态监听
 */

public class IMConnectionListener implements EMConnectionListener{
    private Context mContext;
    private static final String TAG = "IMConnectionListener";

    public IMConnectionListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected(int error) {

        Log.e(TAG, "onDisconnected: "+error );
    }
}
