package com.grandmagic.readingmate.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseModel;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.ResetPasswordRequsetBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.Novate;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class ResetPasswordModel extends AppBaseModel {
    private ResetPasswordRequsetBean mResetPasswordRequsetBean;
    private String mPwd_confirm;

    public ResetPasswordModel(Context context, ResetPasswordRequsetBean resetPasswordRequsetBean,
                              AppBaseResponseCallBack callBack, String pwd_confirm) {
        super(context, callBack);
        mResetPasswordRequsetBean =  resetPasswordRequsetBean;
        mPwd_confirm = pwd_confirm;
    }

    public void resetPassword() {
        if (checkParameter()) {
            Novate novate = new Novate.Builder(mContext).build();
            String json_str = new Gson().toJson(mResetPasswordRequsetBean);
            novate.executeJson(ApiInterface.RESET_PWD, json_str, mCallBack);
        }
    }


    protected boolean checkParameter() {
        //手机不合法
        if (!KitUtils.checkMobilePhone(mResetPasswordRequsetBean.getPhone_num())) {
            ViewUtils.showToast( mContext.getString(R.string.mobile_invalid));
            return false;
        }

        //验证码合法性判断
        if (TextUtils.isEmpty(mResetPasswordRequsetBean.getVerify_code())) {
            ViewUtils.showToast( mContext.getString(R.string.verify_code_empty));
            return false;
        }

        String pwd = mResetPasswordRequsetBean.getPassword();
        //密码空判断
        if (TextUtils.isEmpty(pwd)) {
            ViewUtils.showToast( mContext.getString(R.string.password_empty));
            return false;
        }
        //确认密码
        if (TextUtils.isEmpty(mPwd_confirm)) {
            ViewUtils.showToast( mContext.getString(R.string.password_confirm_empty));
            return false;
        }

        if (!pwd.equals(mPwd_confirm)) {
            ViewUtils.showToast( mContext.getString(R.string.password_not_equal));
            return false;
        }

        return true;

    }
}
