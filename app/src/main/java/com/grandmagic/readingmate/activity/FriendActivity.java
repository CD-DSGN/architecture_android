package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import com.grandmagic.readingmate.bean.response.DataBean;
import com.grandmagic.readingmate.model.ContactModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.IndexBar;
import com.grandmagic.readingmate.view.SwipRecycleView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tamic.novate.NovateResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends AppBaseActivity {
    public static final int REQUEST_NEWFRIEND = 1;
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
    List<Contacts> mAdapterData = new ArrayList<>();
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    private static final String TAG = "FriendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initview();
        initdata();
    }

    List<Contacts> mSouseDatas = new ArrayList<>();
    List<String> mLetters = new ArrayList<>();

    private void initdata() {//模拟假数据
        initServerData();
        getallFriendfromEM();

    }

    private void getallFriendfromEM() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> mAllContactsFromServer = EMClient.getInstance().
                            contactManager().getAllContactsFromServer();//需要在子线程执行。否则会报303
                    for (int i = 0; i < mAllContactsFromServer.size(); i++) {
                        Log.e(TAG, "initdata: " + mAllContactsFromServer.get(i));
                    }
                } catch (HyphenateException mE) {
                    mE.printStackTrace();
                }
            }
        }).start();
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
                if (letter.equals(mSouseDatas.get(j).getLetter()))
                    mAdapterData.add(new Contacts(Contacts.TYPE.TYPE_FRIEND,mSouseDatas.get(j).getUser_id(),
                            mSouseDatas.get(j).getUser_name(),
                            mSouseDatas.get(j).getAvatar_native() ));
            }
        }
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
            String dataName = data.getUser_name();
            for (int j = 0; dataName!=null&&j < dataName.length(); j++) {
                pySb.append(Pinyin.toPinyin(dataName.charAt(j)));
            }
            data.setPyName(pySb.toString());//转化后的拼音
            String letter = pySb.length()>0?pySb.substring(0, 1):"#";//首字母
            if (letter.matches("[A-Z]")) {
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
        mModel.getAllFriendFromServer(new AppBaseResponseCallBack<NovateResponse<List<Contacts>>>(this) {
            @Override
            public void onSuccee(NovateResponse<List<Contacts>> response) {
                List<Contacts> mData = response.getData();
              mSouseDatas.addAll(mData);
                initsouseData();
                initadapterData();
            }
        });
    }

    MultiItemTypeAdapter mAdapter;

    private void initview() {
        mContext = this;
        mTitle.setText(R.string.read_friend);
        mTitlelayout.setBackgroundColor(Color.parseColor("#ffffff"));
        mTitle.setTextColor(Color.parseColor("#181e1d"));
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerviewFriend.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MultiItemTypeAdapter(mContext, mAdapterData);
        mAdapter.addItemViewDelegate(new ContactItemDelagate(mContext)).
                addItemViewDelegate(new ContactLetterDelagate()).
                addItemViewDelegate(new ContactNewFriendDelagate(mContext));
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
    }

    ContactModel mModel = new ContactModel(this);


}
