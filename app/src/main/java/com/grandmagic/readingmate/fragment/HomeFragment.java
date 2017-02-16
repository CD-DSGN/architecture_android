package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CaptureActivity;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.activity.SearchActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends AppBaseFragment {
    protected Context mContext;

    PopupWindow mPopupWindow;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.pop_scan)
    TextView mPopScan;
    @BindView(R.id.pop_search)
    TextView mPopSearch;
    @BindView(R.id.layout_nobook)
    FrameLayout mLayoutNobook;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.iv_camera)
    ImageView mIvCamera;
    private View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_home, container, false);
        AutoUtils.auto(rootview);
        ButterKnife.bind(this, rootview);
        mContext = getActivity();
        showRecyclerView();
//        showEmptyView();
        return rootview;
    }

    private void showEmptyView() {
        mRecyclerview.setVisibility(View.GONE);
        mLayoutNobook.setVisibility(View.VISIBLE);
        rootview.setBackgroundResource(R.drawable.bg_app_deep);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.white));
        mIvCamera.setVisibility(View.GONE);
        mIvSearch.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setSystemBarColor(R.color.bg);
    }

    private void showRecyclerView() {
        ((MainActivity) mContext).setSystemBarColor(R.color.white);
        mIvCamera.setVisibility(View.VISIBLE);
        mIvSearch.setVisibility(View.VISIBLE);
        rootview.setBackgroundColor(0xff0000);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.bg));
        mRecyclerview.setVisibility(View.VISIBLE);
        mLayoutNobook.setVisibility(View.GONE);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        List<String> mStrings = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            mStrings.add(i + "helloworld");
        }
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setAdapter(new CommonAdapter<String>(mContext, R.layout.item_homepage, mStrings) {
            @Override
            public void onViewHolderCreated(ViewHolder holder, View itemView) {
              AutoUtils.auto(itemView);
            }

            @Override
            protected void convert(ViewHolder holder, String mS, int position) {
                Glide.with(mContext).load("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1937046046,1905495319&fm=58")
                        .into((ImageView) holder.getView(R.id.iv_conver));
                Glide.with(mContext).load("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1460995985,2991423940&fm=58")
                        .into((ImageView) holder.getView(R.id.avatar));
            }
        });
    }

    @OnClick({R.id.pop_scan, R.id.pop_search, R.id.tv_title,R.id.iv_camera,R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_camera:
            case R.id.pop_scan:
                startActivity(new Intent(getActivity(), CaptureActivity.class));
                break;
            case R.id.iv_search:
            case R.id.pop_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.tv_title:
                if (mRecyclerview.getVisibility() == View.GONE) {
                    showRecyclerView();
                } else {
                    showEmptyView();
                }
                break;


        }
    }
}
