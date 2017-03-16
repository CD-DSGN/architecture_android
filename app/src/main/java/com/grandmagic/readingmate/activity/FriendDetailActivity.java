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
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.event.ContactDeleteEvent;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.hyphenate.chat.EMClient;
import com.tamic.novate.NovateResponse;

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
    public static final String USER_ID = "userid";
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
    @BindView(R.id.iv_coll_1)
    ImageView mIvColl1;
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

    private void initdata() {
        mModel = new ContactModel(this);
        userid = getIntent().getStringExtra(USER_ID);
    }

    CommentsAdapter mAdapter;
    List<String> mStrings = new ArrayList<>();

    private void initview() {
        mTitleMore.setVisibility(View.VISIBLE);
        mTitle.setText("详细信息");
        ImageLoader.loadCircleImage(this, "http://pic.ytqmx.com:82/2014/0831/01/11.jpg!960.jpg"
                , mAvatar);
        ImageLoader.loadImage(this, "http://pic.ytqmx.com:82/2014/0831/01/11.jpg!960.jpg"
                , mIvColl1);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < 50; i++) {
            mStrings.add("af");
        }
        mAdapter = new CommentsAdapter(this, mStrings);
        mRecyclerview.setAdapter(mAdapter);

        mAppbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //完全展开
                }
                if (appBarLayout.getMeasuredHeight() == Math.abs(verticalOffset)) {
                    //完全关闭
                    mFab.hide();
                }
                if (appBarLayout.getMeasuredHeight() > Math.abs(verticalOffset)) {
                    //完全关闭
                    mFab.show();
                }
            }
        });
        initrefreshLayout();
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
