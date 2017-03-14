package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.CommonPagerAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.model.BookModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.view.HotcommentView;
import com.grandmagic.readingmate.view.IrregularImageView;
import com.grandmagic.readingmate.view.StarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookDetailActivity extends AppBaseActivity {
    public static final String ISBN_CODE = "isbn_code";
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.bookname)
    TextView mBookname;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.author)
    TextView mAuthor;
    @BindView(R.id.ratingbar)
    StarView mRatingbar;
    @BindView(R.id.score)
    TextView mScore;
    @BindView(R.id.num_people)
    TextView mNumPeople;
    @BindView(R.id.t_publisher)
    TextView mTPublisher;
    @BindView(R.id.tv_publisher)
    TextView mTvPublisher;
    @BindView(R.id.t_publishtime)
    TextView mTPublishtime;
    @BindView(R.id.tv_publistime)
    TextView mTvPublistime;
    @BindView(R.id.about)
    TextView mAbout;
    @BindView(R.id.iv_conver)
    IrregularImageView mIvConver;
    @BindView(R.id.lin_collection)
    LinearLayout mLinCollection;
    @BindView(R.id.collectionNum)
    TextView mCollectionNum;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.iv1)
    ImageView mIv1;
    @BindView(R.id.bottomlayout)
    LinearLayout mBottomlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        setTranslucentStatus(true);
        initdata();
        initView();
    }

    private void initView() {
        List<View> mViews = new ArrayList<>();
        View mRecentView = new HotcommentView(this);
        View mHotView = new HotcommentView(this);
        mViews.add(mRecentView);
        mViews.add(mHotView);
        mViewpager.setAdapter(new CommonPagerAdapter(mViews));
    }

    BookModel mModel;

    private void initdata() {
        mModel = new BookModel(this);

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
