package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommentDetailAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.umeng.socialize.UMShareAPI;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rv_comments_detail)
    RecyclerView mRvCommentsDetail;
    @BindView(R.id.lin_share)
    LinearLayout mLinShare;

    private View mView;
    private CommentDetailAdapter mMAdapter;
    private HeaderAndFooterWrapper mMHeaderAndFooterWrapper;
    private SharePopUpWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        setSystemBarColor(R.color.white);
        initView();
    }

    private void initView() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("电影不错");
            data.add("结局缺乏新意，其他没什么");
        }
        mTitle.setText(R.string.comment_detail);
        mRvCommentsDetail.setLayoutManager(new LinearLayoutManager(this));
        mView = LayoutInflater.from(this).inflate(R.layout.item_comments_detail, mRvCommentsDetail, false);
        AutoUtils.auto(mView);
        mMAdapter = new CommentDetailAdapter(this, data);
        mMHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMAdapter);

        mMHeaderAndFooterWrapper.addHeaderView(mView);
        mRvCommentsDetail.setAdapter(mMHeaderAndFooterWrapper);
        mMHeaderAndFooterWrapper.notifyDataSetChanged();

    }


    @OnClick({R.id.back, R.id.title, R.id.lin_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                break;

            case R.id.lin_share:
                //分享评论
                showSharePopWindow();
                break;
        }
    }

    private void showSharePopWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = new SharePopUpWindow(this);
        }
        mPopupWindow.show();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }


}
