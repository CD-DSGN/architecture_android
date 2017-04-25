package com.grandmagic.readingmate.model;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lps on 2017/4/25.
 * 完善资料
 *
 * @version 1
 * @see
 * @since 2017/4/25 17:31
 */


public class InfoImproveModel {
    private Context mContext;

    public InfoImproveModel(Context mContext) {
        this.mContext = mContext;
    }

    public void uploadAvatar(String avatar, AppBaseResponseCallBack mBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("avatar", avatar);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.UPLOAD_AVAR, mJSONObject.toString(), mBack);
    }

    public void addInfo(String mNickName, int mGender, AppBaseResponseCallBack<NovateResponse> mAppBaseResponseCallBack) {
        Novate mNovate = new Novate.Builder(mContext).build();
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("user_name", mNickName);
            mJSONObject.put("gender", mGender);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        mNovate.executeJson(ApiInterface.ADD_INFOREGISTER, mJSONObject.toString(), mAppBaseResponseCallBack);
    }
}
