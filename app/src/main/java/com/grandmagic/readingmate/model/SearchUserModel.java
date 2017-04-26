package com.grandmagic.readingmate.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.UserInfoRequest;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tamic.novate.Novate;
import com.tamic.novate.util.Environment;

import org.json.JSONObject;

/**
 * Created by lps on 2017/3/6.
 */

public class SearchUserModel {
    private static final String TAG = "SearchUserModel";
    private Context mContext;

    public SearchUserModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 通过手机号搜索好友
     *
     * @param phone
     * @param mBack
     */
    public void searchUser(String phone, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).baseUrl(Environment.BASEULR_PRODUCTION).build();
        UserInfoRequest mUserInfoRequest = new UserInfoRequest();
        mUserInfoRequest.setMobile_phone(phone);
        String mS = new Gson().toJson(mUserInfoRequest);
        mNovate.executeJson(ApiInterface.SEARCH_USER, mS, mBack);
    }

    /**
     * 请求添加对方为好友
     *
     * @param user_id2
     * @param message
     * @param mBack
     */
    public void requestAddFriend(String user_id2, String message, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).baseUrl(Environment.BASEULR_PRODUCTION).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("user_id2", Integer.valueOf(user_id2));
            mJSONObject.put("message", message);
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        try {
            EMClient.getInstance().contactManager().addContact(user_id2, message);
        } catch (HyphenateException mE) {
            Log.d(TAG, "requestAddFriend() called with: user_id2 = [" + user_id2 + "], message = [" + message + "], mBack = [" + mBack + "]");

            ViewUtils.showToast(mE.getMessage());
        }
        mNovate.executeJson(ApiInterface.REQUEST_ADD, mJSONObject.toString(), mBack);
    }
}
