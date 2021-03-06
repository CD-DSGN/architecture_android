package com.grandmagic.readingmate.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.db.InviteMessage;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.listener.DefaultSettingsProvider;
import com.grandmagic.readingmate.listener.IMConnectionListener;
import com.grandmagic.readingmate.listener.IMContactListener;
import com.grandmagic.readingmate.listener.IMMessageListenerApp;
import com.grandmagic.readingmate.listener.IMNotifier;
import com.grandmagic.readingmate.listener.IMSettingsProvider;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.tamic.novate.util.Environment;

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
        EMClient.getInstance().chatManager().addMessageListener(mListenerApp);//新消息监听
        EMClient.getInstance().addConnectionListener(mIMConnectionListener);//链接状态监听
        EMClient.getInstance().contactManager().setContactListener(new IMContactListener(appContext));//好友请求监听
        mIMNotifier = new IMNotifier();
        mIMNotifier.init(appContext);
        setUserProfileProvider();
    }

    private void setUserProfileProvider() {
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getEaseUserInfo(username);
            }
        });
    }
    public EaseUser getEaseUserInfo(String mUsernam) {
        EaseUser mUser=new EaseUser(mUsernam);
        ContactsDao mContactsDao = DBHelper.getContactsDao(mAppContext);
        if (mUsernam==null)return null;
        Contacts mContacts = mContactsDao.queryBuilder().whereOr(ContactsDao.Properties.User_id.eq(mUsernam),ContactsDao.Properties.User_name.eq(mUsernam)).build().unique();
        DBHelper.close();
        mUser.setAvatar(Environment.BASEULR_PRODUCTION+mContacts.getAvatar_url().getLarge());
        mUser.setNickname(mContacts.getUser_name());
        return mUser;
    }

    /**
     * 从本地数据库查询好友信息
     * @param mUsernam
     * @return
     */
    public Contacts getUserInfo(String mUsernam) {
        ContactsDao mContactsDao = DBHelper.getContactsDao(mAppContext);
        if (mUsernam==null)return null;
        Contacts mContacts = mContactsDao.queryBuilder().whereOr(ContactsDao.Properties.User_id.eq(mUsernam),ContactsDao.Properties.User_name.eq(mUsernam)).build().unique();
        DBHelper.close();
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
public void newInvaiteMsg(InviteMessage mInviteMessage){
    mIMNotifier.newInvaiteMsg(mInviteMessage);
}
    public IMSettingsProvider getSettingsProvider() {
        if (mSettingsProvider == null) mSettingsProvider = new DefaultSettingsProvider();
        return mSettingsProvider;
    }

}
