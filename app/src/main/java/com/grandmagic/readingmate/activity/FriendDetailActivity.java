package com.grandmagic.readingmate.activity;

import android.content.Intent;
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
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommentsAdapter;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.PersonCollectAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.db.Contacts;
import com.grandmagic.readingmate.bean.response.PersonCollectBookResponse;
import com.grandmagic.readingmate.bean.response.PersonCommentResponse;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.event.FriendDeleteEvent;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ImageLoader;
import com.grandmagic.readingmate.utils.InputMethodUtils;
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
import cn.bingoogolapple.refreshlayout.util.SimpleRefreshListener;

/**
 * @see #mPersonInfo 此页面进入时必传
 */
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
    String mNickname;
    PersonInfo mPersonInfo;

    private void initdata() {
        mModel = new ContactModel(this);
        mPersonInfo = getIntent().getParcelableExtra(PERSON_INFO);
        userid = mPersonInfo.getUser_id();
        mNickname = mPersonInfo.getNickname();
        loadPersonCollect();
        loadComment(commentcurrpage);
        initSimplePersonInfo(mPersonInfo);
    }


    List<PersonCollectBookResponse.InfoBean> mCollectList = new ArrayList<>();//用户收藏list
    DefaultEmptyAdapter mCollDefaultAdapter;
    DefaultEmptyAdapter mCommentDefaultAdapter;
    CommentsAdapter mCommentsAdapter;

    List<PersonCommentResponse.CommentInfoBean> mCommentInfoBeanList = new ArrayList<>();

    private void initview() {
        mTitleMore.setVisibility(View.VISIBLE);
        mTitle.setText("详细信息");
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mCommentsAdapter = new CommentsAdapter(this, mCommentInfoBeanList);
        mCommentDefaultAdapter = new DefaultEmptyAdapter(mCommentsAdapter, this);
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
        isFriend = ContactModel.isFriend(this, mPersonInfo.getUser_id());
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
        mRefreshLayout.setPullDownRefreshEnable(false);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setDelegate(new SimpleRefreshListener(){

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if (commentcurrpage < comentpagecount) {
                    commentcurrpage++;
                    loadComment(commentcurrpage);
                } else {
                    mRefreshLayout.endLoadingMore();
                    Toast.makeText(FriendDetailActivity.this, "NOMORE", Toast.LENGTH_SHORT).show();
                }
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
        mModel.loadUserComment(userid, mCommentcurrpage, new AppBaseResponseCallBack<NovateResponse<PersonCommentResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<PersonCommentResponse> response) {
                mCommentInfoBeanList.addAll(response.getData().getComment_info());
                comentpagecount = response.getData().getPageCount();
                mCommentsAdapter.setAvatar(response.getData().getAvatar_url());
                mCommentsAdapter.setusername(response.getData().getUser_name());
                mCommentDefaultAdapter.refresh();
                mRefreshLayout.endLoadingMore();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mCommentDefaultAdapter.refresh();
            }
        });
    }

    @OnClick({R.id.back, R.id.recommend, R.id.fab, R.id.title_more, R.id.iv_sendmsg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.recommend:
                toRecommend();
                break;
            case R.id.fab:
                showAddFriendDialog();
                break;
            case R.id.title_more:
                showDeletePop();
                break;
            case R.id.iv_sendmsg:
                toChat();
                break;
        }
    }

    /**
     * 弹出请求添加好友的dialog
     */
    private void showAddFriendDialog() {
        final CustomDialog mDialog = new CustomDialog(this);
        mDialog.setMaxNum(20);
        mDialog.setYesStr("发送");
        mDialog.setOnBtnOnclickListener(new CustomDialog.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                addContact(mDialog.getMessage());
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
     * @param reason
     */
    private void addContact(final String reason) {
        InputMethodUtils.hide(this);
        new SearchUserModel(this).requestAddFriend(userid, reason, new AppBaseResponseCallBack<NovateResponse>(this) {
            @Override
            public void onSuccee(NovateResponse response) {
                Toast.makeText(FriendDetailActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 去推荐好友
     */
    private void toRecommend() {
        Intent mIntent = new Intent(FriendDetailActivity.this, RecommendActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(PERSON_INFO, mPersonInfo);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    /**
     * 去聊天界面
     */
    private void toChat() {
        Intent mIntent = new Intent(this, ChatActivity.class);
        mIntent.putExtra(ChatActivity.CHAT_NAME, userid);
        mIntent.putExtra(ChatActivity.CHAT_IM_NAME, mNickname);
        mIntent.putExtra(ChatActivity.GENDER, mPersonInfo.getGender());
        startActivity(mIntent);
    }

    PopupWindow mPopupWindow;

    /**
     * 弹出删除好友的popupwindow
     */
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
                EventBus.getDefault().post(new FriendDeleteEvent(userid));
//                删除好友的同时将对方从本地好友列表移除
                ContactsDao mContactsDao = DBHelper.getContactsDao(FriendDetailActivity.this);
                Contacts mUnique = mContactsDao.queryBuilder().where(ContactsDao.Properties.User_id.eq(userid)).unique();
                if (mUnique != null) {
                    mContactsDao.delete(mUnique);
                }
                DBHelper.close();
                finish();
            }
        });
    }
}
