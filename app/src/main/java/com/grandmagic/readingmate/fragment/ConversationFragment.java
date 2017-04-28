package com.grandmagic.readingmate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.activity.FriendDetailActivity;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import static com.grandmagic.readingmate.activity.ChatActivity.REQUEST_DETAIL;

/**
 * Created by lps on 2017/4/28.
 *
 * @version 1
 * @see
 * @since 2017/4/28 9:48
 */


public class ConversationFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setChatFragmentListener(this);
    }

    /**
     * set message attribute
     *
     * @param message
     */
    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    /**
     * enter to chat detail
     */
    @Override
    public void onEnterToChatDetails() {

    }

    /**
     * on avatar clicked
     *
     * @param username
     */
    @Override
    public void onAvatarClick(String username) {
        Intent mIntent = new Intent(getActivity(), FriendDetailActivity.class);
        Bundle mBundle = new Bundle();
        Contacts mContacts = IMHelper.getInstance().getUserInfo(username);
        if (mContacts == null) return;
        PersonInfo mInf = new PersonInfo();
        mInf.setUser_id(mContacts.getUser_id() + "");
        mInf.setAvatar(mContacts.getAvatar_url().getLarge());
//        mInf.setFriend(isfriend);
        mInf.setNickname(mContacts.getUser_name());
        mInf.setClientid(mContacts.getClientid());
        mInf.setGender(gender);
        mInf.setSignature(mContacts.getSignature());
        mBundle.putParcelable(FriendDetailActivity.PERSON_INFO, mInf);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent, REQUEST_DETAIL);
    }

    /**
     * on avatar long pressed
     *
     * @param username
     */
    @Override
    public void onAvatarLongClick(String username) {

    }

    /**
     * on message bubble clicked
     *
     * @param message
     */
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    /**
     * on message bubble long pressed
     *
     * @param message
     */
    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    /**
     * on extend menu item clicked, return true if you want to override
     *
     * @param itemId
     * @param view
     * @return
     */
    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    /**
     * on set custom chat row provider
     *
     * @return
     */
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }

}
