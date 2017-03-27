package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.MyCollectBookAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.DisplayBook;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.Page;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.pre_page)
    ImageButton mPrePage;
    @BindView(R.id.next_page)
    ImageButton mNextPage;

    BookModel mModel;
    Page mPage;
    List<DisplayBook.InfoBean> mBookList = new ArrayList<>();
    @BindView(R.id.rv_collect_books)
    RecyclerView mRvCollectBooks;
    private AppBaseResponseCallBack<NovateResponse<DisplayBook>> mCallBack;
    private int mTotalNum = 0;

    private MyCollectBookAdapter mMyCollectBookAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;

    public static final int PAGE_SIZE = 6;
    private int cur_position = 0;

    boolean goToNextPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(true);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        initView();
        loadData();
    }

    private void loadData() {
        mCallBack = new AppBaseResponseCallBack<NovateResponse<DisplayBook>>(this, true) {
            @Override
            public void onSuccee(NovateResponse<DisplayBook> response) {
                DisplayBook displayBook = response.getData();
                if (displayBook != null) {
                    mTotalNum = displayBook.getTotal_num();
                    mPage.total_num = mTotalNum;
                    mPage.more(displayBook.getInfo());
                    mMyCollectBookAdapter.notifyDataSetChanged();
                    //mLoadMoreWrapper.notifyDataSetChanged();
                    if (goToNextPage) {
                        goToNextPage = false;
                        mRvCollectBooks.smoothScrollToPosition(++cur_position);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                goToNextPage = false;
            }
        };

        mModel.loadCollectBook(mPage.cur_page, mCallBack, PAGE_SIZE);
    }

    private void initView() {
        mRvCollectBooks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPage = new Page(mBookList, PAGE_SIZE);
        mTitle.setText(R.string.collect);
        mModel = new BookModel(this);
        if (mMyCollectBookAdapter == null) {
            mMyCollectBookAdapter = new MyCollectBookAdapter(this, mBookList);
        }
        //mLoadMoreWrapper = new LoadMoreWrapper(mMyCollectBookAdapter);
        mRvCollectBooks.setAdapter(mMyCollectBookAdapter);
//        mLoadMoreWrapper.setLoadMoreView(R.layout.view_bookdetail);
//        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                mModel.loadCollectBook(mPage.cur_page, mCallBack, PAGE_SIZE);
//            }
//        });

        mRvCollectBooks.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {

                        if (cur_position == mBookList.size() - 1) {  //现有的最后一页向右滑
                            if (mPage.hasMore()) {
                                mModel.loadCollectBook(mPage.cur_page, mCallBack, PAGE_SIZE);
                                goToNextPage = true;
                            }else{
                                ViewUtils.showToast(getString(R.string.no_more));
                            }
                        }else{
                            mRvCollectBooks.smoothScrollToPosition(++cur_position);
                        }
                    }else{
                        if (cur_position > 0) {
                            mRvCollectBooks.smoothScrollToPosition(--cur_position);
                        }
                    }


                }

                return false;
            }
        });


    }

    @OnClick({R.id.back, R.id.title, R.id.pre_page, R.id.next_page})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                break;
            case R.id.pre_page:
                //                int pre = mVpCollectBooks.getCurrentItem() - 1;
                //                if (pre >= 0) {
                //                    mVpCollectBooks.setCurrentItem(pre);
                //                } else {
                //                    ViewUtils.showToast("当前为第一页");
                //                }
                break;
            case R.id.next_page:
                //                int next = mVpCollectBooks.getCurrentItem() + 1;
                //                if (next >= 0) {
                //                    mVpCollectBooks.setCurrentItem(next);
                //                } else {
                //                    ViewUtils.showToast("当前为最后一页");
                //                }
                break;
            default:
                break;
        }
    }


}
