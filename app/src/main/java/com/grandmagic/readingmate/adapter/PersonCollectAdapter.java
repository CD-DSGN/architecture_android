package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.PersonCollectBookResponse;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by lps on 2017/3/23.
 */

public class PersonCollectAdapter extends CommonAdapter<PersonCollectBookResponse.InfoBean>{
    public PersonCollectAdapter(Context context,  List datas) {
        super(context, R.layout.item_person_collect, datas);
    }


    @Override
    protected void convert(ViewHolder holder, PersonCollectBookResponse.InfoBean data, int position) {
        ImageLoader.loadBookImg(mContext, Environment.BASEULR_PRODUCTION+data.getPhoto(), (ImageView) holder.getView(R.id.conver));
        holder.setText(R.id.bookname,data.getBook_name());
    }
}
