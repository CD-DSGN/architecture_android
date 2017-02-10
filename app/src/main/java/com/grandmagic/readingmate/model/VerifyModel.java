package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public class VerifyModel {
    public static void getVerifyCode(final Context context, String phone_num) {
        Novate novate = new Novate.Builder(context).build();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("mobile_phone", phone_num);
        novate.executeGet(ApiInterface.VERIFY_CODE, parameters, new AppBaseResponseCallBack<NovateResponse<Object>>(context) {
            @Override
            public void onSuccee(NovateResponse<Object> response) {
               
            }
        });
    }
}
