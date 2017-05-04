package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.db.ChatDraftBox;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.db.ChatDraftBoxDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.tamic.novate.util.Environment;
import com.tamic.novate.util.SPUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.codec.StringEncoder;

/**
 * Created by lps on 2017/2/22.
 * 最近会话列表的处理
 */

public class RecentConversationDelagate implements ItemViewDelegate<EMConversation> {
    private static final String TAG = RecentConversationDelagate.class.getName();
    private Context mContext;

    public RecentConversationDelagate(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recentconversation;
    }

    @Override
    public boolean isForViewType(EMConversation item, int position) {
        return item.getType() == EMConversation.EMConversationType.Chat;

    }

    @Override
    public void convert(ViewHolder holder, final EMConversation data, final int position) {
        //显示当前会话的未读的消息数量
        int mUnreadMsgCount = data.getUnreadMsgCount();
        holder.setVisible(R.id.unread, mUnreadMsgCount > 0);
        holder.setText(R.id.unread, mUnreadMsgCount + "");
        String imNname = null;
        if (data.getAllMsgCount() > 0) {
            final EMMessage mLastMessage = data.getLastMessage();

            if (mLastMessage.getType() == EMMessage.Type.TXT) {
                holder.setText(R.id.content, ((EMTextMessageBody) mLastMessage.getBody()).getMessage());
            } else if (mLastMessage.getType() == EMMessage.Type.IMAGE) {
                holder.setText(R.id.content, "[图片]");
            } else if (mLastMessage.getType() == EMMessage.Type.VOICE) {
                holder.setText(R.id.content, "[语音]");
            }
            imNname = data.conversationId();
            String mImName = SPUtils.getInstance().getString(mContext, SPUtils.IM_NAME);
            if (imNname.equals(mImName)) {//这里显示的时候要显示对方的信息而不是自己的
                imNname = mLastMessage.getFrom().equals(imNname) ? mLastMessage.getTo() : mLastMessage.getFrom();
            }
            ChatDraftBox mChatDraftBox = DBHelper.getChatDraftBoxDao(mContext).queryBuilder().
                    where(ChatDraftBoxDao.Properties.Tochatuserid.eq(imNname), ChatDraftBoxDao.Properties.MType.eq(mLastMessage.getChatType()))
                    .build().unique();
            DBHelper.close();
            holder.setVisible(R.id.draft, mChatDraftBox != null);
            if (mChatDraftBox != null) {
                holder.setText(R.id.content, mChatDraftBox.getTxt());
            }
            //从本地获取联系人信息
            final Contacts mUserInfo = IMHelper.getInstance().getUserInfo(imNname);

            if (mUserInfo != null) {
                ImageLoader.loadCircleImage(mContext,
                        Environment.BASEULR_PRODUCTION + mUserInfo.getAvatar_url().getLarge(),
                        (ImageView) holder.getView(R.id.avatar)
                );
                holder.setText(R.id.name, mUserInfo.getUser_name() == null ? mUserInfo.getUser_id() + "" : mUserInfo.getUser_name());
            }
            final String finalUsername = mUserInfo == null ? "" : mUserInfo.getUser_id()+"";
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecentConversationListener.onitemclick(mLastMessage, finalUsername, position);
                }
            });
            holder.setOnClickListener(R.id.delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUserInfo != null) {
                        mRecentConversationListener.delete(mUserInfo.getUser_id() + "");
                    }
                }
            });
        }
    }


    RecentConversationListener mRecentConversationListener;

    public void setRecentConversationListener(RecentConversationListener mRecentConversationListener) {
        this.mRecentConversationListener = mRecentConversationListener;
    }

    public interface RecentConversationListener {
        void delete(String username);

        void onitemclick(EMMessage mLastMessage, String mFinalUsername, int position);
    }
}
