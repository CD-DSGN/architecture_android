package com.grandmagic.readingmate.model;

import android.content.Context;


import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by zhangmengqi on 2017/3/28.
 */

public class CommentDetailModel {
    private Context mContext;
    public static int PAGE_COUNT_LIKERS = 3;
    public CommentDetailModel(Context context) {
        mContext = context;
    }

    public void getCommentDetail(String commnet_id, AppBaseResponseCallBack callBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("comment_id", commnet_id);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        mNovate.executeJson(ApiInterface.COMMENT_DETAIL, jsonObject.toString(), callBack);
    }

    public void getAllLikes(String commnet_id, AppBaseResponseCallBack callBack, int cur_page) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject jsonObject = new JSONObject();
        try {
            HashMap<String, Object> map = new HashMap<>();
            jsonObject.put("comment_id", commnet_id);
            jsonObject.put("cpage", cur_page);
            jsonObject.put("pagesize", PAGE_COUNT_LIKERS);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        mNovate.executeJson(ApiInterface.LIKERS_INFO, jsonObject.toString(), callBack);
    }

}
