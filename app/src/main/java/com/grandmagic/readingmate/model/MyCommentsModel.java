package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseModel;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

/**
 * Created by zhangmengqi on 2017/3/20.
 */

public class MyCommentsModel extends AppBaseModel {
    public MyCommentsModel(Context context, AppBaseResponseCallBack callBack) {
        super(context, callBack);
    }

    public void getMyCommentsList() {
        Novate novate = new Novate.Builder(mContext).build();
        novate.executeGet(ApiInterface.MY_COMMENTS_LIST,mCallBack);
    }

}
