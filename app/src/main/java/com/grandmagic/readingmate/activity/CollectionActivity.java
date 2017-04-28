package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.MyCollectBookAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.DisplayBook;
import com.grandmagic.readingmate.event.BookStateEvent;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.Page;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.fl_search)
    FrameLayout mFlSearch;
    @BindView(R.id.activity_collection)
    RelativeLayout mActivityCollection;
    private AppBaseResponseCallBack<NovateResponse<DisplayBook>> mCallBack;
    private int mTotalNum = 0;


    private MyCollectBookAdapter mMyCollectBookAdapter;
    private DefaultEmptyAdapter mDefaultEmptyAdapter;


    public static final int PAGE_SIZE = 6;
    private int cur_position = 0;

    boolean mGoToNextPage = false;
    private MyCollectBookAdapter.OnitemDeleteListener mOnitemDeleteListener;
    private String mDeleteBookID = "-1";

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
        mCallBack = new AppBaseResponseCallBack<NovateResponse<DisplayBook>>(this, false) {
            @Override
            public void onSuccee(NovateResponse<DisplayBook> response) {
                mDefaultEmptyAdapter.refresh();
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
                    mTvPageInfo.setText((cur_position + 1) + "/" + mPage.total_num);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mDefaultEmptyAdapter.refresh();
                mGoToNextPage = false;
            }
        };

        mModel.loadCollectBook(mPage.cur_page, mCallBack, PAGE_SIZE);
    }

    private void initView() {

        if (mOnitemDeleteListener == null) {
            mOnitemDeleteListener= new MyCollectBookAdapter.OnitemDeleteListener() {
                @Override
                public void deleteItem(String id) {
                    mDeleteBookID = id;
                    mModel.deleteCollectBook(id, new AppBaseResponseCallBack<NovateResponse<Object>>(CollectionActivity.this, true) {
                        @Override
                        public void onSuccee(NovateResponse<Object> response) {
                            //删除图书成功，更改本地数据,更新分页信息
                            EventBus.getDefault().post(new BookStateEvent(BookStateEvent.STATE_DELETE, mDeleteBookID));
                            DisplayBook.InfoBean infoBean;
                            for (int i = 0 ; i < mBookList.size(); i++) {
                                infoBean = mBookList.get(i);
                                if (infoBean != null) {
                                    if (infoBean.getBook_id().equals(mDeleteBookID)) {
                                        if (i == mBookList.size() - 1 && cur_position!=0) {  //如果删的是现有数据的最后一页
                                            cur_position --;
                                        }
                                        mPage.delete(i);
//                                        mMyCollectBookAdapter.notifyItemRemoved(i);
//                                        mMyCollectBookAdapter.notifyran

                                        mDefaultEmptyAdapter.notifyDataSetChanged();
                                        CollectionActivity.this.smoothScrollToPosition(cur_position);
                                        break;
                                    }

                                }
                            }
                        }
                    });


                }
            };
        }
        mRvCollectBooks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPage = new Page(mBookList, PAGE_SIZE);
        mTitle.setText(R.string.collect);
        mModel = new BookModel(this);
        if (mMyCollectBookAdapter == null) {
            mMyCollectBookAdapter = new MyCollectBookAdapter(this, mBookList, mOnitemDeleteListener);
        }

        if (mDefaultEmptyAdapter == null) {
            mDefaultEmptyAdapter = new DefaultEmptyAdapter(mMyCollectBookAdapter, this);
            mDefaultEmptyAdapter.setEmptyViewTextview(this.getString(R.string.no_collect_book));
        }

        mRvCollectBooks.setAdapter(mDefaultEmptyAdapter);

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
        mMyCollectBookAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转图书详情
                String book_id = mBookList.get(position).getBook_id();
                Intent intent = new Intent(CollectionActivity.this, BookDetailActivity.class);
                intent.putExtra(BookDetailActivity.BOOK_ID, book_id);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }

    private void smoothScrollToPosition(int position) {
        mRvCollectBooks.smoothScrollToPosition(position);
        position++; //显示从1开始，程序从0开始
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

    @OnClick({R.id.back, R.id.title, R.id.pre_page, R.id.next_page, R.id.fl_search})
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
            case R.id.fl_search:
                //跳转个人收藏搜索页
                Intent intent = new Intent(this, CollectBookSearchActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMyCollectBookAdapter != null && mMyCollectBookAdapter.mSharePopUpWindow != null) {
            mMyCollectBookAdapter.mSharePopUpWindow.dismissPorgressDlg();
        }
    }
}
