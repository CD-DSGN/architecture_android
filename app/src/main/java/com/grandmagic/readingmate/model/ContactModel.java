package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.SPUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;

import java.util.List;

/**
 * Created by lps on 2017/3/1.
 */

public class ContactModel {
    private Context mContext;

    public ContactModel(Context mContext) {
        this.mContext = mContext;
    }

    public void getAllFriendFromServer(AppBaseResponseCallBack mBack){
        Novate mNovate=new Novate.Builder(mContext).build();
        mNovate.executeGet(ApiInterface.SHOWFRIEND, SPUtils.getInstance().getToken(mContext)
                ,mBack );
}
}