package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lps on 2017/3/7.
 */

public class BookModel {
    private Context mContext;

    public BookModel(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 扫码关注图书
     *
     * @param mIsbn_code ISBN码
     * @param mBack      回调
     */
    public void fllowBook(String mIsbn_code, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("isbn_code", mIsbn_code);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.COLLECT_SCAN, mJSONObject.toString(), mBack);
    }

    /**
     * 加载用户收藏的图书
     */
    public void loadCollectBook(AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        mNovate.executeGet(ApiInterface.COLLECT_BOOKDISPLAY, mBack);
    }

    /**
     * 通过关键字搜索图书
     *
     * @param keyword
     * @param mAppBaseResponseCallBack
     */
    public void searchBook(String keyword, AppBaseResponseCallBack mAppBaseResponseCallBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("keyword", keyword);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.BOOK_SEARCH, mJSONObject.toString(), mAppBaseResponseCallBack);
    }

    public void getHotword(AppBaseResponseCallBack b) {
        Novate mNovate=new Novate.Builder(mContext).build();
        mNovate.executeGet(ApiInterface.BOOK_HOT,b);
    }

    /**
     * 图书详细信息
     * @param mBook_id book_id
     * @param back
     */
    public void getBookDetail(String mBook_id, AppBaseResponseCallBack back) {
        Novate mNovate=new Novate.Builder(mContext).build();
        Map<String,Object> mStringMap=new HashMap<>();
        mStringMap.put("book_id",mBook_id);
        mNovate.executeGet(ApiInterface.BOOK_DETAIL,mStringMap,back, SPUtils.getInstance().getToken(mContext));
    }

    /**
     * 对某本关注的书进行评论
     * @param mBook_id 书的id
     * @param mScore 评分
     * @param mContent 评论内容
     * @param callback 回调
     */
    public void ScoreBook(String mBook_id, int mScore, String mContent, AppBaseResponseCallBack callback) {
    Novate mNovate=new Novate.Builder(mContext).build();
JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("book_id",mBook_id);
            mJSONObject.put("score_num",mScore);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.SCORE_BOOK,mJSONObject.toString(),callback);
    }
}
