package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.BookFollowersResponse;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.model.SearchModel;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by lps on 2017/3/20.
 */

public class CollectionAdapter extends CommonAdapter<BookFollowersResponse.InfoBean> {

    public CollectionAdapter(Context context, List datas) {
        super(context, R.layout.item_collect_person, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BookFollowersResponse.InfoBean data, final int position) {
        ImageLoader.loadRoundImage(mContext, Environment.BASEULR_PRODUCTION + data.getAvatar_url().getMid(), (ImageView) holder.getView(R.id.avatar));
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
