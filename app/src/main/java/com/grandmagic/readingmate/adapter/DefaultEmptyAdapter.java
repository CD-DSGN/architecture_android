package com.grandmagic.readingmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

/**
 * Created by lps on 2017/3/23.
 * 定义的默认的EmptyWrapper
 * 如果要使用其他的loadview 和emptyview可以调用
 * {@link #setLoadView(View)}和
 * {@link #setEmptyView(View)}
 * 传入自己定义的View
 *
 * @version 2
 * @see com.zhy.adapter.recyclerview.wrapper.EmptyWrapper
 * @since 2017年3月24日10:56:47
 */

public class DefaultEmptyAdapter extends EmptyWrapper {
    View mEmptyView;
    View mLoadView;
    boolean isload;

    public DefaultEmptyAdapter(RecyclerView.Adapter adapter, Context mContext) {
        super(adapter);
        mLoadView = View.inflate(mContext, R.layout.view_rv_loading, null);
        mLoadView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AutoUtils.auto(mLoadView);
        isload = true;
        mEmptyView = View.inflate(mContext, R.layout.view_rv_empty, null);
        mEmptyView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AutoUtils.auto(mEmptyView);
        setEmptyView(mLoadView);
    }

    /**
     * 刷新布局
     *
     * @see #refresh()
     * 使用这个布局，这个方法必须被调用不然会导致ItemViewType为load的时候的-1
     * 正常显示布局的时候就会导致hoder.getview()返回null，抛出 {@link #java.lang.NullPointerException}
     * 所以在此项目中一般都是先初始化InnerAdapter，然后初始化 @link #DefaultEmptyAdapter，
     * 然后从网络异步加载数据，加载的回调调用一次 {@link #refresh()}方法
     */
    public void refresh() {
        //本来{@link #setEmptyView}这个方法想在构造方法调用的，但是在异步加载数据这段时间如果给
        //用户显示一个空布局显示是不合理的，所以为了保证首次加载不显示这个空布局，将
        // 他放到异步请求的回调进行刷新的时候再做处理.如果以后有加载布局之类的再做其他处理

        //@version 1.01修改 在构造方法调用了加载loadview

        isload = false;
        setEmptyView(mEmptyView);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isload()) {
            return -1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isload()) {
            return ViewHolder.createViewHolder(parent.getContext(), mLoadView);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    /**
     * 判断是要用正在加载还是空布局
     *
     * @return
     */
    private boolean isload() {
        return isload;
    }


    public void setLoadView(View mLoadView) {
        this.mLoadView = mLoadView;
    }
}
