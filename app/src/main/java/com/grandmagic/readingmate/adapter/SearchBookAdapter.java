package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.BookDetailActivity;
import com.grandmagic.readingmate.bean.response.BookSearchResponse;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps  on 2017/3/10.
 */

public class SearchBookAdapter extends CommonAdapter<BookSearchResponse.SearchResultBean> {
    public SearchBookAdapter(Context context, List datas) {
        super(context, R.layout.view_searchhis, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final BookSearchResponse.SearchResultBean data, int position) {
        holder.setText(R.id.bookname, "《"+data.getBook_name()+"》");
        holder.setText(R.id.timeOrauthor, data.getAuthor());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, BookDetailActivity.class);
                mIntent.putExtra(BookDetailActivity.BOOK_ID,data.getBook_id());
                mContext.startActivity(mIntent);
            }
        });
    }
}
