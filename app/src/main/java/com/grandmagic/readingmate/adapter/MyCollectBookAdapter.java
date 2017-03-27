package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.DisplayBook;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.view.StarView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class MyCollectBookAdapter extends CommonAdapter<DisplayBook.InfoBean> {
    public MyCollectBookAdapter(Context context, List datas) {
        super(context, R.layout.item_vp_book_details, datas);
    }


    @Override
    protected void convert(ViewHolder holder, DisplayBook.InfoBean book_info, int position) {
        if (book_info != null) {
            String url = book_info.getPhoto();
            if (!TextUtils.isEmpty(url)) {
                ImageLoader.loadCircleImage(mContext, KitUtils.getAbsoluteUrl(url), (ImageView) holder.getView(R.id.iv_book));
            }
            holder.setText(R.id.tv_book_name, book_info.getBook_name());
            holder.setText(R.id.tv_book_author, book_info.getAuthor());
            StarView starView = holder.getView(R.id.ratingbar);
            float score = 10;
            try {
                score = Float.parseFloat(book_info.getTotal_score());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            starView.setScore(score);
            holder.setText(R.id.tv_book_score, score + "");
            holder.setText(R.id.tv_comment_person_num, book_info.getScore_times());
            holder.setText(R.id.tv_publisher, book_info.getPublisher());

            try {
                holder.setText(R.id.tv_publistime, DateUtil.timeTodate(book_info.getPub_date()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.setText(R.id.tv_book_content, book_info.getSynopsis());

        }

    }

    public List getList() {
        return mDatas;
    }
}
