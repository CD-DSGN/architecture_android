package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.RequestListAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.db.InviteMessage;
import com.grandmagic.readingmate.bean.response.RequestListResponse;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.db.InviteMessageDao;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

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
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        initview();
        loadDataFromServer();
    }

    RequestListAdapter mAdapter;
DefaultEmptyAdapter mEmptyAdapter;
    private void initview() {
        mContext = this;
        mTitle.setText("新好友");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new RequestListAdapter(mContext, mListResponses);
        mAdapter.setStateListener(this);
        mEmptyAdapter=new DefaultEmptyAdapter(mAdapter,this);
        mRecyclerview.setAdapter(mEmptyAdapter);
    }

    private void loadDataFromServer() {
        //标记为已读
        InviteMessageDao mInviteDao = DBHelper.getInviteDao(this);
        List<InviteMessage> mInviteMessages = DBHelper.getInviteDao(this).loadAll();
        for (int i = 0; i < mInviteMessages.size(); i++) {
            mInviteMessages.get(i).setIsread(0);
            mInviteDao.update(mInviteMessages.get(i));
        }
        DBHelper.close();
        mModel = new ContactModel(this);
        mModel.getallRequest(new AppBaseResponseCallBack<NovateResponse<List<RequestListResponse>>>(this) {
            @Override
            public void onSuccee(NovateResponse<List<RequestListResponse>> response) {
                mListResponses.addAll(response.getData());
                mEmptyAdapter.refresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mEmptyAdapter.refresh();
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
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(data.getUser_id());
                } catch (HyphenateException mE) {
                    mE.printStackTrace();
                }
                mListResponses.remove(mPosition);
                mEmptyAdapter.refresh();
                //添加好友时候将对方添加到自己的本地数据库
                Contacts mContacts = new Contacts();
                mContacts.setUser_id(Integer.valueOf(data.getUser_id()));
                mContacts.setUser_name(data.getUser_name());
                mContacts.setAvatar_url(data.getAvatar_native());
                DBHelper.getContactsDao(FriendRequestActivity.this).insertOrReplace(mContacts);
                DBHelper.close();
                EMMessage mTxtSendMessage = EMMessage.createTxtSendMessage("我们已经是好友啦，快来聊天吧！", data.getUser_id());
                if (mTxtSendMessage != null) {
                    EMClient.getInstance().chatManager().sendMessage(mTxtSendMessage);
                }
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
    public void refuse(final RequestListResponse mData, final int mPosition) {
        mModel.refuseFriend(mData.getRequest_id(), new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                try {
                    EMClient.getInstance().contactManager().declineInvitation(mData.getUser_id());
                } catch (HyphenateException mE) {
                    mE.printStackTrace();
                }
                mListResponses.remove(mPosition);
                mEmptyAdapter.refresh();
            }
        });
    }

}
