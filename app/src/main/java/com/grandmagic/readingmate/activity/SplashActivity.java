package com.grandmagic.readingmate.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.hyphenate.chat.EMClient;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.util.SPUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

//启动页面
public class SplashActivity extends AppBaseActivity {
    private static final String TAG = "SplashActivity";
    public static final int DEFAULT_TIME = 2000;
    @BindView(R.id.activity_splash)
    RelativeLayout mActivitySplash;
    @BindView(R.id.logo)
    ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoUtils.setSize(this, false, 750, 1334);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initview();
        checkfrist();
        QueryBuilder.LOG_SQL = true;//是否开启打印greendao的一些数据
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 检测是否第一次使用，是则进入引导页面
     */
    private void checkfrist() {
        boolean mFirst = SPUtils.getInstance().isFirst(this);
        if (mFirst) {
            SPUtils.getInstance().putBoolean(this, SPUtils.IS_FIRST, false);
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            finish();
        } else {
            checklogin();
        }
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean mBoolean) {

            }
        });
    }

    /**
     * 检测登陆状态，未登录则进入登陆，否则进入主页
     */
    boolean canDestroy = false;
    boolean mLogin;

    private void checklogin() {
        mLogin = SPUtils.getInstance().isLogin(this);
        if (mLogin) {
//            保存一份联系人信息
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
            canDestroy = true;
        }

    }

    private void toMain() {
        if (mLogin) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    long start;

    private void initview() {
        ObjectAnimator mScaleX = ObjectAnimator.ofFloat(mLogo, "scaleX", 0, 1f);
        ObjectAnimator mScaleY = ObjectAnimator.ofFloat(mLogo, "scaleY", 0, 1f);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(600);
        mAnimatorSet.playTogether(mScaleX, mScaleY);
        mAnimatorSet.start();
        mCountDownTimer.start();
        start = System.currentTimeMillis();
    }

    CountDownTimer mCountDownTimer = new CountDownTimer(DEFAULT_TIME, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (canDestroy) toMain();else {
//                Toast.makeText(SplashActivity.this, "卡住了？", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().unregister(this);
    }
}
