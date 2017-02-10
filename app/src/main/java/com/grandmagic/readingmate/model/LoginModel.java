package com.grandmagic.readingmate.model;

import android.content.Context;

import com.google.gson.Gson;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.LoginRequestBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

/**
 * Created by lps on 2017/2/7.
 */

public class LoginModel {
    private Context mContext;
    private LoginRequestBean mLoginRequestBean;
    private AppBaseResponseCallBack mCallBack;

    public LoginModel(Context context, LoginRequestBean loginRequestBean, AppBaseResponseCallBack callBack) {
        mContext = context;
        mLoginRequestBean = loginRequestBean;
        mCallBack = callBack;
    }

    public void login() {
        Novate novate = new Novate.Builder(mContext).build();
        String json_str = new Gson().toJson(mLoginRequestBean);
        novate.executeJson(ApiInterface.LOGIN, json_str, mCallBack);
    }
}
