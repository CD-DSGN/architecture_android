package com.grandmagic.readingmate.model;

import android.app.Activity;
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
 * Created by lps on 2017/3/16.
 */

public class SearchModel {
    private Context mContext;

    public SearchModel(Context mContext) {

        this.mContext = mContext;
    }

    /**
     * 上传用户定位
     * @param mLatitude 维度
     * @param mLongitude 经度
     * @param mProvince 省
     * @param mCity 市
     * @param mDistrict 区
     * @param mStreet 街道
     * @param mBack
     */
    public void stepLocation(double mLatitude, double mLongitude, String mProvince, String mCity, String mDistrict, String mStreet, AppBaseResponseCallBack mBack) {
        Novate mNovate=new Novate.Builder(mContext).build();
        JSONObject mJSONObject=new JSONObject();
        try {
            mJSONObject.put("latitude",mLatitude);
            mJSONObject.put("longitude",mLongitude);
            mJSONObject.put("province",mProvince);
            mJSONObject.put("city",mCity);
            mJSONObject.put("area",mDistrict);
            mJSONObject.put("street",mStreet);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.STEP_LOCATION,mJSONObject.toString(),mBack);
    }

    /**
     * 获取附近的好友
     * @param mCurrpage
     * @param mBack
     */
    public void getLocationPerson(int mCurrpage, AppBaseResponseCallBack mBack) {
        Novate mNovate=new Novate.Builder(mContext).build();
        Map<String,Object> mStringObjectMap=new HashMap<>();
        mStringObjectMap.put("page",mCurrpage);
mNovate.executeGet(ApiInterface.GET_LOCATION_PERSON,mStringObjectMap,mBack, SPUtils.getInstance().getToken(mContext));
    }
}
