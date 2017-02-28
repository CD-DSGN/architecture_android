package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommentsAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendDetailActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.recommend)
    ImageView mRecommend;
    @BindView(R.id.gender)
    ImageView mGender;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.iv_coll_1)
    ImageView mIvColl1;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        try {
            getSupportActionBar().hide();
        } catch (Exception mE) {
            mE.printStackTrace();
        }
        initview();
    }

    CommentsAdapter mAdapter;
    List<String> mStrings = new ArrayList<>();

    private void initview() {
        mTitle.setText("详细信息");
        ImageLoader.loadCircleImage(this, "http://pic.ytqmx.com:82/2014/0831/01/11.jpg!960.jpg"
                , mAvatar);
        ImageLoader.loadImage(this, "http://pic.ytqmx.com:82/2014/0831/01/11.jpg!960.jpg"
                , mIvColl1);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < 50; i++) {
            mStrings.add("af");
        }
        mAdapter = new CommentsAdapter(this, mStrings);
        mRecyclerview.setAdapter(mAdapter);
    }
}
