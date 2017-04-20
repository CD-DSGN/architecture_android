package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.event.RefreshHotCommentEvent;
import com.grandmagic.readingmate.utils.AutoUtils;
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

import org.greenrobot.eventbus.EventBus;

import static com.hyphenate.chat.EMMessage.Status.CREATE;

/**
 * Created by lps on 2017/3/6.
 * 通用的消息类型代理
 * 点击头像等事件后续放到这里面处理
 */

public abstract class ChatItemViewDelegate implements ItemViewDelegate<EMMessage> {
    private static final String TAG = "ChatItemViewDelegate";
    EMMessage.Direct mDirect;
    Context mContext;
    long premsgtime;//上一条消息的时间戳

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
        holder.setText(R.id.time, DateUtil.timeTodate(mChatMessage.getMsgTime() + ""));
        //间隔小于5min不显示时间戳
        final View statesView = holder.getView(R.id.send_status);
        final View progress = holder.getView(R.id.status_prgress);
        if (position == 0 || Math.abs(mChatMessage.getMsgTime() - premsgtime) > 60*5*1000) {
            holder.setVisible(R.id.time, true);
            premsgtime = mChatMessage.getMsgTime();
        } else holder.setVisible(R.id.time, false);
        Contacts mUserInfo;
        if (mDirect == EMMessage.Direct.SEND) {
            mUserInfo = IMHelper.getInstance().getUserInfo(SPUtils.getInstance().getString(mContext, SPUtils.IM_NAME));
            Log.e(TAG, "convert: "+mChatMessage.status() );
            switch (mChatMessage.status()) {

                case CREATE:
                    progress.setVisibility(View.GONE);
                    statesView.setVisibility(View.VISIBLE);

                    break;
                case SUCCESS:
                    progress.setVisibility(View.GONE);
                    statesView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progress.setVisibility(View.GONE);
                    statesView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progress.setVisibility(View.GONE);//// FIXME: 2017/4/19 应该设置为显示的。但有时候发送图片不走回调
                    statesView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
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

        RelativeLayout holderView = holder.getView(R.id.contentView);
        holderView.removeAllViews();
        View childView = setContentView(mChatMessage, holderView);
        if (childView != null) {
            ViewHolder mHolder = ViewHolder.createViewHolder(mContext, childView);
            childConvert(mHolder, mChatMessage, position);
        }
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
