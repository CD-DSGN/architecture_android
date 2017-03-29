package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMMessage;
import com.orhanobut.logger.Logger;
import com.tamic.novate.util.Environment;
import com.tamic.novate.util.SPUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/3/6.
 * 通用的消息类型代理
 * 点击头像等事件后续放到这里面处理
 */

public abstract class ChatItemViewDelegate implements ItemViewDelegate<EMMessage> {
    private static final String TAG = "ChatItemViewDelegate";
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
    public void convert(ViewHolder holder, final EMMessage mChatMessage, int position) {
        long mMsgTime = mChatMessage.getMsgTime();
        holder.setText(R.id.time, DateUtil.timeTodate(mChatMessage.getMsgTime() + ""));
        Contacts mUserInfo;
        if (mDirect == EMMessage.Direct.SEND) {
mUserInfo=IMHelper.getInstance().getUserInfo(SPUtils.getInstance().getString(mContext,SPUtils.IM_NAME));
        } else {
            mUserInfo = IMHelper.getInstance()
                    .getUserInfo(mChatMessage.getFrom());
        }


        if (mUserInfo != null) {
            holder.setText(R.id.name, mUserInfo.getUser_name());
            ImageLoader.loadRoundImage(mContext,
                    Environment.BASEULR_PRODUCTION + mUserInfo.getAvatar_url().getLarge(),
                    (ImageView) holder.getView(R.id.avatar)
            );
        }
        holder.setOnClickListener(R.id.avatar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChatClickListener != null)
                    mChatClickListener.clickAvatar(mChatMessage.getFrom());
            }
        });
        final View statesView = holder.getView(R.id.send_status);
        final View progress = holder.getView(R.id.status_prgress);
        RelativeLayout holderView = holder.getView(R.id.contentView);
        View childView = setContentView(mChatMessage,holderView);
        if (childView!=null) {
            ViewHolder mHolder = ViewHolder.createViewHolder(mContext, childView);
            childConvert(mHolder, mChatMessage, position);
        }
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        statesView.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        break;
                    case 1:
                        statesView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        break;
                    case 2:
                        progress.setVisibility(View.VISIBLE);
                        break;

                }
            }
        };
        mChatMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                mHandler.hasMessages(0);
            }

            @Override
            public void onError(int mI, String mS) {
                mHandler.hasMessages(1);
            }

            @Override
            public void onProgress(int mI, String mS) {
                mHandler.hasMessages(2);
            }

        });
    }

    protected abstract void childConvert(ViewHolder mHolder, EMMessage mChatMessage, int mPosition);

    protected abstract View setContentView(EMMessage mChatMessage, RelativeLayout mHolderView);

    protected abstract boolean isForViewType(EMMessage mItem);

    chatClickListener mChatClickListener;

    public ChatItemViewDelegate setChatClickListener(chatClickListener mChatClickListener) {
        this.mChatClickListener = mChatClickListener;
        return this;
    }

    public interface chatClickListener {//一些点击事件的接口

        void clickAvatar(String mFrom);
    }
}
