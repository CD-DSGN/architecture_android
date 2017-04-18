package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommentDetailAdapter;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.request.AddReplyRequestBean;
import com.grandmagic.readingmate.bean.response.CommentsDetailResponoseBean;
import com.grandmagic.readingmate.bean.response.ReplyInfoResponseBean;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.model.CommentDetailModel;
import com.grandmagic.readingmate.ui.DeleteConfirmDlg;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.DensityUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.Page;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.umeng.socialize.UMShareAPI;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.taobao.accs.ACCSManager.mContext;

public class CommentsActivity extends AppBaseActivity implements View.OnLayoutChangeListener {
    public static final String COMMENT_ID = "comment_id";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rv_comments_detail)
    PullLoadMoreRecyclerView mRvCommentsDetail;
    @BindView(R.id.lin_share)
    LinearLayout mLinShare;

    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.bottomlayout)
    LinearLayout mBottomlayout;
    @BindView(R.id.tv_delete_comment)
    TextView mTvDeleteComment;

    public static final int RESULT_DEL = 1;
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
    TextView mLikeNum;
    ImageView mApostrophe;
    ImageView mCommentGood;
    LinearLayout mll_like_num;

    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ArrayList<ImageView> iv_list;

    CommentsDetailResponoseBean mCommentsDetailResponoseBean;
    AppBaseResponseCallBack mReplyCallBack;
    Page mPage;



    List<ReplyInfoResponseBean.InfoBean> mReplys = new ArrayList<>();

    BookModel mModel;
    int mPosition = -1;
    private int mLike_num = 0;
    private int mIsThumb = 2;

    private BookModel mBookModel;
    private DeleteConfirmDlg mDelComment;
    private DeleteConfirmDlg mDelReply;
    private DefaultEmptyAdapter mDefaultEmptyAdapter;

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
            mReplyCallBack = new AppBaseResponseCallBack<NovateResponse<ReplyInfoResponseBean>>(CommentsActivity.this, true, true) {
                @Override
                public void onSuccee(NovateResponse<ReplyInfoResponseBean> response) {
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

//                        if (mPage.list == null || mPage.list.isEmpty()) {
//                            mDefaultEmptyAdapter.refresh();
//                        }

                    }

                    mRvCommentsDetail.setPullLoadMoreCompleted();
                    mDefaultEmptyAdapter.refresh();
                    mMHeaderAndFooterWrapper.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mDefaultEmptyAdapter.refresh();
                    mRvCommentsDetail.setPullLoadMoreCompleted();
                    mMHeaderAndFooterWrapper.notifyDataSetChanged();
                }
            };
        }

        if (mModel == null) {
            mModel = new BookModel(this);
        }

        if (mBookModel == null) {
            mBookModel = new BookModel(this);
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
        if (mCommentsDetailResponoseBean == null) {
            return;
        }

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
        ImageLoader.loadImage(this, KitUtils.getAbsoluteUrl(cover_url), mCover);
        mLike_num = mCommentsDetailResponoseBean.getLike_times();
        mGoodsNum.setText(mLike_num + "人赞过");
        if (mLike_num > 4) {   //显示省略号
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
        mIsThumb = mCommentsDetailResponoseBean.getIs_thumb();
        setCommentThumb();

        String id = mCommentsDetailResponoseBean.getIs_self_comment();
        if (id != null && id.equals("1")) {
            mTvDeleteComment.setVisibility(View.VISIBLE);
        } else {
            mTvDeleteComment.setVisibility(View.INVISIBLE);
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
        mRvCommentsDetail.setLinearLayout();
        mView = LayoutInflater.from(this).inflate(R.layout.item_comments_detail, mRvCommentsDetail, false);
        initHeaderView();
        AutoUtils.auto(mView);
        mMAdapter = new CommentDetailAdapter(this, mReplys);
        mMAdapter.setOnReplyListener(new CommentDetailAdapter.OnReplyListener() {
            @Override
            public void onReplyClick(int position) {
                InputMethodUtils.openSoftKeyboard(CommentsActivity.this, mEtComment);
                mEtComment.setHint(getString(R.string.reply) + mReplys.get(position).getFrom_user_name());
                mPosition = position;
                //                LinearLayoutManager linearLayoutManager1 = linearLayoutManager;
                //                mRvCommentsDetail.smoothScrollToPosition(position);
            }

            @Override
            public void onDeleteClick(final int position) {
                mDelReply.setOnClickYes(new DeleteConfirmDlg.OnClickYes() {
                    @Override
                    public void onClick() {
                        mCommentDetailModel.delReply(mReplys.get(position).getReply_comment_reply_id(),
                                new AppBaseResponseCallBack<NovateResponse>(CommentsActivity.this) {
                                    @Override
                                    public void onSuccee(NovateResponse response) {
                                        mPage.delete(position);
                                        mMHeaderAndFooterWrapper.notifyDataSetChanged();
                                    }
                                });
                        mDelReply.dismiss();
                    }
                });
                mDelReply.show();

            }

            @Override
            public void onLike(final int position) {   //点赞
                mCommentDetailModel.likeReply(mReplys.get(position).getReply_comment_reply_id(),
                        new AppBaseResponseCallBack<NovateResponse>(CommentsActivity.this) {
                            @Override
                            public void onSuccee(NovateResponse response) {
                                ReplyInfoResponseBean.InfoBean reply = mReplys.get(position);
                                reply.setIs_reply_thumb(1);
                                reply.setLike_times(Integer.parseInt(reply.getLike_times()) + 1 + "");
                                mMHeaderAndFooterWrapper.notifyDataSetChanged();
                            }
                        });

            }
        });

        if (mDelReply == null) {
            mDelReply = new DeleteConfirmDlg(this);
        }

        if (mDefaultEmptyAdapter == null) {
            mDefaultEmptyAdapter = new DefaultEmptyAdapter(mMAdapter, this, false);
            mDefaultEmptyAdapter.setEmptyViewTextview(getString(R.string.no_reply_yet));
        }
        mMHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mDefaultEmptyAdapter);
        mMHeaderAndFooterWrapper.addHeaderView(mView);
        mRvCommentsDetail.setAdapter(mMHeaderAndFooterWrapper);
        mMHeaderAndFooterWrapper.notifyDataSetChanged();
        initRefresh();
        mBottomlayout.addOnLayoutChangeListener(this);


    }


    private void initRefresh() {
        mRvCommentsDetail.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mCommentDetailModel.getAllReplys(mCommentID, mReplyCallBack, 1);
                mReplyCallBack.isRefresh = true;
            }

            @Override
            public void onLoadMore() {
                if (mPage.hasMore()) {
                    mCommentDetailModel.getAllReplys(mCommentID, mReplyCallBack, mPage.cur_page);
                    mReplyCallBack.isRefresh = false;
                } else {
                    ViewUtils.showToast(getString(R.string.no_more));
                    mRvCommentsDetail.setPullLoadMoreCompleted();
                }

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
        mCommentGood = (ImageView) mView.findViewById(R.id.iv_comment_good);
        mLikeNum = (TextView) mView.findViewById(R.id.like_num);
        mll_like_num = (LinearLayout) mView.findViewById(R.id.ll_like_num);
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

        mll_like_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到点赞详情页
                Intent intent = new Intent(CommentsActivity.this, LikersInfoActivity.class);
                //把评论ID带过去
                intent.putExtra("comment_id", mCommentID);
                startActivity(intent);
            }
        });

        setCommentThumb();

        //对图书评论进行点赞
        mCommentGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsThumb == 2) {  //未点赞情况下进行点赞
                    mModel.thumbBookComment(mCommentID, new AppBaseResponseCallBack<NovateResponse>(CommentsActivity.this) {
                        @Override
                        public void onSuccee(NovateResponse response) {
                            String good_text = String.format(getString(R.string.goods_num), ++mLike_num);
                            mGoodsNum.setText(good_text);
                            mIsThumb = 1;
                            mCommentsDetailResponoseBean.setIs_thumb(mIsThumb);
                            mCommentsDetailResponoseBean.setLike_times(mLike_num);
                            setCommentThumb();
                        }
                    });
                } else {
                    ViewUtils.showToast(getString(R.string.no_duplicate_good));
                }
            }
        });

        if (mDelComment == null) {
            mDelComment = new DeleteConfirmDlg(this);
        }
    }

    private void setCommentThumb() {
        if (mIsThumb == 1) {   //已点赞
            mCommentGood.setBackgroundResource(R.drawable.iv_like);
        } else if (mIsThumb == 2) {   //未点赞
            mCommentGood.setBackgroundResource(R.drawable.iv_like_gray);
        } else {

        }

        if (mLike_num > 0) {
            mLikeNum.setText(mLike_num + "");
            mLikeNum.setTextColor(Color.parseColor("#991cc9a2"));
        } else {
            mLikeNum.setText(R.string.good);
            mLikeNum.setTextColor(Color.parseColor("#99999999"));
        }
    }


    @OnClick({R.id.back, R.id.title, R.id.lin_share, R.id.submit, R.id.tv_delete_comment})
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

            case R.id.tv_delete_comment:
                deleteComment();  //删除书评
                break;
        }
    }

    private void deleteComment() {
        final String id = mCommentsDetailResponoseBean.getComment_id();
        mDelComment.setOnClickYes(new DeleteConfirmDlg.OnClickYes() {
            @Override
            public void onClick() {
                mBookModel.deleteBookComment(id,
                        new AppBaseResponseCallBack<NovateResponse>(mContext) {
                            @Override
                            public void onSuccee(NovateResponse response) {
                                ViewUtils.showToast(CommentsActivity.this.getString(R.string.delect_suc));
                                Intent intent = new Intent();
                                intent.putExtra(COMMENT_ID, mCommentID);
                                setResult(RESULT_DEL, intent);
                                finish();
                            }
                        });
                mDelComment.dismiss();
            }
        });
        mDelComment.show();
    }

    private void showSharePopWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = new SharePopUpWindow(this);
        }

        if (mCommentsDetailResponoseBean != null) {
//            mPopupWindow.setData(
//                    mCommentsDetailResponoseBean.getBook_name(),
//                    mCommentsDetailResponoseBean.getContent(), KitUtils.getAbsoluteUrl(mCommentsDetailResponoseBean.getPhoto()), "https://a.mlinks.cc/AKVC?book_id=3", "分享书评");

            mPopupWindow.setCommentData(mCommentsDetailResponoseBean.getBook_name(),
                    mCommentsDetailResponoseBean.getComment_id(),
                    mCommentsDetailResponoseBean.getContent(), mCommentsDetailResponoseBean.getPhoto(),
                    "7.5");              //缺图书评分字段
            mPopupWindow.show();
        }

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
            mEtComment.setText("");
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
        } else {
            addReply(mReplys.get(mPosition).getReply_comment_reply_id());
        }
    }

    private void addReply(String pid) {
        if (mCommentsDetailResponoseBean == null) {
            ViewUtils.showToast("网络错误，请刷新重试");
        }

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
