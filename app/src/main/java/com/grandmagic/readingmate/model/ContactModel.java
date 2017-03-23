package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.SPUtils;
import com.tamic.novate.Novate;

import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * 拒绝
     * @param mRequest_id
     * @param mAppBaseResponseCallBack
     */
    public void refuseFriend(String mRequest_id, AppBaseResponseCallBack mAppBaseResponseCallBack) {
        Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("request_id",mRequest_id);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.REQUEST_REFUSE,mJSONObject.toString(),mAppBaseResponseCallBack );
    }

    /**
     * 同意好友请求
     * @param mRequest_id
     * @param mAppBaseResponseCallBack
     */
    public void acceptFriend(String mRequest_id, AppBaseResponseCallBack mAppBaseResponseCallBack) {
        Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("request_id",mRequest_id);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.REQUEST_AGREE,mJSONObject.toString(),mAppBaseResponseCallBack );
    }

    /**
     * 解除好友关系
     * @param mUserid
     * @param b
     */
    public void deleteContact(String mUserid, AppBaseResponseCallBack b) {
      Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("user_id2",mUserid);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.REMOVE_FRIEEND,mJSONObject.toString(),b);

    }

    /**
     * 给好友设置备注
     * @param mUser_id id
     * @param remarkName 备注的昵称
     * @param callback
     */
    public void remarkName(String mUser_id, String remarkName, AppBaseResponseCallBack callback) {
        Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("user_id2",mUser_id);
            mJSONObject.put("remark",remarkName);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.REMARK_FRIEND,mJSONObject.toString(),callback);
    }

    /**
     * 详情页读取用户关注的图书
     * @param mUserid
     * @param mBack
     */
    public void getPersonCollect(String mUserid, AppBaseResponseCallBack mBack) {
        Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("user_id",mUserid);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.BOOK_PERSON_COLLECT,mJSONObject.toString(),mBack);
    }
}
