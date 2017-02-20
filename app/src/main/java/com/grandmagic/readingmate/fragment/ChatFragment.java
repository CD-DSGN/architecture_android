package com.grandmagic.readingmate.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.FriendActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends AppBaseFragment {


    @BindView(R.id.tv_friend)
    TextView mTvFriend;
    @BindView(R.id.iv_friend)
    ImageView mIvFriend;
    @BindView(R.id.rela_friend)
    RelativeLayout mRelaFriend;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        AutoUtils.auto(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.rela_friend)
    public void onClick() {
        startActivity(new Intent(getActivity(), FriendActivity.class));
    }
}
