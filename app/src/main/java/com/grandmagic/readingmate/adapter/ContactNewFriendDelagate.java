package com.grandmagic.readingmate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.FriendActivity;
import com.grandmagic.readingmate.activity.FriendRequestActivity;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/2/21.
 */

public class ContactNewFriendDelagate implements ItemViewDelegate<Contacts> {
    private Context mContext;

    public ContactNewFriendDelagate(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.new_friend_hint;
    }

    @Override
    public boolean isForViewType(Contacts item, int position) {
        return item.getType() == Contacts.TYPE.TYPE_NEWFRIEND;
    }

    @Override
    public void convert(ViewHolder holder, Contacts mContacts, int position) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).startActivityForResult(new Intent(mContext, FriendRequestActivity.class)
                , FriendActivity.REQUEST_NEWFRIEND);
            }
        });

    }
}
