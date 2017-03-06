package com.grandmagic.readingmate.model;

import android.content.Context;

import com.google.gson.Gson;
import com.grandmagic.readingmate.base.AppBaseModel;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.FeedBackRequestBean;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.util.SPUtils;
import com.tamic.novate.Novate;

/**
 * Created by zhangmengqi on 2017/2/16.
 */

//用户反馈类
public class FeedBackModel extends AppBaseModel {
    protected FeedBackRequestBean mFeedBackRequestBean;
    public FeedBackModel(Context context, FeedBackRequestBean feedBackRequestBean, AppBaseResponseCallBack callBack) {
        super(context, callBack);
        mFeedBackRequestBean = feedBackRequestBean;
    }

    public void feedBack() {
        if (!checkParamter()) {
            return;
        }
        Novate novate = new Novate.Builder(mContext).build();
        String json_str = new Gson().toJson(mFeedBackRequestBean);
        novate.executeJson(ApiInterface.FEEDBACK,
                json_str, mCallBack);
    }

    //可能要做字数限制检查
    private boolean checkParamter() {
        return true;
    }


}
