package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.ReplyInfoResponseBean;
import com.grandmagic.readingmate.ui.SpanTextView;
import com.grandmagic.readingmate.ui.mLinkMovementMethod;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class CommentDetailAdapter extends CommonAdapter<ReplyInfoResponseBean.InfoBean> {

    public CommentDetailAdapter(Context context, List datas) {
        super(context, R.layout.item_replay, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ReplyInfoResponseBean.InfoBean data, final int position) {
        ImageView fromAvar = holder.getView(R.id.iv_replay_header);

        ReplyInfoResponseBean.InfoBean.FromUserAvatarUrlBean from_avar = data.getFrom_user_avatar_url();
        if (from_avar != null) {
            String from_avar_url = from_avar.getLarge();
            ImageLoader.loadCircleImage(mContext, KitUtils.getAbsoluteUrl(from_avar_url), fromAvar);
        }

        holder.setText(R.id.tv_nickname_reply, data.getFrom_user_name());
        StringBuilder content = new StringBuilder(data.getReply_content());
        if (!TextUtils.isEmpty(data.getTo_user_name())) {
            content.append("@" + data.getTo_user_name());
        }

        SpanTextView spanTextView = holder.getView(R.id.tv_signature_reply);
        if (!TextUtils.isEmpty(data.getTo_user_comment())) {
            spanTextView.setText(content.toString(), ":   "+data.getTo_user_comment());
        }else{
            spanTextView.setText(content.toString());
        }

        spanTextView.setMovementMethod(mLinkMovementMethod.getInstance(context));

        try {
            holder.setText(R.id.tv_time_replay, DateUtil.timeTodate(data.getReply_pub_time()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tv_reply = holder.getView(R.id.tv_reply);
        tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnReplyListener.onReplyClick(position - 1);   //回复按钮响应, 因为有header的缘故，实际上应该减1
            }
        });
    }

    public interface OnReplyListener {
        public void onReplyClick(int position);
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        mOnReplyListener = onReplyListener;
    }

    private OnReplyListener mOnReplyListener;


}
