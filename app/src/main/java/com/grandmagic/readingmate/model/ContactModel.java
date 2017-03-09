package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.util.SPUtils;
import com.tamic.novate.Novate;

/**
 * Created by lps on 2017/3/1.
 */

public class ContactModel {
    private Context mContext;

    public ContactModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取所有好友
     * @param mBack
     */
    public void getAllFriendFromServer(AppBaseResponseCallBack mBack){
        Novate mNovate=new Novate.Builder(mContext).build();
        mNovate.executeGet(ApiInterface.SHOWFRIEND,mBack );
}

    /**
     * 获取所有好友请求列表
     * @param mBack
     */
    public void getallRequest(AppBaseResponseCallBack mBack) {
        Novate mNovate=new Novate.Builder(mContext).build();
        mNovate.executeGet(ApiInterface.REQUESTINFO_REQUESTLIST,mBack );
    }
}
