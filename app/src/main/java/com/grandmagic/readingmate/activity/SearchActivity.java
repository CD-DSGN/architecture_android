package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.FlowLayout;

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


    @BindView(R.id.lin_scanhis)
    LinearLayout mLinScanhis;

    @BindView(R.id.iv_searchlist)
    ImageView mIvSearchlist;
    @BindView(R.id.bookname)
    TextView mBookname;
    @BindView(R.id.timeOrauthor)
    TextView mTimeOrauthor;
    @BindView(R.id.lin_searchhis)
    LinearLayout mLinSearchhis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        AutoUtils.auto(this);
        initListener();
    }

    private void initListener() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHotSearchFlow.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
                mTvHotSearch.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
