package com.grandmagic.readingmate.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.BookDetailActivity;
import com.grandmagic.readingmate.activity.CaptureActivity;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.activity.SearchActivity;
import com.grandmagic.readingmate.adapter.HomeBookAdapter;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.DisplayBook;
import com.grandmagic.readingmate.dialog.HintDialog;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.permission.CameraPermission;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.tamic.novate.Novate;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;
import rx.functions.Action1;


public class HomeFragment extends AppBaseFragment implements HomeBookAdapter.ClickListener {
    protected Context mContext;
    public static final int REQUEST_CAPTURE = 101;
    public static final int REQUEST_BOOKDETAIL = 102;

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
    List<DisplayBook.InfoBean> mBookList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_home, container, false);
        AutoUtils.auto(rootview);
        ButterKnife.bind(this, rootview);
        mContext = getActivity();
        initdata();
        return rootview;
    }

    BookModel mModel;

    private void initdata() {
        mBookList = new ArrayList();
        mModel = new BookModel(getActivity());
        mModel.loadCollectBook(new AppBaseResponseCallBack<NovateResponse<DisplayBook>>(getActivity()) {
            @Override
            public void onSuccee(NovateResponse<DisplayBook> response) {
                mBookList.addAll(response.getData().getInfo());
                showRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
               super.onError(e);
                showEmptyView();
            }
        });
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
        ((MainActivity) getActivity()).setSystemBarColor(R.color.text_green);
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
        mTvTitle.setTextColor(mContext.getResources().getColor(R.color.text_green));
        mRecyclerview.setVisibility(View.VISIBLE);
        mLayoutNobook.setVisibility(View.GONE);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBookAdapter = new HomeBookAdapter(mContext, mBookList, mRecyclerview);
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
                scanBook();
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

    private void scanBook() {
        new RxPermissions(getActivity()).request(Manifest.permission.CAMERA).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean b) {
                if (b) {
                    startActivityForResult(new Intent(getActivity(), CaptureActivity.class), REQUEST_CAPTURE);
                } else {
                    Toast.makeText(mContext, "你没有给予权限。无法打开相机", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PopupWindow mPopupWindow;

    @Override
    public void bookShare(int position) {
        // TODO: 2017/2/16 分享
        if (mPopupWindow == null) {
            View mpopview = LayoutInflater.from(mContext).inflate(R.layout.view_sharepop, null);
            AutoUtils.auto(mpopview);
            mPopupWindow = new PopupWindow(mpopview, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setClippingEnabled(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                    params.alpha = 1.0f;
                    getActivity().getWindow().setAttributes(params);
                }
            });
        }
        mPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.7f;
        getActivity().getWindow().setAttributes(params);
    }

    @Override
    public void onItemClickListener(int position) {
// TODO: 2017/2/16 跳转到详情
        new HintDialog(mContext, "详情页依然还没写");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && Activity.RESULT_OK == resultCode) {
            if (data == null) return;
            String isbn_code = data.getStringExtra("result");
            Intent mIntent = new Intent(getActivity(), BookDetailActivity.class);
            mIntent.putExtra(BookDetailActivity.ISBN_CODE, isbn_code);
            startActivityForResult(mIntent, REQUEST_BOOKDETAIL);
        } else if (requestCode == REQUEST_BOOKDETAIL && requestCode == Activity.RESULT_OK) {
//如果从图书详情页需要返回做什么处理。可以在这里处理
        }
    }
}
