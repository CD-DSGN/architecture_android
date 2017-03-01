package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/2/28.
 */

public class CommentsAdapter extends CommonAdapter<String> {
    public CommentsAdapter(Context context, List datas) {
        super(context, R.layout.item_comments, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String mS, int position) {
        ImageLoader.loadRoundImage(mContext,"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=275622820,2944364039&fm=116&gp=0.jpg", (ImageView) holder.getView(R.id.avatar));
        ImageLoader.loadRoundImage(mContext,"https://ss0.baidu.com/73x1bjeh1BF3odCf/it/u=2315561548,1720195515&fm=85&s=5802DA134C5046D646A5BBF103009035", (ImageView) holder.getView(R.id.cover));
    }
}
