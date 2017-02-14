package com.grandmagic.readingmate.activity;

import android.app.Activity;
import android.os.Bundle;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;

//搜索页面
public class SearchActivity extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTranslucentStatus(true);
        AutoUtils.auto(this);
    }
}
