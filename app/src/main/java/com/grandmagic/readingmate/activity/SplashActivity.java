package com.grandmagic.readingmate.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.response.SplashResponse;
import com.grandmagic.readingmate.consts.ApiInterface;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.hyphenate.chat.EMClient;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.util.Environment;
import com.tamic.novate.util.SPUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

//启动页面
public class SplashActivity extends AppBaseActivity {
    private static final String TAG = "SplashActivity";
    public static final int TYPE_TO_MAIN = 1;
    public static final int TYPE_TO_GUIDE = 2;
    public static final int TYPE_TO_LOGIN = 3;
    public static final int DEFAULT_TIME = 2300;
    @BindView(R.id.activity_splash)
    RelativeLayout mActivitySplash;
    @BindView(R.id.logo)
    ImageView mLogo;
    int mType;//需要跳转的类型
    long start;//计时器的开始时间
    @BindView(R.id.bottomimg)
    ImageView mBottomimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoUtils.setSize(this, false, 750, 1334);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initview();
        checkfrist();
    }

    /**
     * 检测是否第一次使用，是则进入引导页面
     */
    private void checkfrist() {
        boolean mFirst = SPUtils.getInstance().isFirst(this);
        if (mFirst) {
            mType = TYPE_TO_GUIDE;
            canDestroy = true;
        } else {
            checklogin();
        }
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean mBoolean) {

            }
        });
    }

    private void initview() {
        initServerImage();
        ObjectAnimator mScaleX = ObjectAnimator.ofFloat(mLogo, "scaleX", 0, 1f).setDuration(800);
        ObjectAnimator mScaleY = ObjectAnimator.ofFloat(mLogo, "scaleY", 0, 1f).setDuration(800);
        ObjectAnimator mAlpha = ObjectAnimator.ofFloat(mLogo, "alpha", 1f, 0.3f).setDuration(1500);
        ObjectAnimator mTranslationY = ObjectAnimator.ofFloat(mLogo, "y", 0).setDuration(1500);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        mAnimatorSet.play(mScaleX).with(mScaleY);
        mAnimatorSet.play(mTranslationY).with(mAlpha).after(mScaleY);
        mAnimatorSet.start();
        mCountDownTimer.start();
        start = System.currentTimeMillis();
    }

    private void initServerImage() {
        Novate mNovate = new Novate.Builder(this).connectTimeout(2).build();
        mNovate.executeGet(ApiInterface.GET_HOLIDAYPHOTO, new AppBaseResponseCallBack<NovateResponse<SplashResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<SplashResponse> response) {
                Glide.with(SplashActivity.this).load(Environment.BASEULR_PRODUCTION + response.getData().getPhoto())
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                mLogo.setVisibility(View.GONE);
                                mActivitySplash.setBackgroundDrawable(resource);
                                mBottomimg.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                            }

                        });
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 检测登陆状态，未登录则进入登陆，否则进入主页
     */
    boolean canDestroy = false;
    boolean isLogin;

    private void checklogin() {
        isLogin = SPUtils.getInstance().isLogin(this);
        if (isLogin) {
            EMClient.getInstance().groupManager().loadAllGroups();
            EMClient.getInstance().chatManager().loadAllConversations();
//            保存一份联系人信息
            mType = TYPE_TO_MAIN;
            new ContactModel(this).getAllFriendFromServer(new AppBaseResponseCallBack<NovateResponse<List<Contacts>>>(this) {
                @Override
                public void onSuccee(NovateResponse<List<Contacts>> response) {
                    List<Contacts> mData = response.getData();
                    ContactsDao mContactsDao = DBHelper.getContactsDao(SplashActivity.this);
                    for (Contacts mContacts : mData) {
                        mContactsDao.insertOrReplace(mContacts);
                    }
                    DBHelper.close();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    canDestroy = true;
                    if (System.currentTimeMillis() - start > DEFAULT_TIME) {
                        mCountDownTimer.onFinish();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    canDestroy = true;
                    if (System.currentTimeMillis() - start > DEFAULT_TIME) {
                        mCountDownTimer.onFinish();
                    }
                }
            });
        } else {
            mType = TYPE_TO_LOGIN;
            canDestroy = true;
        }

    }

    private void toMain() {
        switch (mType) {
            case TYPE_TO_GUIDE:
                SPUtils.getInstance().putBoolean(this, SPUtils.IS_FIRST, false);
                startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                break;
            case TYPE_TO_LOGIN:
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                break;
            case TYPE_TO_MAIN:
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                break;
        }
        finish();
    }


    CountDownTimer mCountDownTimer = new CountDownTimer(DEFAULT_TIME, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (canDestroy) toMain();
            else {
//                Toast.makeText(SplashActivity.this, "暂不跳转？", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFinish() called but not go Main");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().unregister(this);
    }

}
