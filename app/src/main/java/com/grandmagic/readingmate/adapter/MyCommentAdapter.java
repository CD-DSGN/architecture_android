package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.PersonnalCommentResponseBean;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class MyCommentAdapter extends CommonAdapter<PersonnalCommentResponseBean> {
    String mUrl;
    String mUsername;

    ImageView mAvatar;

    LinearLayout mLlShare;

    private SharePopUpWindow mSharePopUpWindow;

    public MyCommentAdapter(Context context, List datas, String username, String url) {
        super(context, R.layout.item_my_comments, datas);
        mUrl = url;
        mUsername = username;
        mSharePopUpWindow = new SharePopUpWindow(mContext);
    }


    @Override
    protected void convert(ViewHolder holder, PersonnalCommentResponseBean personnalCommentResponseBean, int position) {
        if (!TextUtils.isEmpty(mUrl)) {
            ImageLoader.loadCircleImage(mContext, KitUtils.getAbsoluteUrl(mUrl), (ImageView) holder.getView(R.id.avatar));
        }
        holder.setText(R.id.nickname, mUsername);
        if (personnalCommentResponseBean != null) {
            String time = personnalCommentResponseBean.getPub_time();
            String time_str = "";
            try {
                time_str = DateUtil.timeTodate1(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.setText(R.id.time, time_str);  //显示发表评论时间
            String like_times = personnalCommentResponseBean.getLike_times();
            if (like_times == null) {
                like_times = " ";
            }
            holder.setText(R.id.tv_likenum, like_times); //设置点赞的数目
            mLlShare = holder.getView(R.id.ll_share);
            mLlShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSharePopUpWindow.show();
                }
            });

            holder.setText(R.id.tv_book_name, personnalCommentResponseBean.getBook_name());
            String book_cover = personnalCommentResponseBean.getPhoto();
            if (!TextUtils.isEmpty(book_cover)) {
                ImageLoader.loadCircleImage(mContext, KitUtils.getAbsoluteUrl(book_cover), (ImageView) holder.getView(R.id.cover));
            }
        }
    }


    public void setUrl(String url) {
        mUrl = url;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public List getList() {
        return mDatas;
    }
}
