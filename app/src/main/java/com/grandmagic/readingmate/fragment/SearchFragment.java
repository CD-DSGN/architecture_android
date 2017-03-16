package com.grandmagic.readingmate.fragment;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.adapter.SearchPersonAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.SearchPersonResponse;
import com.grandmagic.readingmate.listener.LocationListener;
import com.grandmagic.readingmate.model.SearchModel;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.tamic.novate.NovateResponse;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.util.SimpleRefreshListener;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass. test
 */
public class SearchFragment extends AppBaseFragment {

    LocationClient mLocationClient;
    BDLocationListener mBDLocationListener;
    Handler mLocationHandler;
    @BindView(R.id.btn_location)
    Button mBtnLocation;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.title_more)
    ImageView mTitleMore;
    @BindView(R.id.titlelayout)
    RelativeLayout mTitlelayout;
    @BindView(R.id.animaview)
    LottieAnimationView mAnimaview;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    SearchModel mModel;
    Context mContext;
    @BindView(R.id.rootview)
    RelativeLayout mRootview;
    int currpage = 1;
    private double mLongitude;
    private double mLatitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        AutoUtils.auto(view);
        initdata();
        initview();
        initoption();
        return view;
    }

    SearchPersonAdapter mAdapter;
    List<SearchPersonResponse.InfoBean> mPersonList = new ArrayList<>();

    private void initview() {
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 36);
        mBack.setVisibility(View.GONE);
        AutoUtils.autoTextSize(mTitle);
        mTitleMore.setImageResource(R.drawable.ic_location);
        mAnimaview.setImageAssetsFolder("images/");//为有图片资源的动画设置路径
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SearchPersonAdapter(mContext, mPersonList);
        mRecyclerview.setAdapter(mAdapter);
        initRefresh();
    }

    private void initdata() {
        mContext = getActivity();
        mLocationClient = new LocationClient(mContext);
        mModel = new SearchModel(getActivity());
        mLocationHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                BDLocation location = (BDLocation) msg.obj;
                updateLocation(location);
            }

        };
        mBDLocationListener = new LocationListener(mLocationHandler);
    }

    boolean isPlay;

    /**
     * 开始定位并且播放动画
     */
    private void startLocationAndPlayAnimation() {
        mAnimaview.playAnimation();
        mLocationClient.start();
        mRootview.setBackgroundResource(R.color.text_black);
        isPlay = true;
        mBtnLocation.setVisibility(View.GONE);
        mTvStatus.setVisibility(View.VISIBLE);
    }

    /**
     * 上传自己的位置信息以获取附近的人
     *
     * @param mLocation
     */
    private void updateLocation(BDLocation mLocation) {
        mLongitude = mLocation.getLongitude();
        mLatitude = mLocation.getLatitude();
        mLocationClient.stop();
        mModel.stepLocation(mLatitude, mLongitude, new AppBaseResponseCallBack<NovateResponse>(mContext) {
            @Override
            public void onSuccee(NovateResponse response) {
                getLocationPerson();
            }
        });
    }

    /**
     * 获取附近的人
     */
    public void getLocationPerson() {
        mModel.getLocationPerson(currpage, new AppBaseResponseCallBack<NovateResponse<SearchPersonResponse>>(mContext) {
            @Override
            public void onSuccee(NovateResponse<SearchPersonResponse> response) {
                mAnimaview.cancelAnimation();
                isPlay=false;
                mRefreshLayout.setVisibility(View.VISIBLE);
                mAnimaview.setVisibility(View.GONE);
                mBtnLocation.setVisibility(View.GONE);
                mTvStatus.setVisibility(View.GONE);
                mTitleMore.setVisibility(View.VISIBLE);
                mTitle.setTextColor(getResources().getColor(R.color.text_black));
                mRootview.setBackgroundColor(getResources().getColor(R.color.white));
                mPersonList.addAll(response.getData().getInfo());
                mAdapter.refreshData(mPersonList);
            }
        });
    }

    @OnClick({R.id.title_more, R.id.btn_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_more:
                mRefreshLayout.setVisibility(View.GONE);
                mAnimaview.setVisibility(View.VISIBLE);
                mBtnLocation.setVisibility(View.VISIBLE);
                mTvStatus.setVisibility(View.GONE);
                mTitleMore.setVisibility(View.GONE);
                mRootview.setBackgroundColor(getResources().getColor(R.color.text_green));
                mTitle.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_location:
                new RxPermissions(getActivity()).request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean mBoolean) {
                        if (mBoolean) {
                            mPersonList.clear();
                            startLocationAndPlayAnimation();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setSystemBarColor(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        setSystemBarColor(hidden);
        if (hidden) {//当用户切换界面的时候进行取消播放
            mAnimaview.cancelAnimation();
        } else {
            if (isPlay)
                mAnimaview.playAnimation();
        }
    }

    private void setSystemBarColor(boolean hidden) {
        if (!hidden) {
            ((AppBaseActivity) getActivity()).setSystemBarColor(R.color.bg_search);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stop();
    }

    /**
     * 初始化上啦加载更多
     */
    private void initRefresh() {
        BGAStickinessRefreshViewHolder mRefreshViewHolder = new BGAStickinessRefreshViewHolder(mContext, true);
        mRefreshViewHolder.setStickinessColor(R.color.colorAccent);
        mRefreshViewHolder.setRotateImage(R.drawable.bga_refresh_stickiness);
//        mRefreshLayout.offsetTopAndBottom(88);
        mRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        mRefreshLayout.setPullDownRefreshEnable(false);
        mRefreshLayout.setDelegate(new SimpleRefreshListener() {
            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.endLoadingMore();
                    }
                }, 2000);
                return true;
            }
        });
    }

    /**
     * 定位的配置
     */
    public void initoption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(mBDLocationListener);
    }
}
