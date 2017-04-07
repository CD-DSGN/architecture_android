package com.grandmagic.readingmate.listener;

import android.content.Context;

import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.db.InviteMessage;
import com.grandmagic.readingmate.bean.response.OtherInfoResponse;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.db.InviteMessageDao;
import com.grandmagic.readingmate.event.ContactDeletedEvent;
import com.grandmagic.readingmate.event.NewFriendRequestEvent;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.IMHelper;
import com.hyphenate.EMContactListener;
import com.orhanobut.logger.Logger;
import com.tamic.novate.NovateResponse;

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
    public void onFriendRequestAccepted(final String mS) {
        Logger.e("onFriendRequestAccepted: " + mS);
        InviteMessage mMessage = new InviteMessage();
        mMessage.setFrom(mS);
        mMessage.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
        mMessage.setTime(System.currentTimeMillis());
        IMHelper.getInstance().newInvaiteMsg(mMessage);
        EventBus.getDefault().post(new NewFriendRequestEvent());
        //        将这个新好友的信息添加到本地好友列表
        new ContactModel(mContext).getOtherUserInfo(mS, new AppBaseResponseCallBack<NovateResponse<OtherInfoResponse>>(mContext) {
            @Override
            public void onSuccee(NovateResponse<OtherInfoResponse> response) {
                OtherInfoResponse mData = response.getData();
                Contacts mContacts = new Contacts();
                try {
                    mContacts.setUser_id(Integer.valueOf(mS));
                } catch (NumberFormatException mE) {
                    mE.printStackTrace();
                }
                mContacts.setUser_name(mData.getUser_name());
                mContacts.setAvatar_url(mData.getAvatar_url());
                mContacts.setClientid(mData.getClientid());
                mContacts.setGender(mData.getGender());
                mContacts.setSignature(mData.getSignature());
                mContacts.setType(Contacts.TYPE.TYPE_FRIEND);
                DBHelper.getContactsDao(mContext).insertOrReplace(mContacts);
                DBHelper.close();
            }
        });
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
        DBHelper.close();
        //被删除时回调此方法
    }


    @Override
    public void onContactAdded(String username) {
        //增加了联系人时回调此方法
        Logger.e("onContactAdded: " + username);

    }
}

