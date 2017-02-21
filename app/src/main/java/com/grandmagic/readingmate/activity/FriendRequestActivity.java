package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.RequestListAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.bean.response.FriendRequestBean;
import com.grandmagic.readingmate.view.SwipRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendRequestActivity extends AppBaseActivity {
Context mContext;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    List<FriendRequestBean> mRequestBean = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        inittestData();
        initview();
    }
    RequestListAdapter mAdapter;
    private void initview() {
        mContext=this;
        mTitle.setText("新阅友");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter=new RequestListAdapter(mContext,mRequestBean);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void inittestData() {
        for (int i = 0; i < 8; i++) {
            FriendRequestBean mFriendRequestBean = new FriendRequestBean();
            mFriendRequestBean.setAvatar("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=34014521,1996222662&fm=21&gp=0.jpg");
            mFriendRequestBean.setName("好友请求" + i);
            if (i == 3) {
                mFriendRequestBean.setState(1);
            } else {
                mFriendRequestBean.setState(0);
            }
            mRequestBean.add(mFriendRequestBean);
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
