package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.FriendDetailActivity;
import com.grandmagic.readingmate.bean.response.SearchPersonResponse;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by dangxiaohui on 2017/3/16.
 */

public class SearchPersonAdapter extends CommonAdapter<SearchPersonResponse.InfoBean> {
    public SearchPersonAdapter(Context context, List datas) {
        super(context, R.layout.item_searchperson, datas);
    }


    @Override
    protected void convert(ViewHolder holder, final SearchPersonResponse.InfoBean data, int position) {
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, FriendDetailActivity.class);
                mIntent.putExtra(FriendDetailActivity.USER_ID, data.getUser_id());
                mContext.startActivity(mIntent);
            }
        });
    }
}
