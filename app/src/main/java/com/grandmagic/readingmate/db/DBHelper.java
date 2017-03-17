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
     * @return
     */

    public static InviteMessageDao getInviteDao(Context mContext){
        DaoMaster.DevOpenHelper mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, getDBName(mContext), null);
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        DaoSession mSession = mDaoMaster.newSession();
        return mSession.getInviteMessageDao();
    }
    private static String getDBName(Context mContext){
        String mString = SPUtils.getInstance().getString(mContext, SPUtils.IM_NAME);
        return mString+"im.db";
    }
    public static ContactsDao getContactsDao(Context mContext){
        DaoMaster.DevOpenHelper mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, getDBName(mContext), null);
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        DaoSession mSession = mDaoMaster.newSession();
        return mSession.getContactsDao();
    }
}
