package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.BookDetailActivity;
import com.grandmagic.readingmate.bean.response.SimpleBookInfoListResponseBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps  on 2017/3/10.
 */

public class SearchCollectBookAdapter extends CommonAdapter<SimpleBookInfoListResponseBean.SimpleBookInfo> {
    public SearchCollectBookAdapter(Context context, List datas) {
        super(context, R.layout.view_searchhis, datas);
    }


    @Override
    protected void convert(ViewHolder holder, SimpleBookInfoListResponseBean.SimpleBookInfo simpleBookInfo, int position) {
        holder.setText(R.id.bookname, "《"+simpleBookInfo.getBook_name()+"》");
        holder.setText(R.id.timeOrauthor, simpleBookInfo.getAuthor());
        final String book_id = simpleBookInfo.getBook_id();
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, BookDetailActivity.class);
                mIntent.putExtra(BookDetailActivity.BOOK_ID, book_id);
                mContext.startActivity(mIntent);
            }
        });
    }
}
