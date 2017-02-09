package com.grandmagic.readingmate.activity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.LoginPagerAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.dialog.HintDialog;
import com.grandmagic.readingmate.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

//登录
public class LoginActivity extends AppBaseActivity {


    ViewPager mViewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AutoUtils.auto(this);
        initview();
    }

    List<View> mViews = new ArrayList<>();

    private void initview() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        View mview_register = LayoutInflater.from(this).inflate(R.layout.view_register, null);
        View mview_login = LayoutInflater.from(this).inflate(R.layout.view_login, null);


        AutoUtils.auto(mview_login);
        mViews.add(mview_register);
        mViews.add(mview_login);
        mViewpager.setAdapter(new LoginPagerAdapter(mViews));
       new HintDialog(this, "密码错误，请重试");
    }

}
