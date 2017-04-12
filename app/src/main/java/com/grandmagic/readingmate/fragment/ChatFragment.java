package com.grandmagic.readingmate.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.AddFriendActivity;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.activity.FriendActivity;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.adapter.RecentConversationDelagate;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.db.InviteMessage;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.db.InviteMessageDao;
import com.grandmagic.readingmate.event.ConnectStateEvent;
import com.grandmagic.readingmate.event.ContactDeletedEvent;
import com.grandmagic.readingmate.event.FriendDeleteEvent;
import com.grandmagic.readingmate.event.NewFriendRequestEvent;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.SwipRecycleView;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.orhanobut.logger.Logger;
import com.tamic.novate.util.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.CountQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.util.SimpleRefreshListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends AppBaseFragment implements RecentConversationDelagate.RecentConversationListener {
    public static final int REQUEST_READMSG = 1;
    Context mContext;
    @BindView(R.id.tv_friend)
    TextView mTvFriend;
    @BindView(R.id.iv_friend)
    ImageView mIvFriend;
    @BindView(R.id.rela_friend)
    RelativeLayout mRelaFriend;
    @BindView(R.id.recyclerview)
    SwipRecycleView mRecyclerview;//最近会话列表
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.tv_connect_errormsg)
    TextView mTvConnectErrormsg;
    @BindView(R.id.view_error)
    LinearLayout mViewError;
    @BindView(R.id.red_point)
    ImageView mRedPoint;
    Unbinder mBind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        AutoUtils.auto(view);
        mBind = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initview();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        setSystemBarColor(false);
        onrefreshConversation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mBind.unbind();
    }


    DefaultEmptyAdapter mAdapter;
    List<EMConversation> mConversations = new ArrayList<>();

    private void initview() {
        setstate();
        mContext = getActivity();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        //为了以后也许会加入群组会话等，使用MultiItemTypeAdapter，便于扩展
        MultiItemTypeAdapter minnerAdapter = new MultiItemTypeAdapter(mContext, mConversations);
        RecentConversationDelagate mRecentConversationDelagate = new RecentConversationDelagate(mContext);
        mRecentConversationDelagate.setRecentConversationListener(this);
        minnerAdapter.addItemViewDelegate(mRecentConversationDelagate);
        mAdapter = new DefaultEmptyAdapter(minnerAdapter, mContext);
        mRecyclerview.setAdapter(mAdapter);
        initrefreshlayout();
    }

    /**
     * 连接状态改变的状态处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConnectStateEvent event) {
        setstate();
    }

    private void setstate() {
        String mString = SPUtils.getInstance().getString(getActivity(), SPUtils.IM_STATE);
        if (mString.equals("0")) {
            mViewError.setVisibility(View.GONE);
        } else {
            mViewError.setVisibility(View.VISIBLE);
            switch (Integer.valueOf(mString)) {
                case EMError.USER_LOGIN_ANOTHER_DEVICE:
                    mTvConnectErrormsg.setText("账户在其他设备登陆" + mString);
                    break;
                case EMError.USER_REMOVED:
                    mTvConnectErrormsg.setText("账户被移除" + mString);
                    break;
                default:
                    mTvConnectErrormsg.setText("连接im服务器错误Errcode=" + mString);

                    break;
            }
        }
    }

    private void initrefreshlayout() {
        BGAStickinessRefreshViewHolder mRefreshViewHolder = new BGAStickinessRefreshViewHolder(mContext, false);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);
//        mRefreshLayout.offsetTopAndBottom(88);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new SimpleRefreshListener() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                onrefreshConversation();
                mRefreshLayout.endRefreshing();
            }
        });
    }

    /**
     * 创建测试数据
     */
    private List<EMConversation> loadAllConversation() {
        unreadmsgCount=0;
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        Logger.d(list);
        for (EMConversation mConversation:
             list) {
         unreadmsgCount+=   mConversation.getUnreadMsgCount();//每个会话的未读消息
        }
        if (munredInviteCount>0)
        unreadmsgCount+= 1;//如果有未读的邀请信息
        return list;
    }
int unreadmsgCount;//未读消息
    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @OnClick({R.id.add_friend, R.id.rela_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_friend:
                startActivity(new Intent(getActivity(), AddFriendActivity.class));
                break;
            case R.id.rela_friend:
                Intent mIntent = new Intent(getActivity(), FriendActivity.class);
                mIntent.putExtra(FriendActivity.NEW_FRIEND, munredInviteCount);
                startActivity(mIntent);
                break;
        }
    }

    /**
     * 收到新消息时候刷新
     */
    int munredInviteCount;//好友邀请

    public void onrefreshConversation() {
        mConversations.clear();
        mConversations.addAll(loadAllConversation());
        mAdapter.refresh();
        InviteMessageDao mInviteDao = DBHelper.getInviteDao(mContext);
        CountQuery<InviteMessage> mInviteMessageCountQuery = mInviteDao.queryBuilder().where(InviteMessageDao.Properties.Isread.eq(1)).buildCount();
        munredInviteCount = (int) mInviteMessageCountQuery.count();
        DBHelper.close();
        mRedPoint.setVisibility(munredInviteCount > 0 ? View.VISIBLE : View.GONE);
        ((MainActivity) getActivity()).setUnredmsg(unreadmsgCount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_READMSG) {
// TODO: 2017/3/3 处理点击
        }
    }

    /**
     * 侧滑删除会话
     *
     * @param username 环信那边的username
     */
    @Override
    public void delete(String username) {
        //删除和某个user会话，如果需要保留聊天记录，传false
        boolean mB = EMClient.getInstance().chatManager().deleteConversation(username, true);
        if (mB) {
            onrefreshConversation();
        } else {
            Toast.makeText(mContext, "移除会话失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onitemclick(EMMessage mLastMessage, String mFinalUsername, int position) {

        Intent mIntent = new Intent(mContext, ChatActivity.class);
        mIntent.putExtra(ChatActivity.CHAT_IM_NAME, mLastMessage.direct() == EMMessage.Direct.SEND ?
                mLastMessage.getTo() : mLastMessage.getFrom());
        mIntent.putExtra(ChatActivity.CHAT_NAME, mFinalUsername);
        ContactsDao mContactsDao = DBHelper.getContactsDao(mContext);
        Contacts mUnique = mContactsDao.queryBuilder().where(ContactsDao.Properties.User_id.eq(mFinalUsername)).build().unique();
        DBHelper.close();
        if (mUnique != null)
            mIntent.putExtra(ChatActivity.GENDER, mUnique.getGender());
        mConversations.get(position).markAllMessagesAsRead();
        onrefreshConversation();
        ((MainActivity) mContext).startActivityForResult(mIntent, REQUEST_READMSG);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        setSystemBarColor(hidden);
    }

    private void setSystemBarColor(boolean hidden) {
        if (!hidden) ((AppBaseActivity) (mContext)).setSystemBarColor(R.color.white);
    }

    /**
     * 当删除好友的时候回调
     *
     * @param mEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void friendDelete(FriendDeleteEvent mEvent) {
        EMClient.getInstance().chatManager().deleteConversation(mEvent.getUser_id(), true);
        onrefreshConversation();
    }

    /**
     * 当被好友删除的时候回调
     *
     * @param mEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contactDeleted(ContactDeletedEvent mEvent) {
        for (EMConversation mConversation : mConversations) {
            if (mConversation.conversationId().equals(mEvent.getName())) {
                boolean mB = EMClient.getInstance().chatManager().deleteConversation(mEvent.getName(), false);
                if (mB) onrefreshConversation();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFriendRequestevent(NewFriendRequestEvent mEvent) {
        onrefreshConversation();
    }
}
