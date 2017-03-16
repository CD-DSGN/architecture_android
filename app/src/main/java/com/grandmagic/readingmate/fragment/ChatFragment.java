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

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.AddFriendActivity;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.activity.FriendActivity;
import com.grandmagic.readingmate.activity.FriendDetailActivity;
import com.grandmagic.readingmate.activity.MainActivity;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.adapter.RecentConversationDelagate;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.event.ConnectStateEvent;
import com.grandmagic.readingmate.event.ContactDeleteEvent;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        AutoUtils.auto(view);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initview();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSystemBarColor(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    MultiItemTypeAdapter mAdapter;
    List<EMConversation> mConversations;

    private void initview() {
        setstate();
        mContext = getActivity();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mConversations = loadAllConversation();
        mAdapter = new MultiItemTypeAdapter(mContext, mConversations);//为了以后也许会加入群组会话等，使用MultiItemTypeAdapter，便于扩展
        RecentConversationDelagate mRecentConversationDelagate = new RecentConversationDelagate(mContext);
        mRecentConversationDelagate.setRecentConversationListener(this);
        mAdapter.addItemViewDelegate(mRecentConversationDelagate);
        mRecyclerview.setAdapter(mAdapter);
        initrefreshlayout();
    }

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
        return list;
    }

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
                startActivity(new Intent(getActivity(), FriendActivity.class));
                break;
        }
    }

    /**
     * 收到新消息时候刷新
     */
    public void onrefreshConversation() {
        mConversations = loadAllConversation();
        mAdapter.setData(mConversations);
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
        EMClient.getInstance().chatManager().deleteConversation(username, true);
        onrefreshConversation();
    }

    @Override
    public void onitemclick(EMMessage mLastMessage, String mFinalUsername, int position) {

        Intent mIntent = new Intent(mContext, ChatActivity.class);
        mIntent.putExtra(ChatActivity.CHAT_IM_NAME, mLastMessage.direct() == EMMessage.Direct.RECEIVE ?
                mLastMessage.getFrom() : mLastMessage.getTo());
        mIntent.putExtra(ChatActivity.CHAT_NAME, mFinalUsername);
        mConversations.get(position).markAllMessagesAsRead();
        onrefreshConversation();
        ((MainActivity) mContext).startActivityForResult(mIntent, REQUEST_READMSG);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        setSystemBarColor(hidden);
    }

    private void setSystemBarColor(boolean hidden) {
        if (!hidden) ((AppBaseActivity) (mContext)).setSystemBarColor(android.R.color.darker_gray);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contactDelete(ContactDeleteEvent mEvent){
        EMClient.getInstance().chatManager().deleteConversation(mEvent.getUser_id(),true);
        onrefreshConversation();
    }
}
