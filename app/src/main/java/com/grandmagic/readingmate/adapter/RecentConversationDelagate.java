package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.ChatActivity;
import com.grandmagic.readingmate.bean.response.RecentConversation;
import com.grandmagic.readingmate.utils.GlideRoundTransform;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */

public class RecentConversationDelagate implements ItemViewDelegate<EMConversation> {
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
        return item.getType()== EMConversation.EMConversationType.Chat;
    }

    @Override
    public void convert(ViewHolder holder, final EMConversation data, int position) {
        // TODO: 2017/3/2 最近会话
        holder.setText(R.id.time,data.getUnreadMsgCount()+"");
        if (data.getAllMsgCount()>0){
            final EMMessage mLastMessage = data.getLastMessage();
            holder.setText(R.id.content,mLastMessage.getBody().toString());
            holder.setText(R.id.name,mLastMessage.getFrom());

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(mContext, ChatActivity.class);
                    mIntent.putExtra(ChatActivity.CHAT_IM_NAME,mLastMessage.getFrom());
                    mIntent.putExtra(ChatActivity.CHAT_NAME,mLastMessage.getFrom());
                    mContext.startActivity(mIntent);

                }
            });
        }
    }
}
