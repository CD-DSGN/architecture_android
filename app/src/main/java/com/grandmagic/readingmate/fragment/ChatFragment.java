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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.AddFriendActivity;
import com.grandmagic.readingmate.activity.FriendActivity;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.adapter.RecentConversationDelagate;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.SwipRecycleView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends AppBaseFragment {

    Context mContext;
    @BindView(R.id.tv_friend)
    TextView mTvFriend;
    @BindView(R.id.iv_friend)
    ImageView mIvFriend;
    @BindView(R.id.rela_friend)
    RelativeLayout mRelaFriend;
    @BindView(R.id.recyclerview)
    SwipRecycleView mRecyclerview;//最近会话列表


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        AutoUtils.auto(view);
        ButterKnife.bind(this, view);
        initview();
        return view;
    }

    MultiItemTypeAdapter mAdapter;
    List<EMConversation> mConversations;

    private void initview() {
        mContext = getActivity();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
         mConversations = loadAllConversation();
        mAdapter = new MultiItemTypeAdapter(mContext, mConversations);//为了以后也许会加入群组会话等，使用MultiItemTypeAdapter，便于扩展
        mAdapter.addItemViewDelegate(new RecentConversationDelagate(mContext));
        mRecyclerview.setAdapter(mAdapter);
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
    public void onrefreshConversation(){
        mConversations=loadAllConversation();
        mAdapter.setData(mConversations);
    }
}
