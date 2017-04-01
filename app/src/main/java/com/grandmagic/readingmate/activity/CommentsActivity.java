package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommentDetailAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.AddReplyRequestBean;
import com.grandmagic.readingmate.bean.response.CommentsDetailResponoseBean;
import com.grandmagic.readingmate.bean.response.ReplyInfoResponseBean;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.model.CommentDetailModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.DensityUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.Page;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.umeng.socialize.UMShareAPI;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class CommentsActivity extends AppBaseActivity implements View.OnLayoutChangeListener {
    public static final String COMMENT_ID = "comment_id";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rv_comments_detail)
    RecyclerView mRvCommentsDetail;
    @BindView(R.id.lin_share)
    LinearLayout mLinShare;
    @BindView(R.id.BGARefreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.bottomlayout)
    LinearLayout mBottomlayout;


    private View mView;
    private CommentDetailAdapter mMAdapter;
    private HeaderAndFooterWrapper mMHeaderAndFooterWrapper;
    private SharePopUpWindow mPopupWindow;
    private LinearLayout ll_likers_info;

    CommentDetailModel mCommentDetailModel;
    String mCommentID;
    ImageView mAvar;
    TextView mNickName;
    TextView mTime;
    TextView mReplyNum;
    TextView mContent;
    ImageView mCover;
    TextView mBookName;
    TextView mGoodsNum;
    ImageView mApostrophe;

    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ArrayList<ImageView> iv_list;

    CommentsDetailResponoseBean mCommentsDetailResponoseBean;
    AppBaseResponseCallBack mReplyCallBack;
    Page mPage;

    BGAStickinessRefreshViewHolder mRefreshViewHolder;

    List<ReplyInfoResponseBean.InfoBean> mReplys = new ArrayList<>();

    BookModel mModel;
    int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        setSystemBarColor(R.color.white);
        initData();
        initView();
        loadData();
    }

    private void initData() {
        mCommentID = getIntent().getStringExtra("comment_id");
        if (mCommentDetailModel == null) {
            mCommentDetailModel = new CommentDetailModel(this);
        }

        mPage = new Page(mReplys, CommentDetailModel.PAGE_COUNT_REPLYS);

        if (mReplyCallBack == null) {
            mReplyCallBack = new AppBaseResponseCallBack<NovateResponse<ReplyInfoResponseBean>>(CommentsActivity.this, true) {
                @Override
                public void onSuccee(NovateResponse<ReplyInfoResponseBean> response) {
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.endLoadingMore();
                    ReplyInfoResponseBean replyInfoResponseBean = response.getData();
                    try {
                        mPage.total_num = Integer.parseInt(replyInfoResponseBean.getTotal_num());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    List<ReplyInfoResponseBean.InfoBean> list = replyInfoResponseBean.getInfo();
                    if (list != null) {
                        if (mReplyCallBack.isRefresh) {
                            mPage.refresh(list);
                        } else {
                            mPage.more(list);
                        }
                        mMHeaderAndFooterWrapper.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mRefreshLayout.endRefreshing();
                    mRefreshLayout.endLoadingMore();
                }
            };
        }

        if (mModel == null) {
            mModel = new BookModel(this);
        }

    }

    private void loadData() {
        mCommentDetailModel.getCommentDetail(mCommentID, new AppBaseResponseCallBack<NovateResponse<CommentsDetailResponoseBean>>(CommentsActivity.this) {
            @Override
            public void onSuccee(NovateResponse<CommentsDetailResponoseBean> response) {
                mCommentsDetailResponoseBean = response.getData();
                setHeaderView();
            }
        });

        mCommentDetailModel.getAllReplys(mCommentID, mReplyCallBack, 1);
        mReplyCallBack.isRefresh = false;
    }

    private void setHeaderView() {
        String url = "";
        CommentsDetailResponoseBean.AvatarUrlBean avatarUrlBean = mCommentsDetailResponoseBean.getAvatar_url();
        if (avatarUrlBean != null) {
            url = avatarUrlBean.getLarge();
        }
        ImageLoader.loadCircleImage(this, KitUtils.getAbsoluteUrl(url), mAvar);  //加载评论的头像
        mNickName.setText(mCommentsDetailResponoseBean.getUser_name());
        try {
            mTime.setText(DateUtil.timeTodate(mCommentsDetailResponoseBean.getPub_time() + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBookName.setText(mCommentsDetailResponoseBean.getBook_name());
        mReplyNum.setText(mCommentsDetailResponoseBean.getReply_count() + "人回复");
        mContent.setText(mCommentsDetailResponoseBean.getContent());
        String cover_url = mCommentsDetailResponoseBean.getPhoto();
        ImageLoader.loadCircleImage(this, KitUtils.getAbsoluteUrl(cover_url), mCover);
        int like_num = mCommentsDetailResponoseBean.getLike_times();
        mGoodsNum.setText(like_num + "人赞过");
        if (like_num > 4) {   //显示省略号
            mApostrophe.setVisibility(View.VISIBLE);
        } else {
            mApostrophe.setVisibility(View.INVISIBLE);
        }

        List<CommentsDetailResponoseBean.ThumbUserAvatarBean> thumbUserAvatarBeenList = mCommentsDetailResponoseBean.getThumb_user_avatar();
        hideLikers();
        if (thumbUserAvatarBeenList != null) {
            int size = thumbUserAvatarBeenList.size();
            for (int i = 0; i < 4 && i < size; i++) {
                CommentsDetailResponoseBean.ThumbUserAvatarBean bean = thumbUserAvatarBeenList.get(i);
                CommentsDetailResponoseBean.ThumbUserAvatarBean.AvatarUrlBeanX avatar_url = bean.getAvatar_url();
                String url_tmp = avatar_url.getLarge();
                ImageView iv = iv_list.get(i);
                ImageLoader.loadCircleImage(CommentsActivity.this, KitUtils.getAbsoluteUrl(url_tmp), iv);
                iv.setVisibility(View.VISIBLE);
            }
        }


    }

    private void hideLikers() {
        for (ImageView iv : iv_list) {
            iv.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        mTitle.setText(R.string.comment_detail);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvCommentsDetail.setLayoutManager(linearLayoutManager);
        mView = LayoutInflater.from(this).inflate(R.layout.item_comments_detail, mRvCommentsDetail, false);
        initHeaderView();
        AutoUtils.auto(mView);
        mMAdapter = new CommentDetailAdapter(this, mReplys);
        mMAdapter.setOnReplyListener(new CommentDetailAdapter.OnReplyListener() {
            @Override
            public void onReplyClick(int position) {

//                ReplyInfoResponseBean.InfoBean reply = mReplys.get(position);
//                addReply(reply.getPid());
                //弹键盘

                InputMethodUtils.openSoftKeyboard(CommentsActivity.this, mEtComment);
                mEtComment.setHint(getString(R.string.reply) + mReplys.get(position).getFrom_user_name());
                mPosition = position;
//                LinearLayoutManager linearLayoutManager1 = linearLayoutManager;
//                mRvCommentsDetail.smoothScrollToPosition(position);
            }
        });

        mMHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMAdapter);

        mMHeaderAndFooterWrapper.addHeaderView(mView);
        mRvCommentsDetail.setAdapter(mMHeaderAndFooterWrapper);
        mMHeaderAndFooterWrapper.notifyDataSetChanged();
        initRefresh();
        mBottomlayout.addOnLayoutChangeListener(this);



    }


    private void initRefresh() {
        mRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);

        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                mCommentDetailModel.getAllReplys(mCommentID, mReplyCallBack, 1);
                mReplyCallBack.isRefresh = true;
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if (mPage.hasMore()) {
                    mCommentDetailModel.getAllReplys(mCommentID, mReplyCallBack, mPage.cur_page);
                    mReplyCallBack.isRefresh = false;
                } else {
                    ViewUtils.showToast("暂无更多数据");
                    return false;
                }
                return true;
            }
        });
    }

    private void initHeaderView() {
        ll_likers_info = (LinearLayout) mView.findViewById(R.id.ll_goods_avar_list);
        mAvar = (ImageView) mView.findViewById(R.id.avatar);
        mNickName = (TextView) mView.findViewById(R.id.nickname);
        mTime = (TextView) mView.findViewById(R.id.time);
        mReplyNum = (TextView) mView.findViewById(R.id.replynum);
        mContent = (TextView) mView.findViewById(R.id.tv_content);
        mCover = (ImageView) mView.findViewById(R.id.cover);
        mBookName = (TextView) mView.findViewById(R.id.tv_book_name);
        mGoodsNum = (TextView) mView.findViewById(R.id.tv_goods_num);
        mApostrophe = (ImageView) mView.findViewById(R.id.iv_apostrophe);
        iv1 = (ImageView) mView.findViewById(R.id.iv_1);
        iv2 = (ImageView) mView.findViewById(R.id.iv_2);
        iv3 = (ImageView) mView.findViewById(R.id.iv_3);
        iv4 = (ImageView) mView.findViewById(R.id.iv_4);

        if (iv_list == null) {
            iv_list = new ArrayList<>();
            iv_list.add(iv1);
            iv_list.add(iv2);
            iv_list.add(iv3);
            iv_list.add(iv4);
        }

        ll_likers_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到点赞详情页
                Intent intent = new Intent(CommentsActivity.this, LikersInfoActivity.class);
                //把评论ID带过去
                intent.putExtra("comment_id", mCommentID);
                startActivity(intent);
            }
        });
    }


    @OnClick({R.id.back, R.id.title, R.id.lin_share, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                break;

            case R.id.lin_share:
                //分享评论
                showSharePopWindow();
                break;

            case R.id.submit:
                submitReply(); //提交回复
                break;
        }
    }

    private void showSharePopWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = new SharePopUpWindow(this);
        }
        mPopupWindow.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    //感知软键盘展开和收起
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) mEtComment.getLayoutParams();

        if (oldBottom != 0 && bottom != 0 && oldBottom - bottom > DensityUtil.getScreenHeight(this) / 3) {
            //            键盘弹出

            mLinShare.setVisibility(View.GONE);
            mSubmit.setVisibility(View.VISIBLE);
            mParams.height = 4 * mEtComment.getMeasuredHeight();

        } else if (oldBottom != 0 && bottom != 0 && bottom - oldBottom > DensityUtil.getScreenHeight(this) / 3) {
            mLinShare.setVisibility(View.VISIBLE);
            mSubmit.setVisibility(View.GONE);
            mParams.height = mEtComment.getMeasuredHeight() / 4;

            //还原成评论状态
            mPosition = -1;
            mEtComment.setHint("写评论");

            //            键盘收起
        }
        mEtComment.setLayoutParams(mParams);
    }


    /**
     * 提交回复到后台
     */
    private void submitReply() {
        if (mPosition == -1) {
            addReply("0");
        }else{
            addReply(mReplys.get(mPosition).getReply_comment_reply_id());
        }
    }

    private void addReply(String pid) {
        String comment_id = mCommentsDetailResponoseBean.getComment_id();

        AddReplyRequestBean addReplyRequestBean = new AddReplyRequestBean();
        addReplyRequestBean.setComment_id(comment_id);

        String content = mEtComment.getText().toString();
        addReplyRequestBean.setContent(content);

        addReplyRequestBean.setPid(pid);

        mCommentDetailModel.addReply(addReplyRequestBean, new AppBaseResponseCallBack<NovateResponse>(this, true) {
            @Override
            public void onSuccee(NovateResponse response) {
                mEtComment.clearFocus();
                InputMethodUtils.hideForced(CommentsActivity.this);
                mEtComment.setText("");
                //刷新回复表
                mCommentDetailModel.getAllReplys(mCommentID, mReplyCallBack, mPage.cur_page);
                mReplyCallBack.isRefresh = false;
            }
        });
    }

}
