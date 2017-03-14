package com.grandmagic.readingmate.model;

import android.content.Context;
import android.text.TextUtils;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseModel;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.PersonalInfoChangeRequestBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.Novate;

/**
 * Created by zhangmengqi on 2017/3/13.
 */

public class UserInfoModel extends AppBaseModel {

    public UserInfoModel(Context context, AppBaseResponseCallBack callBack) {
        super(context, callBack);
    }

    public void  getUserInfo() {
        Novate novate = new Novate.Builder(mContext).build();
        novate.executeGet(ApiInterface.GET_USR_INFO, mCallBack);
    }

    public void setUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            ViewUtils.showToast(mContext.getString(R.string.nikc_name_empty));
            return;
        }
        
        Novate novate = new Novate.Builder(mContext).build();
        PersonalInfoChangeRequestBean personalInfoChangeRequestBean = new PersonalInfoChangeRequestBean();
        personalInfoChangeRequestBean.setUser_name(userName);
        novate.executeJson(ApiInterface.SET_USR_INFO, personalInfoChangeRequestBean.toGson(), mCallBack);
    }

    public void setsign(String signature) {
        if (TextUtils.isEmpty(signature)) {
            ViewUtils.showToast(mContext.getString(R.string.sign_empty));
            return;
        }
        
        Novate novate = new Novate.Builder(mContext).build();
        PersonalInfoChangeRequestBean personalInfoChangeRequestBean = new PersonalInfoChangeRequestBean();
        personalInfoChangeRequestBean.setSignature(signature);
        novate.executeJson(ApiInterface.SET_USR_INFO, personalInfoChangeRequestBean.toGson(), mCallBack);

    }

    public void setGender(int gender) {
        if (gender != 1 && gender != 2) {
            ViewUtils.showToast(mContext.getString(R.string.reselect_gender));
            return;
        }

        Novate novate = new Novate.Builder(mContext).build();

        PersonalInfoChangeRequestBean personalInfoChangeRequestBean = new PersonalInfoChangeRequestBean();
        personalInfoChangeRequestBean.setGender(gender);
        novate.executeJson(ApiInterface.SET_USR_INFO, personalInfoChangeRequestBean.toGson(), mCallBack);
    }




}
