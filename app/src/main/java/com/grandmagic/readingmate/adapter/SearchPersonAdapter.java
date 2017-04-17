package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.FriendDetailActivity;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.bean.response.SearchPersonResponse;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/3/16.
 *
 * @since 2017年3月28日11:22:53 添加好友功能
 */

public class SearchPersonAdapter extends CommonAdapter<SearchPersonResponse.InfoBean> {
    public SearchPersonAdapter(Context context, List datas) {
        super(context, R.layout.item_searchperson, datas);
    }


    @Override
    protected void convert(ViewHolder holder, final SearchPersonResponse.InfoBean data, final int position) {
        if (data==null)return;
        holder.setText(R.id.name, data.getUser_name());
        ImageLoader.loadCircleImage(mContext, Environment.BASEULR_PRODUCTION + data.getAvatar_url().getLarge(),
                (ImageView) holder.getView(R.id.avatar));
        holder.setVisible(R.id.rela_addfriend, data.getIs_friend() != 1);
        boolean hassamebook = isHassamebook(holder, data);
        holder.setVisible(R.id.rela_sameInterest, hassamebook);
        holder.setImageResource(R.id.gender,data.getGender()==1?R.drawable.iv_male:R.drawable.iv_female);
        holder.setOnClickListener(R.id.rela_addfriend, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.addfriend(data.getUser_id(),position);
                }
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, FriendDetailActivity.class);
                Bundle mBundle = new Bundle();
                PersonInfo mInfo = new PersonInfo();
                mInfo.setAvatar(data.getAvatar_url().getLarge());
                mInfo.setClientid(data.getClientid());
                mInfo.setUser_id(data.getUser_id() + "");
                mInfo.setFriend(data.getIs_friend() == 1);
                mInfo.setNickname(data.getUser_name());
                mInfo.setSignature(data.getSignature());
                mBundle.putParcelable(FriendDetailActivity.PERSON_INFO, mInfo);
                mIntent.putExtras(mBundle);
                mContext.startActivity(mIntent);
            }
        });
    }

    private boolean isHassamebook(ViewHolder holder, SearchPersonResponse.InfoBean data) {
        boolean hassamebook = false;
        LinearLayout mLayout = holder.getView(R.id.lin_collection);
        mLayout.removeAllViews();
        for (SearchPersonResponse.InfoBean.CollectionBean coll : data.getCollection()) {
            TextView mTextView = new TextView(mContext);
            mTextView.setText(coll.getBook_name());
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,28);
            if (coll.getIs_both_enjoy() == 1) {
                hassamebook = true;
                mTextView.setTextColor(mContext.getResources().getColor(R.color.text_green));
            }
            mTextView.setSingleLine(true);
            mTextView.setEllipsize(TextUtils.TruncateAt.END);
            AutoUtils.auto(mTextView);
            mLayout.addView(mTextView);
        }
        return hassamebook;
    }

    public interface AdapterListener {
        void addfriend(int mUser_id,int pos);
    }

    AdapterListener mListener;

    public void setListener(AdapterListener mListener) {
        this.mListener = mListener;
    }
}
