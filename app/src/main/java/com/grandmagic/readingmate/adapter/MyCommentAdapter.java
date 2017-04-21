package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private String mUsername;

    ImageView mAvatar;

    LinearLayout mLlShare;

    public SharePopUpWindow mSharePopUpWindow;
    private OnitemDeleteListener mOnitemDeleteListener;

    public MyCommentAdapter(Context context, List datas, String username, String url, OnitemDeleteListener onitemDeleteListener) {
        super(context, R.layout.item_my_comments, datas);
        mUrl = url;
        mUsername = username;
        mSharePopUpWindow = new SharePopUpWindow(mContext);
        mOnitemDeleteListener = onitemDeleteListener;
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
                time_str = DateUtil.timeTodate(DateUtil.SLANT_PATTERN,time);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.setText(R.id.time, time_str);  //显示发表评论时间
            String like_times = personnalCommentResponseBean.getLike_times();
            if (like_times == null) {
                like_times = "0";
            }

            holder.setText(R.id.tv_likenum, like_times); //设置点赞的数目
            mLlShare = holder.getView(R.id.ll_share);
            final PersonnalCommentResponseBean p = personnalCommentResponseBean;
            mLlShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mSharePopUpWindow.setData(
//                            p.getBook_name(),
//                            p.getContent(), KitUtils.getAbsoluteUrl(p.getPhoto()), "https://a.mlinks.cc/AKVC?book_id=3", "分享书评");

                    mSharePopUpWindow.setCommentData(p.getBook_name(), p.getComment_id(), p.getContent(),
                            p.getPhoto(), p.getAverage_score());

                    mSharePopUpWindow.show();
                }
            });

            holder.setText(R.id.tv_book_name, personnalCommentResponseBean.getBook_name());
            String book_cover = personnalCommentResponseBean.getPhoto();
            if (!TextUtils.isEmpty(book_cover)) {
                ImageLoader.loadImage(mContext, KitUtils.getAbsoluteUrl(book_cover), (ImageView) holder.getView(R.id.cover));
            }

            holder.setText(R.id.content, personnalCommentResponseBean.getContent());

            TextView tv_delete = holder.getView(R.id.delete);
            final PersonnalCommentResponseBean tmp = personnalCommentResponseBean;
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnitemDeleteListener.onDelete(tmp);
                }
            });

            holder.setText(R.id.replynum, personnalCommentResponseBean.getReply_count() + mContext.getString(R.string.reply_num));


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

    public interface OnitemDeleteListener {
        public void onDelete(PersonnalCommentResponseBean personnalCommentResponseBean);
    }

}
