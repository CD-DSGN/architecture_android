package com.grandmagic.readingmate.ui;

import android.content.Context;
import android.os.Bundle;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.DataCleanManager;
import com.grandmagic.readingmate.utils.ViewUtils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangmengqi on 2017/3/22.
 */

public class CleanCacheDlg extends TextDlg implements TextDlg.BtnOnclickListener {

    private Context mContext;

    public CleanCacheDlg(Context context) {
        super(context);
        mContext = context;
    }


    public CleanCacheDlg(Context context, int style) {
        super(context, style);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setTitle(mContext.getString(R.string.clean_cache));
        setMessage(mContext.getString(R.string.clean_cache_hint));
        setOnBtnOnclickListener(this);
    }


    @Override
    public void onYesClick() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                DataCleanManager.cleanInternalCache(mContext);
                DataCleanManager.cleanExternalCache(mContext);
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ViewUtils.showToast(mContext.getString(R.string.clean_cache_fail));
                    }

                    @Override
                    public void onNext(Object o) {
                        ViewUtils.showToast(mContext.getString(R.string.clean_cache_finish));
                    }
                });
    }

    @Override
    public void onNoClick() {
        cancel();   //取消对话框
    }
}
