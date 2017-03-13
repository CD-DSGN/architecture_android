package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;

import org.json.JSONException;
import org.json.JSONObject;

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
}
