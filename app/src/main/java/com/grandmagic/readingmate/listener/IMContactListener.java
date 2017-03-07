package com.grandmagic.readingmate.listener;

import android.content.Context;
import android.util.Log;

import com.hyphenate.EMContactListener;
import com.orhanobut.logger.Logger;

/**
 * Created by lps on 2017/3/7.
 * 好友相关监听
 */

public class IMContactListener implements EMContactListener {

    private Context mContext;

    public IMContactListener(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //收到好友邀请
        Logger.e("onContactInvited: " +username+reason);
    }

    /**
     * 好友请求被同意
     * @param mS
     */
    @Override
    public void onFriendRequestAccepted(String mS) {
        Logger.e("onFriendRequestAccepted: " +mS);
    }

    /**
     * 好友请求被拒绝
     * @param mS
     */
    @Override
    public void onFriendRequestDeclined(String mS) {
        Logger.e("onFriendRequestDeclined: " +mS);
    }

    @Override
    public void onContactDeleted(String username) {
        Logger.e("onContactDeleted: " +username);

        //被删除时回调此方法
    }


    @Override
    public void onContactAdded(String username) {
        //增加了联系人时回调此方法
        Logger.e("onContactAdded: " +username);

    }
}
