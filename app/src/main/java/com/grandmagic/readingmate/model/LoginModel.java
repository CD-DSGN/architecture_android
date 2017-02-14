package com.grandmagic.readingmate.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.LoginRequestBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
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
        if (!KitUtils.checkMobilePhone(mLoginRequestBean.getMobile_phone())) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.mobile_invalid));
            return;
        }

        if (TextUtils.isEmpty(mLoginRequestBean.getPassword())) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.password_empty));
            return;
        }
        Novate novate = new Novate.Builder(mContext).build();
        String json_str = new Gson().toJson(mLoginRequestBean);
        novate.executeJson(ApiInterface.LOGIN, json_str, mCallBack);
    }
}
