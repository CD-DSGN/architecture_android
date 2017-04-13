package com.grandmagic.readingmate.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.grandmagic.readingmate.consts.AppConsts;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.orhanobut.logger.Logger;
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
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.homeview_book)
    CoordinatorLayout mHomeviewBook;
    @BindView(R.id.appbarlayout)
    AppBarLayout mAppBarLayout;
    private View rootview;
    boolean isEmpty = false;
    BGAStickinessRefreshViewHolder mRefreshViewHolder;
    List<DisplayBook.InfoBean> mBookList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_home, container, false);
        AutoUtils.auto(rootview);
        ButterKnife.bind(this, rootview);
        mContext = getActivity();
        mModel = new BookModel(getActivity());
        initview();
        initdata();
        return rootview;
    }

    private void initview() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mBookAdapter = new HomeBookAdapter(mContext, mBookList, mRecyclerview);
        mBookAdapter.setClickListener(this);
        mRecyclerview.setAdapter(mBookAdapter);
        initRefresh();
    }

    BookModel mModel;
    int pagecount = 1, currpage = 1;

    private void initdata() {
        mModel.loadCollectBook(currpage, new AppBaseResponseCallBack<NovateResponse<DisplayBook>>(getActivity(), false) {
            @Override
            public void onSuccee(NovateResponse<DisplayBook> response) {
                Logger.e("首页加载成功");
                if (response.getData().getNum() == 0) {
                    showEmptyView();
                    isEmpty=true;
                    return;
                }
                mBookList.addAll(response.getData().getInfo());
                pagecount = response.getData().getpage();
                mBookAdapter.setData(mBookList);
                isEmpty = false;
                showRecyclerView();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                isEmpty = true;
                showEmptyView();

            }
        });
    }

    /**
     * 显示没有书的时候的页面
     */
    private void showEmptyView() {
        mLayoutNobook.setVisibility(View.VISIBLE);
        mHomeviewBook.setVisibility(View.GONE);
        setSystemBarColor(false);
    }

    /**
     * 有书展示的
     */
    HomeBookAdapter mBookAdapter;

    private void showRecyclerView() {
        mLayoutNobook.setVisibility(View.GONE);
        mHomeviewBook.setVisibility(View.VISIBLE);
        setSystemBarColor(false);

    }

    private void initRefresh() {
        mRefreshViewHolder = new BGAStickinessRefreshViewHolder(mContext, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);
//        mRefreshLayout.offsetTopAndBottom(88);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                currpage = 1;
                loadBook(currpage);

            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if (currpage < pagecount) {
                    currpage++;
                    loadBook(currpage);
                    return true;
                } else {
                    Toast.makeText(mContext, "NOMORE", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //随时处理refresh是否拦截下拉事件。当appbarlayout是展开的时候才允许它进行拦截
                mRefreshLayout.setPullDownRefreshEnable(verticalOffset>=0);
            }
        });
    }

    private void loadBook(final int mCurrpage) {
        mModel.loadCollectBook(mCurrpage,new AppBaseResponseCallBack<NovateResponse<DisplayBook>>(getActivity(), false) {

            @Override
            public void onSuccee(NovateResponse<DisplayBook> response) {
                if (mCurrpage==1)mBookList.clear();
                mRefreshLayout.endLoadingMore();
                mRefreshLayout.endRefreshing();
                mBookList.addAll(response.getData().getInfo());
                mBookAdapter.setData(mBookList);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                currpage--;//如果加载失败将页码还原
                mRefreshLayout.endLoadingMore();
                mRefreshLayout.endRefreshing();
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

    SharePopUpWindow mPopupWindow;

    @Override
    public void bookShare(int position) {
        // TODO: 2017/2/16 分享
        if (mPopupWindow == null) {
            mPopupWindow = new SharePopUpWindow(mContext);
        }

        DisplayBook.InfoBean bookInfo = mBookList.get(position);
        if (!TextUtils.isEmpty(bookInfo.getPhoto())) {
            mPopupWindow.setData(mContext.getString(R.string.app_name)+":" + bookInfo.getBook_name(), bookInfo.getSynopsis(),
                    KitUtils.getAbsoluteUrl(bookInfo.getPhoto()), AppConsts.APP_URL, "");
        }else{
            mPopupWindow.setData(mContext.getString(R.string.app_name)+":" + bookInfo.getBook_name(), bookInfo.getSynopsis(),
                    R.drawable.iv_no_book, AppConsts.APP_URL, "");
        }
        mPopupWindow.show();




    }

    @Override
    public void onItemClickListener(int position) {
// TODO: 2017/2/16 跳转到详情
        Intent mIntent = new Intent(getActivity(), BookDetailActivity.class);
        mIntent.putExtra(BookDetailActivity.BOOK_ID, mBookList.get(position).getBook_id());
        startActivityForResult(mIntent,REQUEST_BOOKDETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && Activity.RESULT_OK == resultCode) {
            if (data == null) return;
            String book_id = data.getStringExtra("result");
            Intent mIntent = new Intent(getActivity(), BookDetailActivity.class);
            mIntent.putExtra(BookDetailActivity.BOOK_ID, book_id);
            startActivityForResult(mIntent, REQUEST_BOOKDETAIL);
        } else if (requestCode == REQUEST_BOOKDETAIL ) {
//如果从图书详情页需要返回做什么处理。可以在这里处理,暂时是重新加载书籍
            mBookList.clear();
            initdata();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        setSystemBarColor(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        setSystemBarColor(hidden);
    }

    private void setSystemBarColor(boolean hidden) {
        if (!hidden)
            ((MainActivity) mContext).setSystemBarColor(isEmpty ? R.color.text_green : android.R.color.white);
    }
}
