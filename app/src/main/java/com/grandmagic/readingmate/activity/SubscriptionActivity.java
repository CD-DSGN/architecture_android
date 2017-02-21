package com.grandmagic.readingmate.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommonAdapter;
import com.grandmagic.readingmate.adapter.CommonPagerAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscriptionActivity extends AppBaseActivity {
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.subscription)
    TextView mSubscription;
    @BindView(R.id.iv_dashline_subscription)
    View mIvDashlineSubscription;
    @BindView(R.id.subscriped)
    TextView mSubscriped;
    @BindView(R.id.iv_dashline_subscriped)
    View mIvDashlineSubscriped;
    @BindView(R.id.vp_subscription)
    ViewPager mVpSubscription;

    List<View> view_List = new ArrayList<>();

    //    @BindView(R.id.subscription_list)
    //    RecyclerView mSubscriptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        AutoUtils.auto(this);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        mTitle.setText(R.string.subscription);
        generateSubscriptionList();
        generateSuscribedList();
        mVpSubscription.setAdapter(new CommonPagerAdapter(view_List));
        mVpSubscription.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIvDashlineSubscription.setVisibility(View.VISIBLE);
                    mIvDashlineSubscriped.setVisibility(View.GONE);
                    mSubscription.setTextColor(Color.parseColor("#1cc9a2"));
                    mSubscriped.setTextColor(Color.parseColor("#191921"));
                }else{
                    mIvDashlineSubscription.setVisibility(View.GONE);
                    mIvDashlineSubscriped.setVisibility(View.VISIBLE);
                    mSubscription.setTextColor(Color.parseColor("#191921"));
                    mSubscriped.setTextColor(Color.parseColor("#1cc9a2"));
                }



            }
        });

        //        mSubscriptionList.setLayoutManager(new LinearLayoutManager(this));
        //        ArrayList<String> data = new ArrayList<>();
        //        for (int i = 0; i < 50; i++) {
        //            data.add("张三");
        //            data.add("李四");
        //        }
        //        mSubscriptionList.setAdapter(
        //                new CommonAdapter<String>(SubscriptionActivity.this, R.layout.item_subscription, data) {
        //
        //                    @Override
        //                    protected void convert(ViewHolder holder, Object o, int position) {
        //
        //                    }
        //                });
    }

    private void generateSuscribedList() {
        RecyclerView rv_subscriped = new RecyclerView(this);
        rv_subscriped.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("张三");
            data.add("李四");
        }
        rv_subscriped.setAdapter(
                new CommonAdapter<String>(SubscriptionActivity.this, R.layout.item_subscription, data) {
                    @Override
                    protected void convert(ViewHolder holder, String o, int position) {

                    }
                }
        );
        view_List.add(rv_subscriped);
    }

    private void generateSubscriptionList() {
        RecyclerView rv_subscription = new RecyclerView(this);
        rv_subscription.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("张三");
            data.add("李四");
        }
        rv_subscription.setAdapter(
                new CommonAdapter<String>(SubscriptionActivity.this, R.layout.item_subscription, data) {
                    @Override
                    protected void convert(ViewHolder holder, String o, int position) {

                    }
                }
        );
        view_List.add(rv_subscription);
    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
