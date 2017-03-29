package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.hyphenate.chat.EMTextMessageBody;
import com.tamic.novate.util.Environment;

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
                    mObjects.add(position);
                    showDialog(mObjects);
                }
            }
        });
    }

    /**
     * 弹出确认发送的窗口窗口
     *
     * @param mSelected position
     */
    private void showDialog(final List<Integer> mSelected) {
        View mView = View.inflate(this, R.layout.dialog_recommend, null);
        Button cancle = (Button) mView.findViewById(R.id.no);
        Button send = (Button) mView.findViewById(R.id.yes);
        TextView recommendUser = (TextView) mView.findViewById(R.id.user);
        TextView recommendname = (TextView) mView.findViewById(R.id.name);
        final EditText levmsg = (EditText) mView.findViewById(R.id.et_leavemsg);
        recommendname.setText("[个人书签]" + mPersonInfo.getNickname());
        StringBuilder mBuilder = new StringBuilder();
        for (int i = 0; i < mSelected.size(); i++) {
            mBuilder.append(mContactses.get(mSelected.get(i)).getUser_name());
            if (i != mSelected.size() - 1) {
                mBuilder.append("、");
            }
        }
        mBuilder.append("(" + mSelected.size() + "人)");
        recommendUser.setText(mBuilder);
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
                List<Integer> str = new ArrayList<>();
                for (int i : mSelected) {//获取到对应用户的环信id
                    str.add(mContactses.get(i).getUser_id());
                }
                sendRecommentMsg(str, levmsg.getText().toString());
            }
        });
        mAlertDialog.show();
    }

    /**
     * 遍历所有需要发消息的人然后发送名片
     *
     * @param mSelected
     * @param leavemsg  留言内容
     */
    private void sendRecommentMsg(List<Integer> mSelected, String leavemsg) {
        for (int mUser_id : mSelected) {
            /*因为直接 createTxtSendMessage必须要求填入body里面的文本。不然会返回null，所以改为现在这种
             */
            EMMessage mCardMsg = EMMessage.createSendMessage(EMMessage.Type.TXT);
            if (mCardMsg == null) return;
            EMTextMessageBody mBody = new EMTextMessageBody(leavemsg);
            mCardMsg.addBody(mBody);
            mCardMsg.setTo(mUser_id + "");
            createCommendmsg(mCardMsg);
            EMClient.getInstance().chatManager().sendMessage(mCardMsg);
        }
        // TODO: 2017/3/28 这里可以做一个推荐成功的提示
        finish();
    }

    AlertDialog mAlertDialog;// 推荐的弹窗

    /**
     * 构建名片消息的内容
     *
     * @param mCardMsg
     */
    private void createCommendmsg(EMMessage mCardMsg) {
        mCardMsg.setAttribute("type", "card");
        mCardMsg.setAttribute("avatar", Environment.BASEULR_PRODUCTION + mPersonInfo.getAvatar());
        mCardMsg.setAttribute("clientid", mPersonInfo.getClientid());
        mCardMsg.setAttribute("nickname", mPersonInfo.getNickname());
        mCardMsg.setAttribute("userid", mPersonInfo.getUser_id());
        mCardMsg.setAttribute("gender", mPersonInfo.getGender());
        mCardMsg.setAttribute("signature", mPersonInfo.getSignature());
    }

    PersonInfo mPersonInfo;// 从个人详情页面传递过来的用户信息

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
                    showDialog(mSelected);
                }
                break;
        }
    }
}
