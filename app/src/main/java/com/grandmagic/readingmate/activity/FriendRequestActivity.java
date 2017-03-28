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
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.bean.response.RequestListResponse;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.model.ContactModel;
import com.tamic.novate.NovateResponse;

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

    List<RequestListResponse> mListResponses = new ArrayList<>();
    ContactModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        initview();
        loadDataFromServer();
    }

    RequestListAdapter mAdapter;

    private void initview() {
        mContext = this;
        mTitle.setText("新阅友");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RequestListAdapter(mContext, mListResponses);
        mAdapter.setStateListener(this);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadDataFromServer() {
        mModel = new ContactModel(this);
        mModel.getallRequest(new AppBaseResponseCallBack<NovateResponse<List<RequestListResponse>>>(this) {
            @Override
            public void onSuccee(NovateResponse<List<RequestListResponse>> response) {
                mListResponses.addAll(response.getData());
                mAdapter.setdata(mListResponses);
            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    /**
     * 同意好友请求
     *
     * @param data
     * @param mPosition
     */
    @Override
    public void accpet(final RequestListResponse data, final int mPosition) {
        mModel.acceptFriend(data.getRequest_id(), new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                mListResponses.remove(mPosition);
                mAdapter.setdata(mListResponses);
                //添加好友时候将对方添加到自己的本地数据库
                Contacts mContacts = new Contacts();
                mContacts.setUser_id(Integer.valueOf(data.getUser_id()));
                mContacts.setUser_name(data.getUser_name());
                mContacts.setAvatar_native(data.getAvatar_native());
                DBHelper.getContactsDao(FriendRequestActivity.this).save(mContacts);
            }
        });
    }

    /**
     * 拒绝好友请求
     *
     * @param mData
     * @param mPosition
     */
    @Override
    public void refuse(RequestListResponse mData, final int mPosition) {
        mModel.refuseFriend(mData.getRequest_id(), new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                mListResponses.remove(mPosition);
                mAdapter.setdata(mListResponses);
            }
        });
    }

}
