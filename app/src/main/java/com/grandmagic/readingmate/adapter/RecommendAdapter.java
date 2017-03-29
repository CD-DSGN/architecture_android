package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by lps on 2017-3-27
 *
 * @version 1
 * @see
 * @since 13:10
 */


public class RecommendAdapter extends CommonAdapter<Contacts> {
    boolean isSignle = true;

    public RecommendAdapter(Context context, List datas) {
        super(context, R.layout.item_recommend_friend, datas);
    }

    HashMap<Integer, Boolean> mHashMap = new HashMap<>();

    @Override
    protected void convert(final ViewHolder holder, Contacts data, final int position) {
        holder.setText(R.id.name, data.getUser_name());
        ImageLoader.loadRoundImage(mContext, data.getAvatar_url().getLarge(),
                (ImageView) holder.getView(R.id.avatar));
        final View check = holder.getView(R.id.checkbtn);
        holder.setOnClickListener(R.id.checkbtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCheckState(position, check);
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null)
                    if (isSignle) {
                        mClickListener.ItemCLick(position);
                    } else {
                        changeCheckState(position, check);
                    }
            }
        });
        holder.setVisible(R.id.checkbtn, !isSignle);
    }

    private void changeCheckState(int position, View mCheck) {
        if (mHashMap.get(position) == null || !mHashMap.get(position)) {
            mCheck.setSelected(true);
            mHashMap.put(position, true);
        } else {
            mCheck.setSelected(false);
            mHashMap.put(position, false);
        }
    }

    ClickListener mClickListener;

    public void setClickListener(ClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ClickListener {
        void ItemCLick(int position);
    }

    public boolean isSignle() {
        return isSignle;
    }

    public void setSignle(boolean mSignle) {
        isSignle = mSignle;
        notifyDataSetChanged();
    }

    /**
     * 获取所有已选中的好友
     *
     * @return
     */
    public List<Integer> getSelected() {
        List<Integer> mList = new ArrayList<>();
        Iterator<Map.Entry<Integer, Boolean>> mIterator = mHashMap.entrySet().iterator();
        while (mIterator.hasNext()) {//对hashmap遍历
            Map.Entry<Integer, Boolean> mNext = mIterator.next();
            if (mNext.getValue()) {
                mList.add(mNext.getKey());
            }
        }
        return mList;
    }
}
