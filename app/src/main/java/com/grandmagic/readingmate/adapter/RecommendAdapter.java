package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by lps on 2017-3-27
 *
 * @version 1
 * @see
 * @since 13:10
 */


public class RecommendAdapter extends CommonAdapter<Contacts> {
    public RecommendAdapter(Context context, List datas) {
        super(context, R.layout.item_recommend_friend, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Contacts data, final int position) {
        holder.setText(R.id.name, data.getUser_name());
        ImageLoader.loadRoundImage(mContext, Environment.BASEULR_PRODUCTION + data.getAvatar_native(),
                (ImageView) holder.getView(R.id.avatar));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.ItemCLick(position);
            }
        });
    }

    ClickListener mClickListener;

    public void setClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ClickListener {
        void ItemCLick(int position);
    }
}
