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
import com.hyphenate.EMCallBack;
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
        return mDirect == EMMessage.Direct.RECEIVE ? R.layout.item_common_recivemsg : R.layout.item_common_sendmsg;
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        mDirect = item.direct();
        return isForViewType(item);
    }

    @Override
    public void convert(ViewHolder holder, EMMessage mChatMessage, int position) {
        Contacts mUserInfo = IMHelper.getInstance()
                .getUserInfo(mChatMessage.getFrom());
        if (mUserInfo != null) {
            ImageLoader.loadRoundImage(mContext,
                    Environment.BASEULR_PRODUCTION + mUserInfo.getAvatar_native(),
                    (ImageView) holder.getView(R.id.avatar)
            );
        }
        holder.setOnClickListener(R.id.avatar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChatClickListener != null) mChatClickListener.clickAvatar();
            }
        });
        final View statesView = holder.getView(R.id.send_status);
        final View progress = holder.getView(R.id.status_prgress);
        View childView = setContentView();
        ViewHolder mHolder = ViewHolder.createViewHolder(mContext, childView);
        RelativeLayout holderView = holder.getView(R.id.contentView);
        childConvert(mHolder, mChatMessage, position);
        mChatMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                statesView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onError(int mI, String mS) {
                statesView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onProgress(int mI, String mS) {
                progress.setVisibility(View.VISIBLE);

            }

        });
        holderView.addView(childView);
    }

    protected abstract void childConvert(ViewHolder mHolder, EMMessage mChatMessage, int mPosition);

    protected abstract View setContentView();

    protected abstract boolean isForViewType(EMMessage mItem);

    chatClickListener mChatClickListener;

    public ChatItemViewDelegate setChatClickListener(chatClickListener mChatClickListener) {
        this.mChatClickListener = mChatClickListener;
        return this;
    }

    public interface chatClickListener {//一些点击事件的接口

        void clickAvatar();
    }
}
