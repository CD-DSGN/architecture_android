package com.grandmagic.readingmate.model;

import android.content.Context;

import com.google.gson.Gson;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.UserInfoRequest;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.util.SPUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.util.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lps on 2017/3/6.
 */

public class SearchUserModel {

    private Context mContext;

    public SearchUserModel(Context mContext) {
        this.mContext = mContext;
    }

    public void searchUser(String phone, AppBaseResponseCallBack mBack){
        Novate mNovate=new Novate.Builder(mContext).baseUrl(Environment.BASEULR_PRODUCTION).build();
//        JSONObject mStringMap=new JSONObject();
//        try {
//            mStringMap.put("mobile_phone",phone);
//        } catch (JSONException mE) {
//            mE.printStackTrace();
//        }
        UserInfoRequest mUserInfoRequest=new UserInfoRequest();
        mUserInfoRequest.setMobile_phone(phone);
        String mS = new Gson().toJson(mUserInfoRequest);
        mNovate.executeJson(ApiInterface.SEARCH_USER,mS,mBack);
    }
}
