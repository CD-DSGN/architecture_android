package com.grandmagic.readingmate.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommentsAdapter;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.PersonCollectAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.PersonCollectBookResponse;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.event.ContactDeleteEvent;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.util.Environment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

public class FriendDetailActivity extends AppBaseActivity {
    ContactModel mModel;
    public static final String PERSON_INFO = "person_info";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.recommend)
    ImageView mRecommend;
    @BindView(R.id.gender)
    ImageView mGender;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.collapsingtoolbarlayout)
    CollapsingToolbarLayout mCollapsingtoolbarlayout;
    @BindView(R.id.appbarlayout)
    AppBarLayout mAppbarlayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout mCoordinatorlayout;
    private static final String TAG = "FriendDetailActivity";
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.rootview)
    LinearLayout mRootview;
    String userid;
    @BindView(R.id.clientid)
    TextView mClientid;
    @BindView(R.id.lin_coll)
    LinearLayout mLinColl;
    @BindView(R.id.iv_sendmsg)
    ImageView mIvSendmsg;
    @BindView(R.id.Collectrecyclerview)
    RecyclerView mCollectrecyclerview;
    boolean isFriend;//与自己是否是好友关系

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
        initdata();
    }

    int comentpagecount = 1, commentcurrpage = 1;

    private void initdata() {
        mModel = new ContactModel(this);
        PersonInfo mPersonInfo = getIntent().getParcelableExtra(PERSON_INFO);
        userid = mPersonInfo.getUser_id();
        loadPersonCollect();
        loadComment(commentcurrpage);
        initSimplePersonInfo(mPersonInfo);
    }


    List<PersonCollectBookResponse.InfoBean> mCollectList = new ArrayList<>();//用户收藏list
    DefaultEmptyAdapter mCollDefaultAdapter;
    DefaultEmptyAdapter mCommentDefaultAdapter;
    List<String> mStrings = new ArrayList<>();

    private void initview() {
        mTitleMore.setVisibility(View.VISIBLE);
        mTitle.setText("详细信息");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        CommentsAdapter mCommentsAdapter = new CommentsAdapter(this, mStrings);
        mCommentDefaultAdapter = new DefaultEmptyAdapter(mCommentsAdapter,this);
        mRecyclerview.setAdapter(mCommentDefaultAdapter);

        mAppbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //完全展开
                }
                if (appBarLayout.getMeasuredHeight() == Math.abs(verticalOffset)) {
                    //完全关闭
                    if (!isFriend)
                        mFab.hide();
                }
                if (appBarLayout.getMeasuredHeight() > Math.abs(verticalOffset)) {
                    //完全关闭
                    if (!isFriend)
                        mFab.show();
                }
            }
        });
        //初始化用户收藏的图书的recyclerView
        mCollectrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PersonCollectAdapter mCollAdapter = new PersonCollectAdapter(this, mCollectList);
        mCollDefaultAdapter = new DefaultEmptyAdapter(mCollAdapter, this);
        mCollectrecyclerview.setAdapter(mCollDefaultAdapter);
        initrefreshLayout();
    }

    private void initSimplePersonInfo(PersonInfo mPersonInfo) {
        ImageLoader.loadCircleImage(this, Environment.BASEULR_PRODUCTION + mPersonInfo.getAvatar(), mAvatar);
        mName.setText(mPersonInfo.getNickname());
        mClientid.setText(mPersonInfo.getClientid());
        isFriend = mPersonInfo.isFriend();
        if (isFriend) {//是否已经是好友关系
            mFab.hide();
            mRecommend.setVisibility(View.VISIBLE);
            mIvSendmsg.setVisibility(View.VISIBLE);
            mTitleMore.setVisibility(View.VISIBLE);
        } else {
            mFab.show();
            mRecommend.setVisibility(View.GONE);
            mIvSendmsg.setVisibility(View.GONE);
            mTitleMore.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新相关
     */
    private void initrefreshLayout() {
        BGAStickinessRefreshViewHolder mRefreshViewHolder = new BGAStickinessRefreshViewHolder(this, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);
//        mRefreshLayout.offsetTopAndBottom(88);
        mRefreshLayout.setPullDownRefreshEnable(false);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endRefreshing();
                    }
                }, 2000);

            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endLoadingMore();
                    }
                }, 2000);
                return true;
            }
        });
    }

    /**
     * 加载用户的收藏的书籍
     */
    private void loadPersonCollect() {
        mModel.getPersonCollect(userid, new AppBaseResponseCallBack<NovateResponse<PersonCollectBookResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<PersonCollectBookResponse> response) {
                mCollectList.addAll(response.getData().getInfo());
                mCollDefaultAdapter.refresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mCollDefaultAdapter.refresh();
            }
        });
    }

    /**
     * 加载用户的评论
     *
     * @param mCommentcurrpage 页码
     */
    private void loadComment(int mCommentcurrpage) {
        mModel.loadUserComment(userid, mCommentcurrpage, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                for (int i = 0; i < 50; i++) {
                    mStrings.add("af");
                }
                mCommentDefaultAdapter.refresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mCommentDefaultAdapter.refresh();
            }
        });
    }

    @OnClick({R.id.back, R.id.recommend, R.id.fab, R.id.title_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.recommend:
                break;
            case R.id.fab:
                break;
            case R.id.title_more:
                showDeletePop();
                break;
        }
    }

    PopupWindow mPopupWindow;

    private void showDeletePop() {
        if (mPopupWindow == null) {
            View mpopview = LayoutInflater.from(this).inflate(R.layout.pop_deletefriend, null);
            mpopview.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFriend();
                    mPopupWindow.dismiss();
                }
            });
            mpopview.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                }
            });
            AutoUtils.auto(mpopview);
            mPopupWindow = new PopupWindow(mpopview, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setClippingEnabled(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.alpha = 1.0f;
                    getWindow().setAttributes(params);
                }
            });
        }
        mPopupWindow.showAtLocation(mRootview, Gravity.BOTTOM, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
    }

    /**
     * 删除好友
     */
    private void deleteFriend() {
        mModel.deleteContact(userid, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                EventBus.getDefault().post(new ContactDeleteEvent(userid));
                finish();
            }
        });
    }
}
