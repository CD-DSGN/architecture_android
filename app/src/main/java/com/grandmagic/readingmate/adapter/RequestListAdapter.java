package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.FriendRequestBean;
import com.grandmagic.readingmate.bean.response.InviteMessage;
import com.grandmagic.readingmate.bean.response.RequestListResponse;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/2/21.
 */
public class RequestListAdapter extends CommonAdapter<RequestListResponse> {

    public RequestListAdapter(Context context, List datas) {
        super(context, R.layout.item_requestlist, datas);
    }


    @Override
    protected void convert(ViewHolder holder, final RequestListResponse data, final int position) {
        holder.setVisible(R.id.bottomline, position == mDatas.size() - 1);
        holder.setText(R.id.name,data.getUser_name());
        holder.setText(R.id.verify,data.getMessage());
        holder.setOnClickListener(R.id.state_todo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStateListener!=null)mStateListener.accpet(data,position);
            }
        });
        ImageLoader.loadRoundImage(mContext, Environment.BASEULR_PRODUCTION+data.getAvatar_native(), (ImageView) holder.getView(R.id.avatar));
    }
    StateListener mStateListener;

    public void setStateListener(StateListener mStateListener) {
        this.mStateListener = mStateListener;
    }

    public void setdata(List<RequestListResponse> mData) {
        mDatas = mData;
        notifyDataSetChanged();
    }

    public interface StateListener{
        void accpet(RequestListResponse mData, int mPosition);
    }
}
