package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.FriendRequestBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/2/21.
 */
public class RequestListAdapter extends CommonAdapter<FriendRequestBean> {


    public RequestListAdapter(Context context, List datas) {
        super(context, R.layout.item_requestlist, datas);
    }


    @Override
    protected void convert(ViewHolder holder, FriendRequestBean data, int position) {
        Glide.with(mContext).load(data.getAvatar()).into((ImageView) holder.getView(R.id.avatar));
        holder.setVisible(R.id.bottomline, position == mDatas.size() - 1);
        holder.setVisible(R.id.state_deal, data.getState()==0);
        holder.setVisible(R.id.state_todo, data.getState()==1);

    }
}
