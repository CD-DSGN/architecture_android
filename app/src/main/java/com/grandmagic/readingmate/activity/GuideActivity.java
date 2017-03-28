package com.grandmagic.readingmate.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.fragment.GuideFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

//用户首次进入的引导页 lps
public class GuideActivity extends AppBaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initview();

    }


    List<Fragment> mViews = new ArrayList<>();

    private void initview() {
        for (int i = 0; i < 3; i++) {
            GuideFragment mGuideFragment=new GuideFragment();
            Bundle mBundle=new Bundle();
            mBundle.putInt("position",i);
            mGuideFragment.setArguments(mBundle);
            mViews.add(mGuideFragment);
        }
    mViewpager.setAdapter(new Myadapter(getSupportFragmentManager()));



    }



class Myadapter extends FragmentPagerAdapter{

    public Myadapter(FragmentManager fm) {
        super(fm);
    }



    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return mViews.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mViews.size();
    }
}
}

