package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.adapter.NotificationCommentDelagte;
import com.grandmagic.readingmate.adapter.NotificationThumbDelagte;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.NotificationCommentResponse;
import com.grandmagic.readingmate.model.CommentRecordModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class MessageNotificationActivity extends AppBaseActivity {

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
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notification);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
        initdata();
    }

    DefaultEmptyAdapter mAdapter;
    List<NotificationCommentResponse.InfoBean> mStrings = new ArrayList<>();

    private void initview() {
        mTitle.setText("评论记录");
        initRefresh();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        MultiItemTypeAdapter mInnerAdapter = new MultiItemTypeAdapter(this, mStrings);
        mInnerAdapter.addItemViewDelegate(new NotificationCommentDelagte(this))
                .addItemViewDelegate(new NotificationThumbDelagte(this));
        mAdapter = new DefaultEmptyAdapter(mInnerAdapter, this);
        mRecyclerview.setAdapter(mAdapter);
    }

    CommentRecordModel mModel;
    int currpage = 1, pagecount = 1;

    private void initdata() {
        mModel = new CommentRecordModel(this);
        loadAllReply(currpage);

    }

    private void loadAllReply(int mCurrpage) {
        mModel.getAllreply(mCurrpage, new AppBaseResponseCallBack<NovateResponse<NotificationCommentResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<NotificationCommentResponse> response) {
                pagecount = response.getData().getPage();
                if (response.getData().getInfo() != null && !response.getData().getInfo().isEmpty())
                    mStrings.addAll(response.getData().getInfo());
                mAdapter.refresh();
                mRefreshLayout.endLoadingMore();
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mAdapter.refresh();
                mRefreshLayout.endLoadingMore();
                mRefreshLayout.endRefreshing();
            }
        });
    }


    private void initRefresh() {
        BGAStickinessRefreshViewHolder mRefreshViewHolder;
        mRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);

        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                currpage = 1;
                mStrings.clear();
                loadAllReply(currpage);
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if (currpage < pagecount) {
                    currpage++;
                    loadAllReply(currpage);
                    return true;
                } else {
                    Toast.makeText(MessageNotificationActivity.this, "NOMORE", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.endLoadingMore();
                    mRefreshLayout.endRefreshing();
                    return false;
                }
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
