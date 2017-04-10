package com.grandmagic.readingmate.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.LoginActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.LoginRequestBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.KitUtils;
import com.tamic.novate.util.SPUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.Novate;

import java.util.HashMap;

/**
 * Created by lps on 2017/2/7.
 * 控制用户的登录、登出操作
 *
 */

public class LoginModel {
    private static final int PASSWORD_MAX_LEN = 10;
    private static final int PASSWORD_MIN_LEN = 5;

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
            ViewUtils.showToast(mContext.getString(R.string.mobile_invalid));
            return;
        }

        if (TextUtils.isEmpty(mLoginRequestBean.getPassword())) {
            ViewUtils.showToast(mContext.getString(R.string.password_empty));
            return;
        }

        int passwd_len = mLoginRequestBean.getPassword().length();
        if (passwd_len < PASSWORD_MIN_LEN && passwd_len > PASSWORD_MAX_LEN) {
            ViewUtils.showToast(mContext.getString(R.string.password_len_check1) + PASSWORD_MIN_LEN + "-"
                    + PASSWORD_MAX_LEN + mContext.getString(R.string.password_check2));
        }


        Novate novate = new Novate.Builder(mContext).build();
        String json_str = new Gson().toJson(mLoginRequestBean);
        novate.executeJsonNoToken(ApiInterface.LOGIN, json_str, mCallBack);
    }

    public void logout() {
        //先拿到token
        String token = SPUtils.getInstance().getToken(mContext);
        //token本来就是空，那直接跳转登录页
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(mContext,
                    LoginActivity.class);
            mContext.startActivity(intent);
            return;
        }

        //清除本地token
        SPUtils.getInstance().clearToken(mContext);
        Novate novate = new Novate.Builder(mContext).build();

        novate.executeGet(ApiInterface.LOGOUT, new HashMap<String, Object>(), mCallBack, token);

    }
}
