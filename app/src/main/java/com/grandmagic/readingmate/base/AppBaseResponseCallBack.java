package com.grandmagic.readingmate.base;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.grandmagic.readingmate.activity.LoginActivity;
import com.grandmagic.readingmate.consts.ApiErrorConsts;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public abstract class AppBaseResponseCallBack<T> implements Novate.ResponseCallBack<T> {
    private Context mContext;

    public AppBaseResponseCallBack(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        //        作统一错误处理
        if (e != null) {
            if (e.getCode() != ApiErrorConsts.token_invalide) {
                Toast.makeText(mContext, e.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }

        }
    }
}
