package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.activity.ChatActivity;
import com.hyphenate.chat.EMMessage;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/3/29.
 *
 * @version 1
 * @see
 * @since 2017/3/29 14:59
 */


public class MessageLocationDelagate extends ChatItemViewDelegate{
    public MessageLocationDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    protected void childConvert(ViewHolder mHolder, EMMessage mChatMessage, int mPosition) {

    }

    @Override
    protected View setContentView(EMMessage mChatMessage, RelativeLayout mHolderView) {
        return null;
    }

    /**{@link com.zhy.adapter.recyclerview.base.com.zhy.adapter.recyclerview.base.ItemViewDelegateManager}
     * 如果没有对应的type消息类型
     * 会抛出异常 @ No ItemViewDelegate added that matches position=8 in data source
     * 所以暂时发送不支持的消息先在这里返回true处理一下
     * @return
     */
    @Override
    protected boolean isForViewType(EMMessage mItem) {
        return mItem.getType()== EMMessage.Type.LOCATION||mItem.getType()== EMMessage.Type.VIDEO
        || mItem.getType()== EMMessage.Type.CMD||mItem.getType()== EMMessage.Type.FILE;
    }
}
