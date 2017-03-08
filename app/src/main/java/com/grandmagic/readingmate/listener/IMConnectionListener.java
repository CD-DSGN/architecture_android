package com.grandmagic.readingmate.listener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.grandmagic.readingmate.event.ConnectStateEvent;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.orhanobut.logger.Logger;
import com.tamic.novate.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

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
        SPUtils.getInstance().putString(mContext,SPUtils.IM_STATE,"0");
        EventBus.getDefault().post(new ConnectStateEvent(0));
    }

    @Override
    public void onDisconnected(int error) {
        SPUtils.getInstance().putString(mContext,SPUtils.IM_STATE,error+"");
        Logger.e("onDisconnected" + error);
        EventBus.getDefault().post(new ConnectStateEvent(error));
    }
}
