package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.BookDetailActivity;
import com.grandmagic.readingmate.activity.CommentsActivity;
import com.grandmagic.readingmate.bean.response.NotificationCommentResponse;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by lps on 2017/4/6.
 *
 * @version 1
 * @see
 * @since 2017/4/6 13:40
 */


public class NotificationThumbDelagte implements ItemViewDelegate<NotificationCommentResponse.InfoBean> {
    private Context mContext;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_notification_comment;
    }

    public NotificationThumbDelagte(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean isForViewType(NotificationCommentResponse.InfoBean item, int position) {
        return item.getType()==2;
    }

    @Override
    public void convert(ViewHolder holder, final NotificationCommentResponse.InfoBean data, int position) {
        holder.setText(R.id.nickname, data.getUser_name());
        holder.setText(R.id.time, DateUtil.timeTodate(data.getTime()));
        ImageLoader.loadRoundImage(mContext, Environment.BASEULR_PRODUCTION+data.getAvatar_url().getMid(),
                (ImageView) holder.getView(R.id.avatar));
        TextView title=holder.getView(R.id.title);
        title.setText(getTitle(data));
        title.setMovementMethod(LinkMovementMethod.getInstance());
        holder.setText(R.id.content,data.getNative_comment());
        holder.setOnClickListener(R.id.content,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CommentsActivity.class);
                mIntent.putExtra(CommentsActivity.COMMENT_ID,data.getComment_id()+"");
                mContext.startActivity(mIntent);
            }
        });
    }

    private SpannableStringBuilder getTitle(final NotificationCommentResponse.InfoBean mData) {

        SpannableStringBuilder mBuilder=new SpannableStringBuilder();
        mBuilder.append("觉得你在");
        mBuilder.append("《"+mData.getBook_name()+"》");
        mBuilder.append("中的评论很赞");
        mBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#1cc9a2")),4,6+mData.getBook_name().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent mIntent = new Intent(mContext, BookDetailActivity.class);
                mIntent.putExtra(BookDetailActivity.BOOK_ID,mData.getBook_id()+"");
                mContext.startActivity(mIntent);
            }
        }, 4, 6 + mData.getBook_name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return mBuilder;
    }


}
