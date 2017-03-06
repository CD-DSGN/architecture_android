package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ChatMessage;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.IMHelper;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/27.
 */
public class MessageIMGDelagate extends ChatItemViewDelegate {


    public MessageIMGDelagate(Context mContext) {
        super(mContext);
    }

    @Override
    public boolean isForViewType(EMMessage item, int position) {
        return item.getType() == EMMessage.Type.IMAGE;
    }

    @Override
    protected void childConvert(ViewHolder mHolder, EMMessage data, int mPosition) {
        EMImageMessageBody  mBody= (EMImageMessageBody) data.getBody();
        ImageLoader.loadRoundImage(mContext,mBody.getRemoteUrl(), (ImageView) mHolder.getView(R.id.image));
    }

    @Override
    protected View setContentView() {
        return LayoutInflater.from(mContext).inflate(mDirect== EMMessage.Direct.RECEIVE?R.layout.item_reciveimgmsg:R.layout.item_sendimgmsg,null);
    }


}
