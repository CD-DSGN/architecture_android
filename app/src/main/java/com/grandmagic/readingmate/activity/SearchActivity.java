package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.SearchBookAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.BookSearchResponse;
import com.grandmagic.readingmate.bean.response.HotWordResponse;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.FlowLayout;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


//搜索页面
public class SearchActivity extends AppBaseActivity {
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_hotSearch)
    TextView mTextView;
    @BindView(R.id.hotSearch_Flow)
    FlowLayout mHotSearchFlow;
    @BindView(R.id.iv_scanlist)
    ImageView mIvScanlist;
    BookModel mModel;
    @BindView(R.id.recyclerview_book)
    PullLoadMoreRecyclerView mRecyclerviewBook;
    @BindView(R.id.view_search)
    LinearLayout mViewSearch;
    @BindView(R.id.view_hotSearch)
    LinearLayout mViewHotSearch;
    @BindView(R.id.lin_scanhis)
    LinearLayout mLinScanhis;

    private List<BookSearchResponse.SearchResultBean> bookListData = new ArrayList<>();
    private String keyword;//输入的搜索关键词

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        mModel = new BookModel(this);
        AutoUtils.auto(this);
        initListener();
        initview();
        initdata();
    }

    /**
     * 加载热门搜索
     */
    private void initdata() {
        mModel.getHotword(new AppBaseResponseCallBack<NovateResponse<HotWordResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<HotWordResponse> response) {
                setHotView(response.getData().getHotword());
            }
        });
    }

    /**
     * 加载热门搜索的UI
     *
     * @param mHotword
     */
    private void setHotView(List<String> mHotword) {
        for (final String s : mHotword) {
            TextView mTextView = new TextView(this);
            mTextView.setText(s);
//            mTextView.setTextColor(Color.parseColor("#e6ffff"));
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 28);
            mTextView.setPadding(10,10,10,10);
            mTextView.setBackgroundResource(R.drawable.shape_round_white);
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mParams.setMargins(20, 20, 20, 20);
            mTextView.setLayoutParams(mParams);
            AutoUtils.auto(mTextView);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEtSearch.setText(s);
                    keyword=s;
                    search(currpage);
                }
            });
            mHotSearchFlow.addView(mTextView);
        }
    }

    SearchBookAdapter mAdapter;

    private void initview() {
        mAdapter = new SearchBookAdapter(this, bookListData);
        mRecyclerviewBook.setLinearLayout();
        mRecyclerviewBook.setAdapter(mAdapter);
        initRefresh();
    }
    private void initRefresh() {
      mRecyclerviewBook.setPullRefreshEnable(false);
        mRecyclerviewBook.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (currpage < pagecount) {
                    currpage++;
                    search(currpage);
                } else {
                    mRecyclerviewBook.setPullLoadMoreCompleted();
                }
            }
        });
    }

    private void initListener() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewHotSearch.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
                mViewSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                keyword = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search(currpage);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 通过关键字从服务端搜索
     *
     * @param mCurrpage 页码
     */
    int pagecount=1,currpage=1;
    private void search(final int mCurrpage) {
        mModel.searchBook(keyword,mCurrpage, new AppBaseResponseCallBack<NovateResponse<BookSearchResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<BookSearchResponse> response) {
               if (mCurrpage==1)bookListData.clear();
                pagecount=response.getData().getPageCount();
                bookListData.addAll(response.getData().getSearch_result());
                mAdapter.refreshData(bookListData);
                List<BookSearchResponse.ScanRecordBean> mScanList = response.getData().getScan_record();
                setScanList(mScanList);
                mRecyclerviewBook.setPullLoadMoreCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRecyclerviewBook.setPullLoadMoreCompleted();
            }
        });

    }

    /**
     * 处理扫描记录的view
     *
     * @param mScanList
     */
    public void setScanList(List<BookSearchResponse.ScanRecordBean> mScanList) {
        for (final BookSearchResponse.ScanRecordBean scan : mScanList) {
            View mView = LayoutInflater.from(this).inflate(R.layout.view_searchhis, null);
            TextView nameview = (TextView) mView.findViewById(R.id.bookname);
            TextView timeview = (TextView) mView.findViewById(R.id.timeOrauthor);
            nameview.setText("《"+scan.getBook_name()+"》");
            timeview.setText(scan.getScan_time());
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(SearchActivity.this, BookDetailActivity.class);
                    mIntent.putExtra(BookDetailActivity.BOOK_ID, scan.getBook_id());
                    startActivity(mIntent);
                }
            });
            AutoUtils.auto(mView);
            mLinScanhis.addView(mView);
        }
    }
}
