package com.grandmagic.readingmate.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.grandmagic.readingmate.consts.ApiErrorConsts;
import com.grandmagic.readingmate.consts.AppConsts;
import com.grandmagic.readingmate.event.ConnectStateEvent;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.hyphenate.EMError;
import com.orhanobut.logger.Logger;
import com.tamic.novate.Novate;
import com.tamic.novate.Throwable;
import com.tamic.novate.exception.NovateException;
import com.tamic.novate.exception.ServerException;
import com.tamic.novate.util.Environment;
import com.tamic.novate.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;

import retrofit2.adapter.rxjava.HttpException;

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

//        ProgressDialog pd = new ProgressDialog(context);
//        pd.setProgressStyle(R.style.loading); 准备修改样式
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
        java.lang.Throwable throwable = e.getThrowable();
        if (e != null) {
            if (e.getCode() == ApiErrorConsts.token_invalid || e.getCode() == NovateException.UNAUTHORIZED) {
                SPUtils.getInstance().putString(mContext,SPUtils.IM_STATE, EMError.USER_LOGIN_ANOTHER_DEVICE+"");
                EventBus.getDefault().post(new ConnectStateEvent(EMError.USER_LOGIN_ANOTHER_DEVICE));
            } else if (throwable instanceof HttpException
                    || throwable instanceof java.net.SocketTimeoutException
                    || throwable instanceof ConnectException
                    || throwable instanceof  javax.net.ssl.SSLHandshakeException
                    || throwable instanceof  java.security.cert.CertPathValidatorException) {
                    ViewUtils.showToast(e.getMessage() + "");
            } else if (throwable instanceof ServerException) {
                for (int code : AppConsts.show_error_msg) {
                    if (e.getCode() == code) {
                        ViewUtils.showToast(e.getMessage() + "");
                        break;
                    }
                }
            } else {
                if (Environment.getEnviroment() != Environment.ENVIRONMENT_PRODUCTION) {
                    ViewUtils.showToast(e.getMessage() + "");
                    Logger.e(e.getMessage());
                }
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
