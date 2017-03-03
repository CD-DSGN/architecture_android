package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.bean.response.RecentConversation;
import com.grandmagic.readingmate.utils.GlideRoundTransform;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
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
    public void convert(ViewHolder holder, final EMConversation data, int position) {
        // TODO: 2017/3/2 最近会话
        holder.setText(R.id.readnum, data.getUnreadMsgCount() + "");
        String imNname = null;
        if (data.getAllMsgCount() > 0) {
            final EMMessage mLastMessage = data.getLastMessage();
            holder.setText(R.id.content, mLastMessage.getBody().toString());
            imNname = mLastMessage.direct() == EMMessage.Direct.RECEIVE ? mLastMessage.getFrom() : mLastMessage.getTo();
            Contacts mUserInfo = IMHelper.getInstance().getUserInfo(imNname);

            if (mUserInfo != null) {
                ImageLoader.loadCircleImage(mContext,
                        Environment.BASEULR_PRODUCTION+mUserInfo.getAvatar_native(),
                        (ImageView) holder.getView(R.id.avatar)
                );
                holder.setText(R.id.name, mUserInfo.getUser_name()==null?mUserInfo.getUser_id()+"":mUserInfo.getUser_name());
            }
            final String finalUsername =mUserInfo==null?"": mUserInfo.getUser_name();
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(mContext, ChatActivity.class);
                    mIntent.putExtra(ChatActivity.CHAT_IM_NAME, mLastMessage.getFrom());
                    mIntent.putExtra(ChatActivity.CHAT_NAME, finalUsername);
                    mContext.startActivity(mIntent);

                }
            });
        }
    }
}
