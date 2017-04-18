package com.grandmagic.readingmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CollectionAdapter;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.BookFollowersResponse;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class CollectedPersonActivity extends AppBaseActivity implements CollectionAdapter.AdapterLisenter {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.recyclerview)
    PullLoadMoreRecyclerView mRecyclerview;
    BookModel mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected_person);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
        initData();
    }

    int cpage = 1, pagecount = 1;
    String book_id;

    private void initData() {
        book_id = getIntent().getStringExtra(BookDetailActivity.BOOK_ID);
        mModel = new BookModel(this);
        loadFollower(cpage);
    }

    private void loadFollower(final int mCpage) {
        mModel.loadAllFollower(book_id, mCpage, new AppBaseResponseCallBack<NovateResponse<BookFollowersResponse>>(this) {
            @Override
            public void onSuccee(NovateResponse<BookFollowersResponse> response) {
                if (mCpage==1) {
                    pagecount = response.getData().getPageCount();
                }
                if (response.getData().getInfo() != null && !response.getData().getInfo().isEmpty())
                    mList.addAll(response.getData().getInfo());
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

    DefaultEmptyAdapter mAdapter;
    List<BookFollowersResponse.InfoBean> mList = new ArrayList<>();

    private void initview() {
        mTitle.setText("收藏过的用户");
        mRecyclerview.setLinearLayout();
        CollectionAdapter mCollectionAdapter = new CollectionAdapter(this, mList);
        mCollectionAdapter.setLisenter(this);
        mAdapter = new DefaultEmptyAdapter(mCollectionAdapter, this);
        mRecyclerview.setAdapter(mAdapter);
        initrefresh();
    }

    private void initrefresh() {
        mRecyclerview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                cpage = 1;
                mList.clear();
                loadFollower(cpage);
            }

            @Override
            public void onLoadMore() {
                if (cpage < pagecount) {
                    cpage++;
                    loadFollower(cpage);
                } else {
                    Toast.makeText(CollectedPersonActivity.this, "nomre", Toast.LENGTH_SHORT).show();
                    mRecyclerview.setPullLoadMoreCompleted();
                }
            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(int position) {
        BookFollowersResponse.InfoBean mInfoBean = mList.get(position);
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
        mDialog.setTitle(R.string.add_friend_request);
        mDialog.setOnBtnOnclickListener(new CustomDialog.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                addContact(mDialog.getMessage(), mList.get(position).getUser_id());
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
                Toast.makeText(CollectedPersonActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
