package com.grandmagic.readingmate.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.grandmagic.readingmate.bean.response.Contacts;
import com.grandmagic.readingmate.bean.response.DataBean;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.IndexBar;
import com.grandmagic.readingmate.view.SwipRecycleView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends AppBaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initdata();
        initview();
    }

    List<DataBean> mSouseDatas = new ArrayList<>();
    List<String> mLetters = new ArrayList<>();

    private void initdata() {//模拟假数据
        initTestData();
        initsouseData();
        initadapterData();
    }

    private void initadapterData() {
        mAdapterData.add(new Contacts(Contacts.TYPE.TYPE_NEWFRIEND));//新朋友的头部
        for (int i = 0; i < mLetters.size(); i++) {
            String letter=mLetters.get(i);
           Contacts mContacts=new Contacts(Contacts.TYPE.TYPE_LETTER,letter);
            mAdapterData.add(mContacts);
            for (int j = 0; j < mSouseDatas.size(); j++) {
            if (letter.equals(mSouseDatas.get(j).getTag()))
                mAdapterData.add(new Contacts(Contacts.TYPE.TYPE_FRIEND,mSouseDatas.get(j).getName()));
            }
        }
    }

    private void initsouseData() {
        int mSize = mSouseDatas.size();
        for (int i = 0; i < mSize; i++) {
            DataBean data = mSouseDatas.get(i);
            StringBuilder pySb = new StringBuilder();
            String dataName = data.getName();
            for (int j = 0; j < dataName.length(); j++) {
                pySb.append(Pinyin.toPinyin(dataName.charAt(j)));
            }
            data.setPyName(pySb.toString());//转化后的拼音
            String letter = pySb.substring(0, 1);//首字母
            if (letter.matches("[A-Z]")) {
                data.setTag(letter);
            } else {
                data.setTag("#");
            }
            if (!mLetters.contains(letter)) {
                mLetters.add(letter);
            }

        }
        sortData();
    }

    private void sortData() {
        Collections.sort(mSouseDatas, new Comparator<DataBean>() {
            @Override
            public int compare(DataBean o1, DataBean o2) {
                if (o1.getTag().equals("#")) return 1;
                else if (o2.getTag().equals("#")) return -1;
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

    private void initTestData() {
        for (int i = 0; i < 12; i++) {
            mSouseDatas.add(new DataBean("张三"));
            mSouseDatas.add(new DataBean("浪费的考虑过"));
            mSouseDatas.add(new DataBean("根本"));
            mSouseDatas.add(new DataBean("李四"));
            mSouseDatas.add(new DataBean("欧阳克"));
            mSouseDatas.add(new DataBean("肯定能发"));
            mSouseDatas.add(new DataBean("李四"));
            mSouseDatas.add(new DataBean("王五"));
            mSouseDatas.add(new DataBean("撒"));
            mSouseDatas.add(new DataBean("艾丝凡"));
            mSouseDatas.add(new DataBean("艾弗森"));
            mSouseDatas.add(new DataBean("李四"));
            mSouseDatas.add(new DataBean("爱疯"));
            mSouseDatas.add(new DataBean("阿芳认"));
            mSouseDatas.add(new DataBean("啊"));
            mSouseDatas.add(new DataBean("欧盟和"));
            mSouseDatas.add(new DataBean("哦你概况"));
            mSouseDatas.add(new DataBean("偶尔"));
        }
    }

    MultiItemTypeAdapter mAdapter;

    private void initview() {
        mContext = this;
        mTitle.setText(R.string.read_friend);
        mTitle.setTextColor(Color.parseColor("#181e1d"));
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerviewFriend.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MultiItemTypeAdapter(mContext, mAdapterData);
        mAdapter.addItemViewDelegate(new ContactItemDelagate(mContext));
        mAdapter.addItemViewDelegate(new ContactLetterDelagate());
        mAdapter.addItemViewDelegate(new ContactNewFriendDelagate(mContext));
        mRecyclerviewFriend.setAdapter(mAdapter);
        mIndexbar.setLayoutmanager(mLinearLayoutManager).setSouseData(mSouseDatas).setHintTextView(mHintText);

    }

    @OnClick({R.id.back, R.id.title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.title:
                break;

        }
    }


}
