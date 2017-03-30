package com.grandmagic.readingmate.push;

import android.content.Context;
import android.util.Log;

import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by lps on 2017/3/30.
 *
 * @version 1
 * @see
 * @since 2017/3/30 14:25
 */


public class IUmengNotificationClickHandler extends UmengNotificationClickHandler {
    private static final String TAG = "IUmengNotificationClick";
    public IUmengNotificationClickHandler() {
        super();
    }

    @Override
    public void handleMessage(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "handleMessage() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage + "]");
        super.handleMessage(mContext, mUMessage);
    }

    @Override
    public void dismissNotification(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "dismissNotification() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage + "]");
        super.dismissNotification(mContext, mUMessage);
    }

    @Override
    public void autoUpdate(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "autoUpdate() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage + "]");
        super.autoUpdate(mContext, mUMessage);
    }

    @Override
    public void openUrl(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "openUrl() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage + "]");
        super.openUrl(mContext, mUMessage);
    }

    @Override
    public void openActivity(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "openActivity() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage + "]");
        super.openActivity(mContext, mUMessage);
    }

    @Override
    public void launchApp(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "launchApp() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage + "]");
        super.launchApp(mContext, mUMessage);
    }

    @Override
    public void dealWithCustomAction(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "dealWithCustomAction() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage + "]");
        super.dealWithCustomAction(mContext, mUMessage);
    }
}
