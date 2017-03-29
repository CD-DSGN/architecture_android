package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommentDetailAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.CommentsDetailResponoseBean;
import com.grandmagic.readingmate.model.CommentDetailModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.DateUtil;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.view.SharePopUpWindow;
import com.tamic.novate.NovateResponse;
import com.umeng.socialize.UMShareAPI;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsActivity extends AppBaseActivity {
public static final String COMMENT_ID="comment_id";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rv_comments_detail)
    RecyclerView mRvCommentsDetail;
    @BindView(R.id.lin_share)
    LinearLayout mLinShare;

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
    }

    private void loadData() {
        mCommentDetailModel.getCommentDetail(mCommentID, new AppBaseResponseCallBack<NovateResponse<CommentsDetailResponoseBean>>(CommentsActivity.this) {
            @Override
            public void onSuccee(NovateResponse<CommentsDetailResponoseBean> response) {
                mCommentsDetailResponoseBean = response.getData();
                setHeaderView();

            }
        });


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
        mReplyNum.setText(mCommentsDetailResponoseBean.getReply_count() + "人回复");
        mContent.setText(mCommentsDetailResponoseBean.getContent());
        String cover_url = mCommentsDetailResponoseBean.getPhoto();

        ImageLoader.loadCircleImage(this,KitUtils.getAbsoluteUrl(cover_url),mCover);
        int like_num = mCommentsDetailResponoseBean.getLike_times();
        mGoodsNum.setText(like_num + "点赞");
        if (like_num > 4) {   //显示省略号
            mApostrophe.setVisibility(View.VISIBLE);
        }else{
            mApostrophe.setVisibility(View.INVISIBLE);
        }

        List<CommentsDetailResponoseBean.ThumbUserAvatarBean> thumbUserAvatarBeenList = mCommentsDetailResponoseBean.getThumb_user_avatar();
        hideLikers();
        if (thumbUserAvatarBeenList != null) {
            int size = thumbUserAvatarBeenList.size();
            for (int i = 0 ; i < 4 && i <size; i++) {
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
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("电影不错");
            data.add("结局缺乏新意，其他没什么");
        }
        mTitle.setText(R.string.comment_detail);
        mRvCommentsDetail.setLayoutManager(new LinearLayoutManager(this));
        mView = LayoutInflater.from(this).inflate(R.layout.item_comments_detail, mRvCommentsDetail, false);
        initHeaderView();
        AutoUtils.auto(mView);
        mMAdapter = new CommentDetailAdapter(this, data);
        mMHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMAdapter);

        mMHeaderAndFooterWrapper.addHeaderView(mView);
        mRvCommentsDetail.setAdapter(mMHeaderAndFooterWrapper);
        mMHeaderAndFooterWrapper.notifyDataSetChanged();

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


    @OnClick({R.id.back, R.id.title, R.id.lin_share})
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
}
