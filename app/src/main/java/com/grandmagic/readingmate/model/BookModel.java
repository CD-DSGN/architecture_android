package com.grandmagic.readingmate.model;

import android.content.Context;
import android.text.TextUtils;

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
 * @version 1
 * @since 2017年3月24日16:08:31
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
    public void loadCollectBook(int mCurrpage, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        Map<String, Object> mStringObjectMap = new HashMap<>();
        mStringObjectMap.put("cpage", mCurrpage);
        mNovate.executeGet(ApiInterface.COLLECT_BOOKDISPLAY, mStringObjectMap, mBack, SPUtils.getInstance().getToken(mContext));
    }

    /**
     * 通过关键字搜索图书
     *  @param keyword
     * @param mCurrpage 页码
     * @param mAppBaseResponseCallBack
     */
    public void searchBook(String keyword, int mCurrpage, AppBaseResponseCallBack mAppBaseResponseCallBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("keyword", keyword);
            mJSONObject.put("cpage", mCurrpage);
            mJSONObject.put("pagesize", 10);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.BOOK_SEARCH, mJSONObject.toString(), mAppBaseResponseCallBack);
    }

    public void getHotword(AppBaseResponseCallBack b) {
        Novate mNovate = new Novate.Builder(mContext).build();
        mNovate.executeGet(ApiInterface.BOOK_HOT, b);
    }

    /**
     * 图书详细信息
     *
     * @param mBook_id book_id
     * @param back
     */
    public void getBookDetail(String mBook_id, AppBaseResponseCallBack back) {
        Novate mNovate = new Novate.Builder(mContext).build();
        Map<String, Object> mStringMap = new HashMap<>();
        mStringMap.put("book_id", mBook_id);
        mNovate.executeGet(ApiInterface.BOOK_DETAIL, mStringMap, back, SPUtils.getInstance().getToken(mContext));
    }

    /**
     * 对某本关注的书进行评论
     *
     * @param mBook_id 书的id
     * @param mScore   评分
     * @param mContent 评论内容
     * @param callback 回调
     */
    public void ScoreBook(String mBook_id, int mScore, String mContent, AppBaseResponseCallBack callback) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("book_id", mBook_id);
            mJSONObject.put("score_num", mScore);
            if (!TextUtils.isEmpty(mContent))
                mJSONObject.put("comment_content", mContent);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.SCORE_BOOK, mJSONObject.toString(), callback);
    }

    /**
     * 获取我过往对这本书的评价
     *
     * @param mBook_id
     * @param mBack
     */
    public void getMyComment(String mBook_id, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("book_id", mBook_id);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.BOOK_COMMENTSCORESTATUS, mJSONObject.toString(), mBack);

        mNovate.executeJson(ApiInterface.BOOK_COMMENTSCORESTATUS, mJSONObject.toString(), mBack);
    }

    /**
     * 删除对图书的评论
     *
     * @param bookid bookid
     * @param mBack
     */
    public void deleteBookComment(String bookid, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("book_id", bookid);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.DELETE_BOOKCOMMENT, mJSONObject.toString(), mBack);
    }

    /**
     * 对评论点赞
     *
     * @param comment_id comment_id
     * @param mBack
     */
    public void thumbBookComment(String comment_id, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("comment_id", comment_id);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.THUMB_BOOKCOMMENT, mJSONObject.toString(), mBack);
    }

    /**
     * 加载图书的评论
     *
     * @param mBook_id
     * @param mCurrpage
     * @param mOrder_way
     */
    public void loadBookComment(String mBook_id, int mCurrpage, String mOrder_way, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        HashMap<String, Object> mHashMap = new HashMap<>();
        mHashMap.put("book_id", mBook_id);
        mHashMap.put("cpage", mCurrpage);
        mHashMap.put("order_way", mOrder_way);
        mNovate.executeGet(ApiInterface.BOOK_COMMENT_DETAIL, mHashMap, mBack, SPUtils.getInstance().getToken(mContext));
    }

    public void loadAllFollower(String bookid,int cpage,AppBaseResponseCallBack mBack){
        Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("book_id",bookid);
            mJSONObject.put("cpage",cpage);
            mJSONObject.put("pagesize",30);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.BOOK_ALL_FOLLOWERS,mJSONObject.toString(),mBack);

    }
}
