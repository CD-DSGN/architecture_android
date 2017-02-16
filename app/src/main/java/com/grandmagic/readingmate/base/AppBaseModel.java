package com.grandmagic.readingmate.base;

import android.content.Context;

/**
 * Created by zhangmengqi on 2017/2/10.
 */

public class AppBaseModel {
    protected Context mContext;
    protected AppBaseResponseCallBack mCallBack;


    public AppBaseModel(Context context,AppBaseResponseCallBack callBack) {
        mContext = context;
        mCallBack = callBack;

    }



}
