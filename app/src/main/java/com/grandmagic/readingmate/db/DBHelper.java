package com.grandmagic.readingmate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tamic.novate.util.SPUtils;

/**
 * Created by lps on 2017/3/7.
 */

public class DBHelper {
    /**
     * 获取数据库
     *
     * @return
     */

    public static InviteMessageDao getInviteDao(Context mContext) {
        DaoSession mSession = getDaoSession(mContext);
        return mSession.getInviteMessageDao();
    }

    private static String getDBName(Context mContext) {
        String mString = SPUtils.getInstance().getString(mContext, SPUtils.IM_NAME);
        return mString + "im.db";
    }

    public static ContactsDao getContactsDao(Context mContext) {
        DaoSession mSession = getDaoSession(mContext);
        return mSession.getContactsDao();
    }

    static DaoMaster.DevOpenHelper mDevOpenHelper;

    private static DaoSession getDaoSession(Context mContext) {
        mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, getDBName(mContext), null);
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        return mDaoMaster.newSession();
    }

    static DaoSession mSession;

    public static BookCommentDao getBookCommentDao(Context mContext) {
        mSession = getDaoSession(mContext);
        return mSession.getBookCommentDao();
    }

    public static void close() {
        closeDaosession();
        closeHelper();
    }

    private static void closeHelper() {
        if (mDevOpenHelper != null) {
            mDevOpenHelper.close();
            mDevOpenHelper = null;
        }
    }

    private static void closeDaosession() {
        if (mSession != null) {
            mSession.clear();
            mSession = null;
        }
    }
}
