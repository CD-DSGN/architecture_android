package com.grandmagic.readingmate.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DaoMaster;
import com.grandmagic.readingmate.db.DaoSession;
import com.grandmagic.readingmate.listener.DefaultSettingsProvider;
import com.grandmagic.readingmate.listener.IMConnectionListener;
import com.grandmagic.readingmate.listener.IMMessageListenerApp;
import com.grandmagic.readingmate.listener.IMNotifier;
import com.grandmagic.readingmate.listener.IMSettingsProvider;
import com.grandmagic.readingmate.listener.IMUserProfileProvider;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lps on 2017/3/2.
 * IM相关
 */

public class IMHelper {
    private static final String TAG = "IMHelper";
    private static IMHelper instance = null;
    private IMMessageListenerApp mListenerApp = null;
    IMConnectionListener mIMConnectionListener;
    private Context mAppContext;
    private List<Activity> mActivityList = new ArrayList<>();
    private IMNotifier mIMNotifier;//提示的工具类
    IMSettingsProvider mSettingsProvider;

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
        mAppContext = appContext;
        EMClient.getInstance().chatManager().addMessageListener(mListenerApp);
        EMClient.getInstance().addConnectionListener(mIMConnectionListener);
        mIMNotifier = new IMNotifier();
        mIMNotifier.init(appContext);
    }


    /**
     * 从本地数据库查询好友信息
     * @param mUsernam
     * @return
     */
    public Contacts getUserInfo(String mUsernam) {
        DaoMaster.DevOpenHelper mDevOpenHelper = new DaoMaster.DevOpenHelper(mAppContext, "contacts-db", null);
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        DaoSession mDaoSession = mDaoMaster.newSession();
        Contacts mContacts = mDaoSession.queryBuilder(Contacts.class).whereOr(ContactsDao.Properties.User_id.eq(mUsernam),ContactsDao.Properties.User_name.eq(mUsernam)).build().unique();
        Log.e(TAG, "getUserInfo: "+(mContacts==null?null:mContacts.toString()) );
        return mContacts;
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

    public IMSettingsProvider getSettingsProvider() {
        if (mSettingsProvider == null) mSettingsProvider = new DefaultSettingsProvider();
        return mSettingsProvider;
    }

}
