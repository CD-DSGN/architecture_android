package com.grandmagic.readingmate.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.RegisterRequestBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.Novate;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class RegisterModel {
    private RegisterRequestBean mRegisterBean;
    private AppBaseResponseCallBack mCallBack;
    private Context mContext;
    private String pwd_comfirm;

    public RegisterModel(Context context, RegisterRequestBean registerBean, AppBaseResponseCallBack callBack, String password_comfirm) {
        mRegisterBean = registerBean;
        mCallBack = callBack;
        mContext = context;
        pwd_comfirm = password_comfirm;
    }

    public void register() {
        //手机合法性检查
        if (!KitUtils.checkMobilePhone(mRegisterBean.getMobile_phone())) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.mobile_invalid));
            return;
        }

        //判断验证码是否为空
        if (TextUtils.isEmpty(mRegisterBean.getVerification_code())) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.verify_code_empty));
            return;
        }

        String pwd = mRegisterBean.getPassword();
        if (TextUtils.isEmpty(pwd)) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.password_empty));
            return;
        }

        if (TextUtils.isEmpty(pwd_comfirm)) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.password_confirm_empty));
            return;
        }

        //判断是密码和确认密码否一致
        if (!pwd.equals(pwd_comfirm)) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.password_not_equal));
            return;
        }

        Novate novate = new Novate.Builder(mContext).build();
        String json_str = new Gson().toJson(mRegisterBean);
        novate.executeJson(ApiInterface.REGISTER, json_str, mCallBack);
    }
}
