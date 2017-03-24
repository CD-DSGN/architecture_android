package com.grandmagic.readingmate.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.BookCommentsAdapter;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.BookCommentResponse;
import com.grandmagic.readingmate.model.BookModel;
import com.tamic.novate.NovateResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.util.SimpleRefreshListener;

/**
 * Created by lps on 2017/3/14.
 * 处理详情页的评论相关。Activity里面逻辑太多
 */

public class HotcommentView extends FrameLayout implements BookCommentsAdapter.AdapterListener {
    private String order_way;
    private BookModel mModel;
    private String mBook_id;
    public static final String COMMENT_TIME = "pub_time";
    public static final String COMMENT_LIKE = "like_times";
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    private Context mContext;
    private BGAStickinessRefreshViewHolder mRefreshViewHolder;

    public HotcommentView(Context context, String flag, BookModel mModel, String mBook_id) {
        super(context);
        this.mContext = context;
        this.order_way = flag;
        this.mModel = mModel;
        this.mBook_id = mBook_id;
        loadData(currpage);
        initPagerview();
    }

    int pagecount, currpage = 1;

    private void loadData(int currpage) {
        mModel.loadBookComment(mBook_id, currpage, order_way, new AppBaseResponseCallBack<NovateResponse<BookCommentResponse>>(mContext) {
            @Override
            public void onSuccee(NovateResponse<BookCommentResponse> response) {
                pagecount = response.getData().getPageCount();
                List<BookCommentResponse.CommentsBean> mComments = response.getData().getComments();
                if (mComments!=null&&!mComments.isEmpty())
                mList.addAll(mComments);
                mAdapter.refresh();
            }
        });
    }

    public HotcommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPagerview();
    }



    List<BookCommentResponse.CommentsBean> mList;
    DefaultEmptyAdapter mAdapter;

    private void initPagerview() {
        View mInflate = LayoutInflater.from(mContext).inflate(R.layout.view_hotcomment, this);
        ButterKnife.bind(this, mInflate);
        initRefresh();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mList = new ArrayList<>();
        BookCommentsAdapter mInnerAdapter = new BookCommentsAdapter(mContext, mList);
        mInnerAdapter.setListener(this);
        this.mAdapter = new DefaultEmptyAdapter(mInnerAdapter,mContext);
        mRecyclerview.setAdapter(this.mAdapter);
    }


    private void initRefresh() {
        mRefreshViewHolder = new BGAStickinessRefreshViewHolder(mContext, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);
//        mRefreshLayout.offsetTopAndBottom(88);
        mRefreshLayout.setPullDownRefreshEnable(false);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new SimpleRefreshListener() {

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if (currpage < pagecount) {
                    currpage++;
                    loadData(currpage);
                    return true;
                } else {
                    Toast.makeText(mContext, "NOMORE", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    /**
     * 对评论点赞
     *
     * @param commentid
     */
    // FIXME: 2017/3/21 在最新或者最热点赞之后不好更新另一个view的点赞情况
    @Override
    public void thumb(String commentid, final int position) {
        mModel.thumbBookComment(commentid, new AppBaseResponseCallBack<NovateResponse>(mContext) {
            @Override
            public void onSuccee(NovateResponse response) {
                mList.get(position).setThumb_up("1");
                int mLike_times = mList.get(position).getLike_times();
                mList.get(position).setLike_times(mLike_times+1);
                mAdapter.refresh();
            }
        });
    }
}
