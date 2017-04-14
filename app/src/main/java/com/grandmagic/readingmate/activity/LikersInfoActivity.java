package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.LikersAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.LikersInfoResponseBean;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.model.CommentDetailModel;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.grandmagic.readingmate.utils.Page;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class LikersInfoActivity extends AppBaseActivity implements LikersAdapter.AdapterLisenter {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.rv_likers)
    RecyclerView mRvLikers;

    Page mPage;
    @BindView(R.id.BGARefreshLayout)
    BGARefreshLayout mRefreshLayout;
    private List<LikersInfoResponseBean.InfoBean> mLikers = new ArrayList<>();
    CommentDetailModel mCommentDetailModel;
    String mCommentID;

    BGAStickinessRefreshViewHolder mRefreshViewHolder;


    AppBaseResponseCallBack mCallBack;
    LikersAdapter mLikersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(true);
        setContentView(R.layout.activity_likers_info);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        initData();
        initView();
        loadData();
    }

    private void initData() {
        mCommentID = getIntent().getStringExtra("comment_id");

        if (mCommentDetailModel == null) {
            mCommentDetailModel = new CommentDetailModel(this);
        }

        if (mPage == null) {
            mPage = new Page(mLikers, mCommentDetailModel.PAGE_COUNT_LIKERS);
        }

        if (mLikersAdapter == null) {
            mLikersAdapter = new LikersAdapter(this, mLikers);
            mLikersAdapter.setLisenter(this);
        }



        if (mCallBack == null) {
            mCallBack = new AppBaseResponseCallBack<NovateResponse<LikersInfoResponseBean>>(LikersInfoActivity.this) {
                @Override
                public void onSuccee(NovateResponse<LikersInfoResponseBean> response) {
                    mRefreshLayout.endLoadingMore();
                    mRefreshLayout.endRefreshing();
                    LikersInfoResponseBean likersInfoResponseBean = response.getData();
                    try {
                        mPage.total_num = Integer.parseInt(likersInfoResponseBean.getTotal_num());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<LikersInfoResponseBean.InfoBean> tmp = likersInfoResponseBean.getInfo();
                    if (mCallBack.isRefresh) {
                        mPage.refresh(tmp);
                    } else {
                        mPage.more(tmp);
                    }
                    mLikersAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mRefreshLayout.endLoadingMore();
                    mRefreshLayout.endRefreshing();
                }
            };
        }
    }

    private void loadData() {
        if (mCommentID != null) {
            mCommentDetailModel.getAllLikes(mCommentID, mCallBack
                    , mPage.cur_page);
        }

    }

    private void initView() {
        mTitle.setText(R.string.likes_info);

        mRvLikers.setLayoutManager(new LinearLayoutManager(this));
        mRvLikers.setAdapter(mLikersAdapter);
        initRefresh();

    }

    private void initRefresh() {
        mRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);

        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                mCommentDetailModel.getAllLikes(mCommentID, mCallBack, 1);
                mCallBack.isRefresh = true;
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if (mPage.hasMore()) {
                    mCommentDetailModel.getAllLikes(mCommentID, mCallBack, mPage.cur_page);
                    mCallBack.isRefresh = false;
                } else {
                    ViewUtils.showToast("暂无更多数据");
                    return false;
                }
                return true;
            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(int position) {
        LikersInfoResponseBean.InfoBean mInfoBean = mLikers.get(position);
        Intent mIntent = new Intent(this, FriendDetailActivity.class);
        Bundle mBundle = new Bundle();
        PersonInfo mInfo = new PersonInfo();
        mInfo.setUser_id(mInfoBean.getUser_id());
        mInfo.setNickname(mInfoBean.getUser_name());
        mInfo.setFriend(mInfoBean.getIs_friend() == 1);
        mInfo.setClientid(mInfoBean.getClient_id());
        mInfo.setAvatar(mInfoBean.getAvatar_url().getLarge());
        mBundle.putParcelable(FriendDetailActivity.PERSON_INFO, mInfo);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }


    @Override
    public void addFriend(int position) {
        showAddFriendDialog(position);
    }

    /**
     * 显示添加好友的弹窗
     *
     * @param position
     */
    private void showAddFriendDialog(final int position) {
        final CustomDialog mDialog = new CustomDialog(this);
        mDialog.setMaxNum(20);
        mDialog.setYesStr("发送");
        mDialog.setTitle(getString(R.string.add_friend_request));
        mDialog.setOnBtnOnclickListener(new CustomDialog.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                addContact(mDialog.getMessage(), mLikers.get(position).getUser_id());
                mDialog.dismiss();
            }

            @Override
            public void onNoClick() {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * 添加好友，
     *
     * @param reason 验证信息
     */
    private void addContact(String reason, String mUser_id) {
        SearchUserModel mSearchUserModel = new SearchUserModel(this);
        InputMethodUtils.hide(this);
        mSearchUserModel.requestAddFriend(mUser_id, reason, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                Toast.makeText(LikersInfoActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
