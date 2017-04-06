package com.grandmagic.readingmate.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.grandmagic.readingmate.activity.LoginActivity;
import com.grandmagic.readingmate.consts.ApiErrorConsts;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.exception.NovateException;

/**
 * Created by zhangmengqi on 2017/2/9.
 */

public abstract class AppBaseResponseCallBack<T> implements Novate.ResponseCallBack<T> {
    private Context mContext;
    private boolean mNeedLoading = false;  //需不需要loading
    private Object mLoadingView = null;

    public boolean isRefresh = false;
    public boolean mLoadingOnce = false;

    public AppBaseResponseCallBack(Context context) {
        mContext = context;
    }

    public AppBaseResponseCallBack(Context context, boolean needLoading) {
        mContext = context;
        mNeedLoading = needLoading;

        mLoadingView = (Object) new ProgressDialog(context);
    }

    public AppBaseResponseCallBack(Context context, boolean needLoading, boolean loadingOnce) {
        mContext = context;
        mNeedLoading = needLoading;
        mLoadingOnce = loadingOnce;
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
        if (mLoadingOnce) {
            mNeedLoading = false;
        }
    }

    @Override
    public void onError(Throwable e) {
        //        作统一错误处理
        Logger.e("Novate Error"+e.getMessage());
        dismissLoading();
        if (e != null) {
            if (e.getCode() != ApiErrorConsts.token_invalid && e.getCode() != NovateException.UNAUTHORIZED) {
                ViewUtils.showToast(e.getMessage() + "");
                Logger.e(e.getMessage());
            } else {
                EMClient.getInstance().logout(true);//环信的退出
                Intent intent = new Intent(mContext, LoginActivity.class);
                Logger.e(e.getMessage()+mContext);
                mContext.startActivity(intent);
            }

        }
        if (mLoadingOnce) {
            mNeedLoading = false;
        }
    }


    private void showLoading() {
        //加载loading图
        if (mNeedLoading && mLoadingView != null && !((Activity)mContext).isFinishing()) {
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
