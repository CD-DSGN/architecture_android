package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseModel;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

import java.util.HashMap;

/**
 * Created by zhangmengqi on 2017/3/20.
 */

public class MyCommentsModel extends AppBaseModel {
    public static final int PAGE_SIZE = 20;

    public MyCommentsModel(Context context, AppBaseResponseCallBack callBack) {
        super(context, callBack);
    }


    public void getMyComment(int cpage) {
        HashMap<String, Object> page_info = new HashMap<>();
        page_info.put("pagesize", PAGE_SIZE + "");
        page_info.put("cpage", cpage + "");
        Novate novate = new Novate.Builder(mContext).build();
        novate.executeGet(ApiInterface.MY_COMMENTS_LIST,page_info, mCallBack);
    }


}
