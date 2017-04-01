package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.ContactItemDelagate;
import com.grandmagic.readingmate.adapter.ContactLetterDelagate;
import com.grandmagic.readingmate.adapter.ContactNewFriendDelagate;
import com.grandmagic.readingmate.adapter.MultiItemTypeAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.db.DBHelper;
import com.grandmagic.readingmate.event.ContactDeletedEvent;
import com.grandmagic.readingmate.event.FriendDeleteEvent;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.IndexBar;
import com.grandmagic.readingmate.view.SwipRecycleView;
import com.orhanobut.logger.Logger;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends AppBaseActivity implements ContactItemDelagate.remarkListener {
    public static final int REQUEST_NEWFRIEND = 1;
    public static final String NEW_FRIEND="new_friend";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recyclerview_friend)
    SwipRecycleView mRecyclerviewFriend;
    Context mContext;
    @BindView(R.id.indexbar)
    IndexBar mIndexbar;
    @BindView(R.id.hintText)
    TextView mHintText;
    List<Contacts> mAdapterData = new ArrayList<>();//最终adapter需要的列表
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    private static final String TAG = "FriendActivity";
    int newFriendCOunt;//好友邀请数量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        EventBus.getDefault().register(this);
        initview();
        initdata();
    }

    List<Contacts> mSouseDatas = new ArrayList<>();//服务端返回的列表
    List<String> mLetters = new ArrayList<>();//首字母 A，B。。Z

    private void initdata() {
        newFriendCOunt = getIntent().getIntExtra(NEW_FRIEND, 0);

        initServerData();
    }

    /**
     * 生成adapter需要的list
     */
    private void initadapterData() {
        mAdapterData.add(new Contacts(Contacts.TYPE.TYPE_NEWFRIEND));//新朋友的头部
        for (int i = 0; i < mLetters.size(); i++) {
            String letter = mLetters.get(i);
            mAdapterData.get(mAdapterData.size() - 1).setNeedline(false);
            Contacts mContacts = new Contacts(Contacts.TYPE.TYPE_LETTER, letter);//字母
            mAdapterData.add(mContacts);
            for (int j = 0; j < mSouseDatas.size(); j++) {
                if (letter.equals(mSouseDatas.get(j).getLetter())) {
                    mAdapterData.add(mSouseDatas.get(j).setType(Contacts.TYPE.TYPE_FRIEND));
                    DBHelper.getContactsDao(FriendActivity.this).insertOrReplace(mSouseDatas.get(j));
                }
            }
        }
        Logger.e("更新好友列表");
        mAdapter.setData(mAdapterData);
    }

    /**
     * 对名字进行转化和排序处理
     */
    private void initsouseData() {
        int mSize = mSouseDatas.size();
        for (int i = 0; i < mSize; i++) {
            Contacts data = mSouseDatas.get(i);
            StringBuilder pySb = new StringBuilder();
            String dataName = TextUtils.isEmpty(data.getRemark())?data.getUser_name(): data.getRemark();//如果有备注就按备注的处理
            for (int j = 0; dataName != null && j < dataName.length(); j++) {
                pySb.append(Pinyin.toPinyin(dataName.charAt(j)));
            }
            data.setPyName(pySb.toString());//转化后的拼音
            String letter = pySb.length() > 0 ? pySb.substring(0, 1) : "#";//首字母
            if (letter.matches("[A-Za-z]")) {
                data.setLetter(letter);
            } else {
                data.setLetter("#");
            }
            if (!mLetters.contains(letter)) {
                mLetters.add(letter);
            }

        }
        sortData();
    }

    private void sortData() {
        Collections.sort(mSouseDatas, new Comparator<Contacts>() {
            @Override
            public int compare(Contacts o1, Contacts o2) {
                if (o1.getLetter().equals("#")) return 1;
                else if (o2.getLetter().equals("#")) return -1;
                return o1.getPyName().compareTo(o2.getPyName());
            }
        });
        Collections.sort(mLetters, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals("#")) return 1;
                else if (o2.equals("#")) return -1;
                return o1.compareTo(o2);
            }
        });
    }

    private void initServerData() {
        mAdapterData.add(new Contacts(Contacts.TYPE.TYPE_NEWFRIEND,newFriendCOunt));//新朋友的头部
        mModel.getAllFriendFromServer(new AppBaseResponseCallBack<NovateResponse<List<Contacts>>>(this) {
            @Override
            public void onSuccee(NovateResponse<List<Contacts>> response) {
                mSouseDatas.clear();
                mAdapterData.clear();
                mLetters.clear();//防止删除好友的时候重新加载好友列表数据重复
                List<Contacts> mData = response.getData();
                mSouseDatas.addAll(mData);
                initsouseData();
                initadapterData();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    MultiItemTypeAdapter mAdapter;
    ContactNewFriendDelagate mContactNewFriendDelagate;

    private void initview() {
        mContext = this;

        mTitle.setText(R.string.read_friend);
        mTitlelayout.setBackgroundColor(Color.parseColor("#ffffff"));
        mTitle.setTextColor(Color.parseColor("#181e1d"));
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerviewFriend.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MultiItemTypeAdapter(mContext, mAdapterData);
        mContactNewFriendDelagate = new ContactNewFriendDelagate(mContext);
        mAdapter.addItemViewDelegate(new ContactItemDelagate(mContext).setRemarkListener(this)).//好友
                addItemViewDelegate(new ContactLetterDelagate()).//字母

                addItemViewDelegate(mContactNewFriendDelagate);//新朋友的头部
        mRecyclerviewFriend.setAdapter(mAdapter);
        mIndexbar.setLayoutmanager(mLinearLayoutManager).
                setSouseData(mSouseDatas).
                setHintTextView(mHintText);

    }

    @OnClick({R.id.back, R.id.title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_NEWFRIEND){
          mContactNewFriendDelagate.read();
        }
    }

    ContactModel mModel = new ContactModel(this);

    /**
     * 当移除好友的时候
     * @param mEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void frienddelete(FriendDeleteEvent mEvent){
      initServerData();
    }

    /**
     * 当被移除的时候
     * @param mEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contactDelete(ContactDeletedEvent mEvent){
        initServerData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置备注
     * @param mMessage
     * @param mRemarkPosition
     */
    @Override
    public void remark(String mMessage, int mRemarkPosition) {
        mAdapterData.get(mRemarkPosition).setRemark(mMessage);
        mAdapter.setData(mAdapterData);
    }
}
