package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.utils.GlideRoundTransform;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/22.
 */
public class MessageTextSendDelagate implements ItemViewDelegate<EMMessage> {
    private static final String TAG = "MessageTextSendDelagate";
    private Context mContext;

    public MessageTextSendDelagate(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_sendtextmsg;
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        return item.getType() == EMMessage.Type.TXT && item.direct() == EMMessage.Direct.SEND;
    }

    @Override
    public void convert(final ViewHolder holder, EMMessage mChatMessage, int position) {
        EMTextMessageBody mBody = (EMTextMessageBody) mChatMessage.getBody();
        holder.setText(R.id.content, mBody.getMessage());

    }


}
