package com.grandmagic.readingmate.listener;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by lps on 2017/3/1.
 */

public class IMMessageListener implements EMMessageListener{
    private static final String TAG = "IMMessageListener";
    private Context mContext;

    public IMMessageListener(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public void onMessageReceived(List<EMMessage> mList) {
        for (EMMessage msg:
             mList) {
            Log.e(TAG, "onMessageReceived: "+msg );
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
