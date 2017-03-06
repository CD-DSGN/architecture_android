package com.grandmagic.readingmate.listener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.orhanobut.logger.Logger;

/**
 * Created by lps on 2017/3/2.
 * 链接状态监听
 */

public class IMConnectionListener implements EMConnectionListener {
    private Context mContext;

    public IMConnectionListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onConnected() {
        Logger.e("onConnected");
    }

    @Override
    public void onDisconnected(int error) {

        Logger.e("onDisconnected" + error);
    }
}
