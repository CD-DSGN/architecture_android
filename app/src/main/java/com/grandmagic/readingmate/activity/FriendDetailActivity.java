package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.grandmagic.readingmate.utils.KitUtils;
import com.grandmagic.readingmate.utils.ViewUtils;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tamic.novate.util.Environment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @see #mPersonInfo 此页面进入时必传
 */
public class FriendDetailActivity extends AppBaseActivity {
    private static final String TAG = "FriendDetailActivity";
    public static final String PERSON_INFO = "person_info";
    ContactModel mModel;
    PullLoadMoreRecyclerView mRecyclerview;
    //title
    ImageView mBack;

    TextView mTitle;
    ImageView mTitleMore;
    //header
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.recommend)
    ImageView mRecommend;
    @BindView(R.id.gender)
    ImageView mGender;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
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
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
        initdata();
    }

    private void initview() {
        inittitleLatout();
        initrecyclerView();
        initrefreshLayout();
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


    //初始化recyclerView
    private void initrecyclerView() {
        View mHeaderView = initHeaderView();
        mRecyclerview = (PullLoadMoreRecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setLinearLayout();
        mCommentsAdapter = new CommentsAdapter(this, mCommentInfoBeanList);
        mCommentDefaultAdapter = new DefaultEmptyAdapter(mCommentsAdapter, this);
        HeaderAndFooterWrapper mWrapper = new HeaderAndFooterWrapper(mCommentDefaultAdapter);
        mCommentDefaultAdapter.setHeaderOrFooterAdapter(mWrapper);
        mWrapper.addHeaderView(mHeaderView);
        mRecyclerview.setAdapter(mWrapper);


        //初始化用户收藏的图书的recyclerView
        mCollectrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PersonCollectAdapter mCollAdapter = new PersonCollectAdapter(this, mCollectList);
        mCollDefaultAdapter = new DefaultEmptyAdapter(mCollAdapter, this);
        mCollectrecyclerview.setAdapter(mCollDefaultAdapter);
    }

    //初始化顶部的title
    private void inittitleLatout() {
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);
        mTitleMore = (ImageView) findViewById(R.id.title_more);
        mRootview = (LinearLayout) findViewById(R.id.rootview);
        mTitleMore.setVisibility(View.VISIBLE);
        mTitle.setText("详细信息");
        mTitleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletePop();
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //初始化上部分的header
    private View initHeaderView() {
        View mInflate = LayoutInflater.from(this).inflate(R.layout.view_friend_header, null);
        ButterKnife.bind(this, mInflate);
        mInflate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AutoUtils.auto(mInflate);
        return mInflate;
    }

    private void initSimplePersonInfo(PersonInfo mPersonInfo) {
        ImageLoader.loadCircleImage(this, Environment.BASEULR_PRODUCTION + mPersonInfo.getAvatar(), mAvatar);
        mName.setText(KitUtils.unicodeDecode(mPersonInfo.getNickname()));
        mClientid.setText(KitUtils.unicodeDecode(mPersonInfo.getSignature()));
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
        mRecyclerview.setPullRefreshEnable(false);
        mRecyclerview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (commentcurrpage < comentpagecount) {
                    commentcurrpage++;
                    loadComment(commentcurrpage);
                } else {
                    ViewUtils.showToast("NOMORE");
                    mRecyclerview.setPullLoadMoreCompleted();
                }
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
                Log.e(TAG, "onSuccee() called with: response = [" + response + "]");
                mCommentInfoBeanList.addAll(response.getData().getComment_info());
                comentpagecount = response.getData().getPageCount();
                mCommentsAdapter.setAvatar(response.getData().getAvatar_url());
                mCommentsAdapter.setusername(response.getData().getUser_name());
                mCommentDefaultAdapter.refresh();
                mRecyclerview.setPullLoadMoreCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mCommentDefaultAdapter.refresh();
                mRecyclerview.setPullLoadMoreCompleted();
            }
        });
    }

    @OnClick({R.id.recommend, R.id.iv_sendmsg,R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.recommend:
                toRecommend();
                break;
            case R.id.fab:
                showAddFriendDialog();
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
        mDialog.setTitle(R.string.add_friend_request);
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
