package com.grandmagic.readingmate.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
    }

    private void initview() {
        mTitle.setText(R.string.read_friend);
        mTitle.setTextColor(Color.parseColor("#181e1d"));
    }

    @OnClick({R.id.back, R.id.title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.title:
                break;
        }
    }
}
