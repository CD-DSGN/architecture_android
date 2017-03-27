package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.SearchCollectBookAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.SimpleBookInfoListResponseBean;
import com.grandmagic.readingmate.model.BookModel;
import com.tamic.novate.NovateResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectBookSearchActivity extends AppBaseActivity {

    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_search_result_of_collect_book)
    RecyclerView mRvSearchResultOfCollectBook;

    private BookModel mBookModel;
    private SearchCollectBookAdapter mSearchCollectBookAdapter;

    List<SimpleBookInfoListResponseBean.SimpleBookInfo> mSimpleBookInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_book_search);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        mBookModel = new BookModel(this);
        mRvSearchResultOfCollectBook.setLayoutManager(new LinearLayoutManager(this));
        mSearchCollectBookAdapter = new SearchCollectBookAdapter(this, mSimpleBookInfos);
        mRvSearchResultOfCollectBook.setAdapter(mSearchCollectBookAdapter);

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String keyword = v.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        mBookModel.searchCollectBook(keyword,
                                new AppBaseResponseCallBack<NovateResponse<List<SimpleBookInfoListResponseBean.SimpleBookInfo>>>(CollectBookSearchActivity.this) {
                                    @Override
                                    public void onSuccee(NovateResponse<List<SimpleBookInfoListResponseBean.SimpleBookInfo>> response) {
                                        mSimpleBookInfos.clear();
                                        if (response.getData() != null) {
                                            mSimpleBookInfos.addAll(response.getData());
                                        }
                                        mSearchCollectBookAdapter.notifyDataSetChanged();
                                    }
                                });
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @OnClick({R.id.rv_search_result_of_collect_book})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rv_search_result_of_collect_book:

                break;
        }
    }
}
