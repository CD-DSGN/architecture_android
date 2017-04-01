package com.grandmagic.readingmate.listener;

import android.content.Context;

import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.bean.response.InviteMessage;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.db.InviteMessageDao;
import com.grandmagic.readingmate.event.ContactDeletedEvent;
import com.grandmagic.readingmate.event.NewFriendRequestEvent;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.EMContactListener;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lps on 2017/3/7.
 * 好友相关监听
 * 执行在子线程
 */

public class IMContactListener implements EMContactListener {

    private Context mContext;

    public IMContactListener(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //收到好友邀请
        Logger.e("onContactInvited: " + username + reason);

        InviteMessage mMessage = new InviteMessage();
        mMessage.setFrom(username);
        mMessage.setTime(System.currentTimeMillis());
        mMessage.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
        mMessage.setReason(reason);
        mMessage.setIsread(1);
        IMHelper.getInstance().newInvaiteMsg(mMessage);
        EventBus.getDefault().post(new NewFriendRequestEvent());
    }


    /**
     * 好友请求被同意
     *
     * @param mS
     */
    @Override
    public void onFriendRequestAccepted(String mS) {
        Logger.e("onFriendRequestAccepted: " + mS);
        InviteMessageDao mInviteDao = DBHelper.getInviteDao(mContext);
        InviteMessage mUnique = mInviteDao.queryBuilder().where(InviteMessageDao.Properties.From.eq(mS)).build().unique();
        if (mUnique == null) return;
        mUnique.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
        mUnique.setTime(System.currentTimeMillis());
        mInviteDao.update(mUnique);

        InviteMessage mMessage = new InviteMessage();
        mMessage.setFrom(mS);
        IMHelper.getInstance().newInvaiteMsg(mMessage);
        EventBus.getDefault().post(new NewFriendRequestEvent());
    }

    /**
     * 好友请求被拒绝
     *
     * @param mS
     */
    @Override
    public void onFriendRequestDeclined(String mS) {
        Logger.e("onFriendRequestDeclined: " + mS);
    }

    @Override
    public void onContactDeleted(String username) {
        Logger.e("onContactDeleted: " + username);
        EventBus.getDefault().post(new ContactDeletedEvent(username));
        ContactsDao mContactsDao = DBHelper.getContactsDao(mContext);
        Contacts mUnique = mContactsDao.queryBuilder().where(ContactsDao.Properties.User_name.eq(username)).build().unique();
        if (mUnique != null) mContactsDao.delete(mUnique);
        //被删除时回调此方法
    }


    @Override
    public void onContactAdded(String username) {
        //增加了联系人时回调此方法
        Logger.e("onContactAdded: " + username);

    }
}

