package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseModel;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class VerifyModel extends AppBaseModel {
//    private Context mContext;
//    private AppBaseResponseCallBack mCallBack;

    public VerifyModel(Context context, AppBaseResponseCallBack callBack) {
//        mContext = context;
//        mCallBack = callBack;
        super(context, callBack);
    }

    public void getVerifyCode(String phone_num) {
        Novate novate = new Novate.Builder(mContext).build();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("mobile_phone", phone_num);
        novate.executeGet(ApiInterface.VERIFY_CODE, parameters, mCallBack
        );
    }
}
