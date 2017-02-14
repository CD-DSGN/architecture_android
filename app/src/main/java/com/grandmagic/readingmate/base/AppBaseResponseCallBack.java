package com.grandmagic.readingmate.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
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
    private boolean mNeedLoading = false;  //需不需要loading
    private Object mLoadingView = null;

    public AppBaseResponseCallBack(Context context) {
        mContext = context;
    }

    public AppBaseResponseCallBack(Context context, boolean needLoading) {
        mContext = context;
        mNeedLoading = needLoading;
        mLoadingView = (Object) new ProgressDialog(context);
    }

    //自定义进度条
    public AppBaseResponseCallBack(Context context, boolean needLoading, Object loadingView) {
        mContext = context;
        mNeedLoading = needLoading;
        mLoadingView = loadingView;
    }

    @Override
    public void onStart() {
        showLoading();
    }

    @Override
    public void onCompleted() {
        dismissLoading();
    }

    @Override
    public void onError(Throwable e) {
        //        作统一错误处理
        dismissLoading();
        if (e != null) {
            if (e.getCode() != ApiErrorConsts.token_invalide) {
                Toast.makeText(mContext, e.getMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }

        }
    }


    private void showLoading() {
        //加载loading图
        if (mNeedLoading && mLoadingView != null) {
            if (mLoadingView instanceof ProgressDialog) {
                ((ProgressDialog) mLoadingView).show();
            }
        }
    }

    private void dismissLoading() {
        if (mNeedLoading && mLoadingView != null) {
            if (mLoadingView instanceof ProgressDialog) {
                if (((ProgressDialog) mLoadingView).isShowing()) {
                    ((ProgressDialog) mLoadingView).dismiss();
                }
            }
        }

    }


}