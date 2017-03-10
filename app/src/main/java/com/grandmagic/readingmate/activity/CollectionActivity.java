package com.grandmagic.readingmate.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionActivity extends AppBaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.pre_page)
    ImageButton mPrePage;
    @BindView(R.id.next_page)
    ImageButton mNextPage;
    @BindView(R.id.vp_collect_books)
    ViewPager mVpCollectBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        AutoUtils.auto(this);
        initView();
    }

    private void initView() {
        mTitle.setText(R.string.collect);
        mVpCollectBooks.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater inflater = LayoutInflater.from(CollectionActivity.this);
                View v = inflater.inflate(R.layout.item_vp_book_details, mVpCollectBooks, false);
                AutoUtils.auto(v);
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View)object);
            }
        });

    }

    @OnClick({R.id.back, R.id.title, R.id.pre_page, R.id.next_page, R.id.vp_collect_books})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title:
                break;
            case R.id.pre_page:
                ViewUtils.showToast("上一页");
                break;
            case R.id.new_friend:
                ViewUtils.showToast("下一页");
                break;
            case R.id.vp_collect_books:
                break;
            default:
                break;
        }
    }
}
