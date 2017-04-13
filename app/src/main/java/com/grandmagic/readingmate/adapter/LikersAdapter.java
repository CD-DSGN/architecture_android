package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.LikersInfoResponseBean;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;



public class LikersAdapter extends CommonAdapter<LikersInfoResponseBean.InfoBean> {

    public LikersAdapter(Context context, List datas) {
        super(context, R.layout.item_likes_info, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LikersInfoResponseBean.InfoBean data, final int position) {
        LikersInfoResponseBean.InfoBean.AvatarUrlBean avar = data.getAvatar_url();
        String url = "";
        if (avar != null) {
            url = avar.getLarge();
        }

        ImageLoader.loadRoundImage(mContext, KitUtils.getAbsoluteUrl(url), (ImageView) holder.getView(R.id.avatar));
        holder.setText(R.id.name, data.getUser_name());
        holder.setVisible(R.id.add_friend, data.getIs_friend() != 1);
        holder.setText(R.id.signature, data.getSignature());
        holder.setOnClickListener(R.id.add_friend, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLisenter != null) mLisenter.addFriend(position);
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLisenter != null) mLisenter.onItemClick(position);
            }
        });
    }

    AdapterLisenter mLisenter;

    public AdapterLisenter getLisenter() {
        return mLisenter;
    }

    public void setLisenter(AdapterLisenter mLisenter) {
        this.mLisenter = mLisenter;
    }

    public interface AdapterLisenter {
        void onItemClick(int position);

        void addFriend(int position);
    }
}
