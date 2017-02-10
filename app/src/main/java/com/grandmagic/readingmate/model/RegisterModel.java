package com.grandmagic.readingmate.model;

import android.content.Context;

import com.google.gson.Gson;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.RegisterRequestBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class RegisterModel {
    private RegisterRequestBean mRegisterBean;
    private AppBaseResponseCallBack mCallBack;
    private Context mContext;

    public RegisterModel(Context context, RegisterRequestBean registerBean, AppBaseResponseCallBack callBack) {
        mRegisterBean = registerBean;
        mCallBack = callBack;
        mContext = context;
    }

    public void register() {
        Novate novate = new Novate.Builder(mContext).build();
        String json_str = new Gson().toJson(mRegisterBean);
        novate.executeJson(ApiInterface.REGISTER, json_str, mCallBack);
    }
}
