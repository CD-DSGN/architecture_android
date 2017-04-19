package com.grandmagic.readingmate.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import com.grandmagic.readingmate.event.BookStateEvent;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.orhanobut.logger.Logger;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;


public class HomeFragment extends AppBaseFragment implements HomeBookAdapter.ClickListener {
    protected Context mContext;
    public static final int REQUEST_CAPTURE = 101;
    public static final int REQUEST_BOOKDETAIL = 102;

    @BindView(R.id.recyclerview)
    PullLoadMoreRecyclerView mRecyclerview;

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
    @BindView(R.id.homeview_book)
    CoordinatorLayout mHomeviewBook;
    @BindView(R.id.appbarlayout)
    AppBarLayout mAppBarLayout;
    private View rootview;
    boolean isEmpty = false;
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
        EventBus.getDefault().register(this);
        initview();
        initdata();
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
//        setSystemBarColor(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initview() {
        mRecyclerview.setLinearLayout();
        mBookAdapter = new HomeBookAdapter(mContext, mBookList, mRecyclerview.getRecyclerView());
        mBookAdapter.setClickListener(this);
        mRecyclerview.setAdapter(mBookAdapter);
        initRefresh();
    }

    BookModel mModel;
    int pagecount = 1, currpage = 1;

    private void initdata() {
        mRecyclerview.setRefreshing(true);
        mModel.loadCollectBook(currpage, new AppBaseResponseCallBack<NovateResponse<DisplayBook>>(getActivity(), false) {
            @Override
            public void onSuccee(NovateResponse<DisplayBook> response) {
                Logger.e("首页加载成功");
                if (response.getData().getNum() == 0) {
                    showEmptyView();
                    isEmpty = true;
                    return;
                }

                mTotal_num = response.getData().getTotal_num();
                pagenum = response.getData().getNum();
                pagecount = response.getData().getpage();
                mBookList.clear();
                if (response.getData().getInfo() != null && !response.getData().getInfo().isEmpty())
                    mBookList.addAll(response.getData().getInfo());
                mBookAdapter.setData(mBookList);
                isEmpty = false;
                showRecyclerView();
                mRecyclerview.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                isEmpty = true;
                showEmptyView();
                mRecyclerview.setRefreshing(false);
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
        mRecyclerview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                currpage = 1;
                loadBook(currpage);
            }

            @Override
            public void onLoadMore() {
                if (currpage < pagecount) {
                    currpage++;
                    loadBook(currpage);
                } else {
                    mRecyclerview.setPullLoadMoreCompleted();
                }
            }
        });
    }

    int mTotal_num, pagenum;//#mTotal_num 书的总数，#pagenum第一页的条数

    private void loadBook(final int mCurrpage) {
        mModel.loadCollectBook(mCurrpage, new AppBaseResponseCallBack<NovateResponse<DisplayBook>>(getActivity(), false) {

            @Override
            public void onSuccee(NovateResponse<DisplayBook> response) {
                if (mCurrpage == 1) mBookList.clear();
                mBookList.addAll(response.getData().getInfo());
                mBookAdapter.setData(mBookList);
                mRecyclerview.setPullLoadMoreCompleted();


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                currpage--;//如果加载失败将页码还原
                mRecyclerview.setPullLoadMoreCompleted();
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
        mPopupWindow.setBookData(bookInfo.getBook_name(), bookInfo.getBook_id(),
                bookInfo.getSynopsis(), bookInfo.getPhoto(), bookInfo.getTotal_score());

        mPopupWindow.show();


    }

    @Override
    public void onItemClickListener(int position) {
// TODO: 2017/2/16 跳转到详情
        Intent mIntent = new Intent(getActivity(), BookDetailActivity.class);
        mIntent.putExtra(BookDetailActivity.BOOK_ID, mBookList.get(position).getBook_id());
        startActivityForResult(mIntent, REQUEST_BOOKDETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && Activity.RESULT_OK == resultCode) {
            if (data == null) return;
            String book_id = data.getStringExtra("result");
            Intent mIntent = new Intent(getActivity(), BookDetailActivity.class);
            mIntent.putExtra(BookDetailActivity.BOOK_ID, book_id);
            startActivityForResult(mIntent, REQUEST_BOOKDETAIL);
        }
        else if (requestCode == REQUEST_BOOKDETAIL) {
//如果从图书详情页需要返回做什么处理。可以在这里处理,暂时是重新加载书籍
//            mBookList.clear();
//            initdata();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        setSystemBarColor(hidden);
    }

    private void setSystemBarColor(boolean hidden) {
        if (!hidden)
            ((MainActivity) mContext).setSystemBarColor(isEmpty ? R.color.text_green : android.R.color.white);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void BookStateCHange(BookStateEvent mStateEvent) {
        // TODO: 2017/4/14
        if (mStateEvent.getState() == BookStateEvent.STATE_MOVE) {
            currpage = 1;
            pagecount = 1;
            initdata();
        } else if (mStateEvent.getState() == BookStateEvent.STATE_DELETE) {
            try {
                pagecount = (int) Math.ceil(mTotal_num / pagenum);//被删除书籍的时候重新计算页码
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (DisplayBook.InfoBean mBook : mBookList) {
                if (mStateEvent.getBookid().equals(mBook.getBook_id())) {
                    mBookList.remove(mBook);
                    mBookAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
