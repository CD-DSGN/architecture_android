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
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    PullLoadMoreRecyclerView mRecyclerview;


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
        mRecyclerview.setLinearLayout();
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
           mRecyclerview.setPullLoadMoreCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mAdapter.refresh();
                mRecyclerview.setPullLoadMoreCompleted();
            }
        });
    }


    private void initRefresh() {
    mRecyclerview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
        @Override
        public void onRefresh() {
            currpage = 1;
            mStrings.clear();
            loadAllReply(currpage);
        }
        @Override
        public void onLoadMore() {
            if (currpage < pagecount) {
                currpage++;
                loadAllReply(currpage);

            } else {
                Toast.makeText(MessageNotificationActivity.this, "NOMORE", Toast.LENGTH_SHORT).show();
           mRecyclerview.setPullLoadMoreCompleted();
            }
        }
    });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
