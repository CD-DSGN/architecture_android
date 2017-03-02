package com.grandmagic.readingmate.utils;

import android.app.Activity;
import android.content.Context;

import com.grandmagic.readingmate.listener.IMConnectionListener;
import com.grandmagic.readingmate.listener.IMMessageListenerApp;
import com.grandmagic.readingmate.listener.IMNotifier;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lps on 2017/3/2.
 * IM相关
 */

public class IMHelper {
    private static IMHelper instance = null;
    private IMMessageListenerApp mListenerApp = null;
    IMConnectionListener mIMConnectionListener;
    private List<Activity> mActivityList = new ArrayList<>();
    private IMNotifier mIMNotifier;//提示的工具类

    private IMHelper() {
    }

    public static IMHelper getInstance() {
        if (instance == null) {
            synchronized (IMHelper.class) {
                if (instance == null) {
                    instance = new IMHelper();
                }
            }
        }
        return instance;
    }

    public void init(Context appContext) {
        mListenerApp = new IMMessageListenerApp(appContext);
         mIMConnectionListener = new IMConnectionListener(appContext);
        EMClient.getInstance().chatManager().addMessageListener(mListenerApp);
        EMClient.getInstance().addConnectionListener(mIMConnectionListener);
        mIMNotifier = new IMNotifier();
        mIMNotifier.init(appContext);
    }

    /**
     * 是否有前台activity
     *
     * @return
     */
    public boolean hasForegroundActivies() {
        return mActivityList.size() != 0;
    }

    /**
     * 往mActivityList添加avtivity
     *
     * @param mActivity
     */
    public void pushActivity(Activity mActivity) {
        if (!mActivityList.contains(mActivity)) {
            mActivityList.add(0, mActivity);
        }
    }

    /**
     * 从mActivityList移除activity
     *
     * @param mActivity
     */
    public void popActivity(Activity mActivity) {
        mActivityList.remove(mActivity);
    }

    public void onNewMsg(EMMessage mMsg) {
        mIMNotifier.newMsg(mMsg);
    }
}
