package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.CaptureActivity;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.activity.SearchActivity;
import com.grandmagic.readingmate.adapter.HomeBookAdapter;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.dialog.HintDialog;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;


public class HomeFragment extends AppBaseFragment implements HomeBookAdapter.ClickListener {
    protected Context mContext;


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
    @BindView(R.id.rela_title)
    RelativeLayout mRelaTitle;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
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

    /**
     * 显示没有书的时候的页面
     */
    private void showEmptyView() {
        mRelaTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mRecyclerview.setVisibility(View.GONE);
        mLayoutNobook.setVisibility(View.VISIBLE);
        rootview.setBackgroundResource(R.drawable.bg_app_deep);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.white));
        mIvCamera.setVisibility(View.GONE);
        mIvSearch.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setSystemBarColor(R.color.bg);
    }

    /**
     * 有书展示的
     */
    HomeBookAdapter mBookAdapter;

    private void showRecyclerView() {
        mRelaTitle.setBackgroundColor(Color.WHITE);
        ((MainActivity) mContext).setSystemBarColor(R.color.white);
        mIvCamera.setVisibility(View.VISIBLE);
        mIvSearch.setVisibility(View.VISIBLE);
        rootview.setBackgroundColor(0xf8f8f8);
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.bg));
        mRecyclerview.setVisibility(View.VISIBLE);
        mLayoutNobook.setVisibility(View.GONE);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        List<String> mStrings = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            mStrings.add(i + "helloworld");
        }
        mBookAdapter = new HomeBookAdapter(mContext, mStrings, mRecyclerview);
        mBookAdapter.setClickListener(this);
        mRecyclerview.setAdapter(mBookAdapter);

        BGAStickinessRefreshViewHolder mRefreshViewHolder = new BGAStickinessRefreshViewHolder(mContext, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);
//        mRefreshLayout.offsetTopAndBottom(88);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endRefreshing();
                    }
                }, 2000);

            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endLoadingMore();
                    }
                }, 2000);
                return true;
            }
        });
    }

    @OnClick({R.id.pop_scan, R.id.pop_search, R.id.tv_title, R.id.iv_camera, R.id.iv_search})
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

    PopupWindow mPopupWindow;

    @Override
    public void bookShare(int position) {
        // TODO: 2017/2/16 分享
        if (mPopupWindow == null) {
            View mpopview = LayoutInflater.from(mContext).inflate(R.layout.view_sharepop, null);
            AutoUtils.auto(mpopview);
            mPopupWindow = new PopupWindow(mpopview, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setClippingEnabled(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
                    params.alpha=1.0f;
                    getActivity().getWindow().setAttributes(params);
                }
            });
        }
        mPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
        params.alpha=0.7f;
        getActivity().getWindow().setAttributes(params);
    }

    @Override
    public void onItemClickListener(int position) {
// TODO: 2017/2/16 跳转到详情
        new HintDialog(mContext, "详情页依然还没写");
    }
}
