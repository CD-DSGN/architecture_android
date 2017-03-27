package com.grandmagic.readingmate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.RecommendAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.tamic.novate.util.SPUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 2017年3月27日11:25:56
 *
 * @author lps  推荐给好友的界面
 */
public class RecommendActivity extends AppBaseActivity {

    public static final String USERID = "user_id";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        setTranslucentStatus(true);
        initview();
        initdata();
    }


    RecommendAdapter mAdapter;
    List<Contacts> mContactses = new ArrayList<>();

    private void initview() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecommendAdapter(this, mContactses);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setClickListener(new RecommendAdapter.ClickListener() {
            @Override
            public void ItemCLick(int position) {
                sendCardMsg(mContactses.get(position).getUser_id());
            }
        });
    }

    private void sendCardMsg(int mUser_id) {
        EMMessage mCardMsg = EMMessage.createTxtSendMessage("111", mUser_id + "");
        if (mCardMsg==null)return;
        mCardMsg.setAttribute("type", "card");
        mCardMsg.setAttribute("avatar", mPersonInfo.getAvatar());
        mCardMsg.setAttribute("clientid", mPersonInfo.getClientid());
        mCardMsg.setAttribute("nickname", mPersonInfo.getNickname());
        mCardMsg.setAttribute("userid", mPersonInfo.getUser_id());
        EMClient.getInstance().chatManager().sendMessage(mCardMsg);
    }

    PersonInfo mPersonInfo;

    private void initdata() {
        mPersonInfo = getIntent().getParcelableExtra(FriendDetailActivity.PERSON_INFO);
        //从本地数据库中读取好友列表
        ContactsDao mContactsDao = DBHelper.getContactsDao(this);
        List<Contacts> mLoadAll = mContactsDao.queryBuilder().
//                排除掉这个用户自己
        where(ContactsDao.Properties.User_id.notEq(mPersonInfo.getUser_id())).list();
        if (mLoadAll != null && !mLoadAll.isEmpty()) {
            mContactses.addAll(mLoadAll);
            mAdapter.notifyDataSetChanged();
        }
    }
}
