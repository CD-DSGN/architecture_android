package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.FriendDetailActivity;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.bean.response.SearchPersonResponse;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
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
        holder.setText(R.id.name,data.getUser_name());
        ImageLoader.loadCircleImage(mContext, Environment.BASEULR_PRODUCTION+data.getAvatar_url().getLarge(),
                (ImageView) holder.getView(R.id.avatar));
        holder.setVisible(R.id.rela_addfriend,data.getIs_friend()!=1);
        boolean hassamebook = isHassamebook(holder, data);
        holder.setVisible(R.id.rela_sameInterest,hassamebook);

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, FriendDetailActivity.class);
                Bundle mBundle=new Bundle();
                PersonInfo mInfo=new PersonInfo();
                mInfo.setAvatar(data.getAvatar_url().getLarge());
                mInfo.setClientid(data.getClientid());
                mInfo.setUser_id(data.getUser_id()+"");
                mInfo.setFriend(data.getIs_friend()==1);
                mInfo.setNickname(data.getUser_name());
                mBundle.putParcelable(FriendDetailActivity.PERSON_INFO,mInfo);
                mIntent.putExtras(mBundle);
                mContext.startActivity(mIntent);
            }
        });
    }

    private boolean isHassamebook(ViewHolder holder, SearchPersonResponse.InfoBean data) {
        boolean hassamebook=false;
        LinearLayout mLayout = holder.getView(R.id.lin_collection);
        mLayout.removeAllViews();
        for (SearchPersonResponse.InfoBean.CollectionBean coll:data.getCollection()) {
            TextView mTextView = new TextView(mContext);
            mTextView.setText("《" + coll.getBook_name() + "》");
            if (coll.getIs_both_enjoy() == 1) {
                hassamebook = true;
                mTextView.setTextColor(mContext.getResources().getColor(R.color.text_green));
            }
        mLayout.addView(mTextView);
        }
        return hassamebook;
    }
}
