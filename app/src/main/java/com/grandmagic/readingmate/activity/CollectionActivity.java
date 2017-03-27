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
    @BindView(R.id.tv_page_info)
    TextView mTvPageInfo;
    private AppBaseResponseCallBack<NovateResponse<DisplayBook>> mCallBack;
    private int mTotalNum = 0;

    private MyCollectBookAdapter mMyCollectBookAdapter;

    public static final int PAGE_SIZE = 6;
    private int cur_position = 0;

    boolean mGoToNextPage = false;

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
                    if (mGoToNextPage) {
                        mGoToNextPage = false;
                        goToNextPage();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mGoToNextPage = false;
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

        mRvCollectBooks.setAdapter(mMyCollectBookAdapter);

        mRvCollectBooks.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        goToNextPage();
                    } else {
                        goToPrePage();
                    }
                }
                return false;
            }
        });


    }

    private void smoothScrollToPosition(int position) {
        position++; //显示从1开始，程序从0开始
        mRvCollectBooks.smoothScrollToPosition(position);
        mTvPageInfo.setText(position + "/" + mPage.total_num);
    }



    private void goToPrePage() {
        if (cur_position > 0) {
            smoothScrollToPosition(--cur_position);
        } else {
            ViewUtils.showToast(getString(R.string.no_pre_page));
        }
    }

    private void goToNextPage() {
        if (cur_position == mBookList.size() - 1) {  //现有的最后一页向右滑
            if (mPage.hasMore()) {
                mModel.loadCollectBook(mPage.cur_page, mCallBack, PAGE_SIZE);
                mGoToNextPage = true;
            } else {
                ViewUtils.showToast(getString(R.string.no_more));
            }
        } else {
            smoothScrollToPosition(++cur_position);
        }
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
                goToPrePage();
                break;
            case R.id.next_page:
                goToNextPage();
                break;
            default:
                break;
        }
    }


}
