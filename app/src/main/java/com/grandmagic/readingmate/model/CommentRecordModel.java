package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lps on 2017/4/6.
 *
 * @version 1
 * @see
 * @since 2017/4/6 13:14
 */


public class CommentRecordModel {

    private Context mContext;

    public CommentRecordModel(Context mContext) {
        this.mContext = mContext;
    }
    public void getAllreply(int cpage, AppBaseResponseCallBack mBack){
        Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("cpage",cpage);
            mJSONObject.put("pagesize",10);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.GETALLREPLY,mJSONObject.toString(),mBack);
    }
}
