package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.AddFriendActivity;
import com.grandmagic.readingmate.activity.FriendActivity;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.adapter.RecentConversationDelagate;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.bean.response.RecentConversation;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.SwipRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends AppBaseFragment {

    Context mContext;
    @BindView(R.id.tv_friend)
    TextView mTvFriend;
    @BindView(R.id.iv_friend)
    ImageView mIvFriend;
    @BindView(R.id.rela_friend)
    RelativeLayout mRelaFriend;
    @BindView(R.id.recyclerview)
    SwipRecycleView mRecyclerview;//最近会话列表


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        AutoUtils.auto(view);
        ButterKnife.bind(this, view);
        initview();
        return view;
    }

    MultiItemTypeAdapter mAdapter;
    List<RecentConversation> mConversations;

    private void initview() {
        mContext = getActivity();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        inittestData();
        mAdapter = new MultiItemTypeAdapter(mContext, mConversations);//为了以后也许会加入群组会话等，使用MultiItemTypeAdapter，便于扩展
        mAdapter.addItemViewDelegate(new RecentConversationDelagate(mContext));
        mRecyclerview.setAdapter(mAdapter);
    }

    /**
     * 创建测试数据
     */
    private void inittestData() {
        mConversations = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            RecentConversation mConversation = new RecentConversation();
            mConversation.setName("测试会话" + i);
            mConversation.setContent("测试内容" + i);
            mConversation.setTime("上午10点");
            mConversation.setAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1824807693,4159709632&fm=21&gp=0.jpg");
            mConversations.add(mConversation);
        }
    }



    @OnClick({R.id.add_friend, R.id.rela_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_friend:
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
                break;
            case R.id.rela_friend:
                startActivity(new Intent(getActivity(), FriendActivity.class));
                break;
        }
    }
}
