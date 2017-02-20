package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.SPUtils;

//启动页面
public class SplashActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoUtils.setSize(this,false,750,1334);
        setContentView(R.layout.activity_splash);
        initview();
//        checkfrist();
        startActivity(new Intent(SplashActivity.this,SettingActivity.class));
    }

    /**
     * 检测是否第一次使用，是则进入引导页面
     */
    private void checkfrist() {
        boolean mFirst = SPUtils.getInstance().isFirst(this);
        if (mFirst) {
// TODO: 2017/2/7 引导页
            Toast.makeText(this, "首次进入，后面待续", Toast.LENGTH_SHORT).show();
            SPUtils.getInstance().putBoolean(this, SPUtils.IS_FIRST, false);
            checklogin();
        } else {
            checklogin();
        }
    }

    /**
     * 检测登陆状态，未登录则进入登陆，否则进入主页
     */
    private void checklogin() {
        boolean mLogin = SPUtils.getInstance().isLogin(this);
        if (mLogin) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    private void initview() {

    }
}
