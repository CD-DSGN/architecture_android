package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.RequestListAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.bean.response.FriendRequestBean;
import com.grandmagic.readingmate.bean.response.InviteMessage;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.db.InviteMessageDao;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendRequestActivity extends AppBaseActivity implements RequestListAdapter.StateListener {
    Context mContext;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    List<InviteMessage> mInviteMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        loadDataFromDB();
        initview();
    }

    RequestListAdapter mAdapter;

    private void initview() {
        mContext = this;
        mTitle.setText("新阅友");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RequestListAdapter(mContext, mInviteMessages);
        mAdapter.setStateListener(this);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadDataFromDB() {
        InviteMessageDao mInviteDao = DBHelper.getInviteDao(this);
        mInviteMessages = mInviteDao.loadAll();
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public void accpet(InviteMessage data) {
        try {
            EMClient.getInstance().contactManager().acceptInvitation(data.getFrom());
            data.setStatus(InviteMessage.InviteMesageStatus.AGREED);
            DBHelper.getInviteDao(mContext).insertOrReplace(data);//更新数据库
        } catch (HyphenateException mE) {
            mE.printStackTrace();
        }
        loadDataFromDB();
        mAdapter.notifyDataSetChanged();
    }

}
