package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.DisplayBook;
import com.grandmagic.readingmate.consts.AppConsts;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.grandmagic.readingmate.view.StarView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by zhangmengqi on 2017/3/8.
 */

public class MyCollectBookAdapter extends CommonAdapter<DisplayBook.InfoBean> {
    OnitemDeleteListener mOnitemDeleteListener;

    public MyCollectBookAdapter(Context context, List datas, OnitemDeleteListener onitemDeleteListener) {
        super(context, R.layout.item_vp_book_details, datas);
        mOnitemDeleteListener = onitemDeleteListener;
    }


    @Override
    protected void convert(ViewHolder holder, DisplayBook.InfoBean book_info, int position) {
        if (book_info != null) {
            String url = book_info.getPhoto();

            //ImageLoader.loadCircleImage(mContext, KitUtils.getAbsoluteUrl(url), (ImageView) holder.getView(R.id.iv_book));
            ImageLoader.loadImage(mContext, KitUtils.getAbsoluteUrl(url), (ImageView) holder.getView(R.id.iv_book), R.drawable.iv_no_book_cover);


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

            int score_num = 0;
            try {
                score_num = Integer.parseInt(book_info.getScore_times());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            String str_score_num = mContext.getString(R.string.comment_num);
            if (score_num > 10000) {
                str_score_num = String.format(str_score_num, score_num / 10000 + "万");
            } else if (score_num > 1000) {
                str_score_num = String.format(str_score_num, score_num / 1000 + "千");
            } else {
                str_score_num = String.format(str_score_num, score_num);
            }

            holder.setText(R.id.tv_comment_person_num, str_score_num);
            String str_publisher = book_info.getPublisher();
            holder.setText(R.id.tv_publisher, str_publisher);

            String str_time = "";
            try {
                str_time = DateUtil.timeTodate(book_info.getPub_date());
            } catch (Exception e) {
                e.printStackTrace();
            }

            String str_publistime = str_time;
            holder.setText(R.id.tv_publistime, str_publistime);
            holder.setText(R.id.tv_book_content, book_info.getSynopsis());

            TextView tv_delete_book = holder.getView(R.id.delete_book);
            final String book_id = book_info.getBook_id();
            tv_delete_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnitemDeleteListener.deleteItem(book_id);
                }
            });

            String str_time_scan = "";
            try {
                str_time_scan = DateUtil.timeTodate(book_info.getScan_time());
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.setText(R.id.tv_time, str_time_scan);

            final DisplayBook.InfoBean book = book_info;
            holder.setOnClickListener(R.id.tv_share_book, new View.OnClickListener() {
                private SharePopUpWindow sharePopUpWindow;
                @Override
                public void onClick(View v) {
                    if (sharePopUpWindow == null) {
                        sharePopUpWindow = new SharePopUpWindow(mContext);
                    }
                    if (!TextUtils.isEmpty(book.getPhoto())) {
                        sharePopUpWindow.setData("大术读家:" + book.getBook_name(), book.getSynopsis(),
                                KitUtils.getAbsoluteUrl(book.getPhoto()), AppConsts.APP_URL, "");
                    }else{
                        sharePopUpWindow.setData("大术读家:" + book.getBook_name(), book.getSynopsis(),
                                R.drawable.iv_no_book, AppConsts.APP_URL, "");
                    }
                    sharePopUpWindow.show();
                    sharePopUpWindow.show();
                }
            });

        }
    }


    public List getList() {
        return mDatas;
    }

    public interface OnitemDeleteListener {
        void deleteItem(String id);
    }


}
