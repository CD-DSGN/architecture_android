package com.grandmagic.readingmate.listener;

import android.content.Context;
import android.util.Log;

import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by lps on 2017/3/1.
 * Mainactivity级别的监听器，新消息
 */

public class IMMessageListenerMain implements EMMessageListener {
    private static final String TAG = "IMMessageListenerApp";
    private Context mContext;

    public IMMessageListenerMain(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public void onMessageReceived(List<EMMessage> mList) {
        for (EMMessage msg : mList) {
            Log.e(TAG, "onMessageReceived: " + msg);
//            IMHelper.getInstance().onNewMsg(msg);
            ((MainActivity) (mContext)).newMsg();
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> mList) {

    }

    @Override
    public void onMessageRead(List<EMMessage> mList) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> mList) {

    }

    @Override
    public void onMessageChanged(EMMessage mEMMessage, Object mO) {

    }
}
