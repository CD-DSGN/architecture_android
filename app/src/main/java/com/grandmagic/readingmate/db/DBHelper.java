package com.grandmagic.readingmate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lps on 2017/3/7.
 */

public class DBHelper {
    /**
     * 获取数据库
     * @return
     */

    public static InviteMessageDao getInviteDao(Context mContext){
        DaoMaster.DevOpenHelper mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, "contacts.db", null);
        SQLiteDatabase db = mDevOpenHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        DaoSession mSession = mDaoMaster.newSession();
        return mSession.getInviteMessageDao();
    }
}
