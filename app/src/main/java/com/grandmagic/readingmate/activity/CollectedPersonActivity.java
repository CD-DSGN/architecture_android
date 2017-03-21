package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CollectionAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CollectedPersonActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_person);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        initview();
    }

    CollectionAdapter mAdapter;
    List<String> mStrings = new ArrayList<>();

    private void initview() {
        mTitle.setText("收藏过的用户");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mStrings.add("afs");
        mStrings.add("afs");
        mStrings.add("afs");
        mAdapter = new CollectionAdapter(this, mStrings);
        mRecyclerview.setAdapter(mAdapter);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
