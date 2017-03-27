package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.RecommendAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.bean.response.PersonInfo;
import com.grandmagic.readingmate.db.ContactsDao;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 2017年3月27日11:25:56
 *
 * @author lps  推荐给好友的界面
 */
public class RecommendActivity extends AppBaseActivity {

    public static final String USERID = "user_id";
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.cancle)
    TextView mCancle;
    @BindView(R.id.check)
    TextView mCheck;
    boolean isSingleSel = true;//是否是单选，默认单选的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
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
                if (isSingleSel) {
                    ArrayList<Integer> mObjects = new ArrayList<>();
                    mObjects.add(mContactses.get(position).getUser_id());
                    showDialog(mObjects);
                }
            }
        });
    }

    /**
     * 弹出确认发送的窗口窗口
     * @param mSelected
     */
    private void showDialog(final List<Integer> mSelected) {
        View mView = View.inflate(this, R.layout.dialog_recommend, null);
        Button cancle = (Button) mView.findViewById(R.id.no);
        Button send = (Button) mView.findViewById(R.id.yes);
        mView.setLayoutParams(new LinearLayout.LayoutParams(566, 444));
        AutoUtils.auto(mView);
        mAlertDialog = new AlertDialog.Builder(this).create();
        mAlertDialog.setView(mView);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRecommentMsg(mSelected);
            }
        });
        mAlertDialog.show();
    }

    /**
     * 遍历所有需要发消息的人然后发送名片
     * @param mSelected
     */
    private void sendRecommentMsg(List<Integer> mSelected) {
        for (int mUser_id : mSelected) {
            EMMessage mCardMsg = EMMessage.createTxtSendMessage("111", mUser_id + "");
            if (mCardMsg == null) return;
            createCommendmsg(mCardMsg);
            EMClient.getInstance().chatManager().sendMessage(mCardMsg);
        }
        finish();
    }

    AlertDialog mAlertDialog;

    /**
     * 构建名片消息的内容
     * @param mCardMsg
     */
    private void createCommendmsg(EMMessage mCardMsg) {
        mCardMsg.setAttribute("type", "card");
        mCardMsg.setAttribute("avatar", com.tamic.novate.util.Environment.BASEULR_DEVELOPMENT + mPersonInfo.getAvatar());
        mCardMsg.setAttribute("clientid", mPersonInfo.getClientid());
        mCardMsg.setAttribute("nickname", mPersonInfo.getNickname());
        mCardMsg.setAttribute("userid", mPersonInfo.getUser_id());
        mCardMsg.setAttribute("gender", mPersonInfo.getGender());
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

    @OnClick({R.id.cancle, R.id.check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                finish();
                break;
            case R.id.check:
                if (isSingleSel) {
                    mCheck.setText("确认");
                    isSingleSel = false;
                    mAdapter.setSignle(isSingleSel);
                } else {
                    List<Integer> mSelected = mAdapter.getSelected();//这里面存的是被选择用户所在的position
                    List<Integer> str = new ArrayList<>();
                    for (int i : mSelected) {//获取到对应用户的环信id
                        str.add(mContactses.get(i).getUser_id());
                    }
                    showDialog(str);
                }
                break;
        }
    }
}
