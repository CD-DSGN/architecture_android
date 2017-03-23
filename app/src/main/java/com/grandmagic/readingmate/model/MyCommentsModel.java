package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

import java.util.HashMap;

/**
 * Created by zhangmengqi on 2017/3/20.
 */

public class MyCommentsModel {
    public static final int PAGE_SIZE = 20;
    private Context mContext;

    public MyCommentsModel(Context context) {
        mContext = context;
    }

    public void getMyComment(AppBaseResponseCallBack callBack, int cpage) {
        HashMap<String, Object> page_info = new HashMap<>();
        page_info.put("pagesize", PAGE_SIZE + "");
        page_info.put("cpage", cpage + "");
        Novate novate = new Novate.Builder(mContext).build();
        novate.executeGet(ApiInterface.MY_COMMENTS_LIST,page_info, callBack);
    }


}
