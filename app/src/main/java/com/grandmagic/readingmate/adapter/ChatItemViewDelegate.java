package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMMessage;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/3/6.
 * 通用的消息类型代理
 * 点击头像等事件后续放到这里面处理
 */

public abstract class ChatItemViewDelegate implements ItemViewDelegate<EMMessage> {
    EMMessage.Direct mDirect;
    Context mContext;

    public ChatItemViewDelegate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return mDirect== EMMessage.Direct.RECEIVE? R.layout.item_common_recivemsg:R.layout.item_common_sendmsg;
    }

    /**
     * fixme 因为把接收消息和发送消息放到一个delagete处理，所以会被调用2次
     * @param holder
     * @param mChatMessage
     * @param position
     */
    @Override
    public void convert(ViewHolder holder, EMMessage mChatMessage, int position) {
        mDirect=mChatMessage.direct();
        Contacts mUserInfo = IMHelper.getInstance()
                .getUserInfo(mChatMessage.getFrom());
        if (mUserInfo != null){
            ImageLoader.loadRoundImage(mContext,
                    Environment.BASEULR_PRODUCTION + mUserInfo.getAvatar_native(),
                    (ImageView) holder.getView(R.id.avatar)
            );
        }
        View childView = setContentView();
        ViewHolder mHolder=ViewHolder.createViewHolder(mContext, childView);
        RelativeLayout holderView = holder.getView(R.id.contentView);
        childConvert(mHolder,mChatMessage,position);
        if (holderView.getChildCount()==0) {
            holderView.addView(childView);
        }
    }

    protected abstract void childConvert(ViewHolder mHolder, EMMessage mChatMessage, int mPosition);

    protected abstract View setContentView();
}
