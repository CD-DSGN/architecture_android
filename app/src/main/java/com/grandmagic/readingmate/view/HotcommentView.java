package com.grandmagic.readingmate.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.BookCommentsAdapter;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.BookCommentResponse;
import com.grandmagic.readingmate.event.BookStateEvent;
import com.grandmagic.readingmate.event.RefreshHotCommentEvent;
import com.grandmagic.readingmate.model.BookModel;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    PullLoadMoreRecyclerView mRecyclerview;

    private Context mContext;
    private TextView mCommentnum;


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

    public void loadData(final int mCurrpage) {
        mModel.loadBookComment(mBook_id, mCurrpage, order_way, new AppBaseResponseCallBack<NovateResponse<BookCommentResponse>>(mContext) {
            @Override
            public void onSuccee(NovateResponse<BookCommentResponse> response) {
                currpage=mCurrpage;
                if (mCurrpage==1)mList.clear();
                pagecount = response.getData().getPageCount();
                if (mCommentnum!=null&&response.getData().getTotal_num()>0)mCommentnum.setText("共计"+response.getData().getTotal_num()+"条评论");
                List<BookCommentResponse.CommentsBean> mComments = response.getData().getComments();
                if (mComments != null && !mComments.isEmpty())
                    mList.addAll(mComments);
                mAdapter.refresh();
              mRecyclerview.setPullLoadMoreCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mAdapter.refresh();
              mRecyclerview.setPullLoadMoreCompleted();
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
        mRecyclerview.setLinearLayout();
        mList = new ArrayList<>();
        BookCommentsAdapter mInnerAdapter = new BookCommentsAdapter(mContext, mList);
        mInnerAdapter.setListener(this);
        this.mAdapter = new DefaultEmptyAdapter(mInnerAdapter, mContext);
        mRecyclerview.setAdapter(this.mAdapter);
    }


    private void initRefresh() {
     mRecyclerview.setPullRefreshEnable(false);
        mRecyclerview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (currpage < pagecount) {
                    currpage++;
                    loadData(currpage);
                } else {
                   mRecyclerview.setPullLoadMoreCompleted();
                }
            }
        });
    }

    /**
     * 对评论点赞
     *
     * @param commentid
     */
    //
    @Override
    public void thumb(String commentid, final int position) {
        mModel.thumbBookComment(commentid, new AppBaseResponseCallBack<NovateResponse>(mContext) {
            @Override
            public void onSuccee(NovateResponse response) {
                mList.get(position).setThumb_up("1");
                int mLike_times = mList.get(position).getLike_times();
                mList.get(position).setLike_times(mLike_times + 1);
                mAdapter.refresh();
                if (order_way.equals(COMMENT_TIME))
                EventBus.getDefault().post(new RefreshHotCommentEvent());
                EventBus.getDefault().post(new BookStateEvent(BookStateEvent.STATE_MOVE,mBook_id));

            }
        });
    }

    public void addNumView(TextView mCommentnum) {

        this.mCommentnum = mCommentnum;
    }
}
