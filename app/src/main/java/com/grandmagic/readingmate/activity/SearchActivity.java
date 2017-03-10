package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.SearchBookAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.BookSearchResponse;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.FlowLayout;
import com.tamic.novate.NovateResponse;

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
    TextView mTvHotSearch;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.hotSearch_Flow)
    FlowLayout mHotSearchFlow;
    @BindView(R.id.iv_scanlist)
    ImageView mIvScanlist;
    @BindView(R.id.iv_searchlist)
    ImageView mIvSearchlist;
    @BindView(R.id.bookname)
    TextView mBookname;
    @BindView(R.id.timeOrauthor)
    TextView mTimeOrauthor;
    BookModel mModel;
    @BindView(R.id.recyclerview_book)
    RecyclerView mRecyclerviewBook;
    @BindView(R.id.view_search)
    LinearLayout mViewSearch;
    @BindView(R.id.view_hotSearch)
    LinearLayout mViewHotSearch;
    private List<BookSearchResponse> bookListData = new ArrayList<>();

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
    }

    SearchBookAdapter mAdapter;

    private void initview() {
        mAdapter = new SearchBookAdapter(this, bookListData);
        mRecyclerviewBook.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerviewBook.setAdapter(mAdapter);
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
                String mString = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    bookListData.clear();
                    search(mString);
                    return true;
                }
                return false;
            }
        });
    }

    private void search(String mString) {
        mModel.searchBook(mString, new AppBaseResponseCallBack<NovateResponse<List<BookSearchResponse>>>(this) {
            @Override
            public void onSuccee(NovateResponse<List<BookSearchResponse>> response) {
                bookListData.addAll(response.getData());
                mAdapter.refreshData(bookListData);
            }
        });
    }
}
