package com.grandmagic.readingmate.push;

import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.grandmagic.readingmate.R;
import com.orhanobut.logger.Logger;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by lps on 2017/3/30.
 *
 * @version 1
 * @see
 * @since 2017/3/30 13:36
 */


public class IUmengMessageHandler  extends UmengMessageHandler {
    private static final String TAG = "IUmengMessageHandler";
//    此方法会在通知展示到通知栏时回调
//    若SDK默认的消息展示样式不符合开发者的需求，可通过覆盖该方法来自定义通知栏展示样式
    @Override
    public Notification getNotification(Context context, UMessage msg) {
        return super.getNotification(context, msg);
    }

    @Override
    public void dealWithCustomMessage(Context mContext, UMessage mUMessage) {

        super.dealWithCustomMessage(mContext, mUMessage);
    }

    /**
     * 为了方便调试，重写了这个方法，似乎是所有消息都会走这个方法的
     * @param mContext
     * @param mUMessage
     */
    @Override
    public void handleMessage(Context mContext, UMessage mUMessage) {
        Log.d(TAG, "handleMessage() called with: mContext = [" + mContext + "], mUMessage = [" + mUMessage.getRaw() + "]");
        super.handleMessage(mContext, mUMessage);
    }
}
