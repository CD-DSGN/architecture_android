package com.grandmagic.readingmate.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBasePostModel;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.ResetPasswordRequsetBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.Novate;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class ResetPasswordModel extends AppBasePostModel{
    private ResetPasswordRequsetBean mResetPasswordRequsetBean;

    public ResetPasswordModel(Context context, ResetPasswordRequsetBean resetPasswordRequsetBean, AppBaseResponseCallBack callBack) {
        super(context, callBack);
        mResetPasswordRequsetBean =  resetPasswordRequsetBean;
    }

    public void resetPassword() {
        checkParameter();
        Novate novate = new Novate.Builder(mContext).build();
        String json_str = new Gson().toJson(mResetPasswordRequsetBean);
        novate.executeJson(ApiInterface.LOGIN, json_str, mCallBack);
    }

    @Override
    protected void checkParameter() {
        //手机不合法
        if (!KitUtils.checkMobilePhone(mResetPasswordRequsetBean.getPhone_num())) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.mobile_invalid));
            return;
        }

        //验证码合法性判断
        if (TextUtils.isEmpty(mResetPasswordRequsetBean.getVerify_code())) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.verify_code_empty));
            return;
        }

        //密码空判断
        if (TextUtils.isEmpty(mResetPasswordRequsetBean.getPassword())) {
            ViewUtils.showToast(mContext, mContext.getString(R.string.password_empty));
            return;
        }

    }
}
