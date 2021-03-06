package com.grandmagic.readingmate.fragment;


import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.activity.AddFriendActivity;
import com.grandmagic.readingmate.adapter.DefaultEmptyAdapter;
import com.grandmagic.readingmate.adapter.SearchPersonAdapter;
import com.grandmagic.readingmate.base.AppBaseActivity;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.base.AppBaseResponseCallBack;
import com.grandmagic.readingmate.bean.response.SearchPersonResponse;
import com.grandmagic.readingmate.listener.LocationListener;
import com.grandmagic.readingmate.model.SearchModel;
import com.grandmagic.readingmate.model.SearchUserModel;
import com.grandmagic.readingmate.ui.CustomDialog;
import com.grandmagic.readingmate.utils.AutoUtils;
import com.grandmagic.readingmate.utils.InputMethodUtils;
import com.refreshlab.PullLoadMoreRecyclerView;
import com.tamic.novate.NovateResponse;
import com.tamic.novate.Throwable;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass. test
 */
public class SearchFragment extends AppBaseFragment implements SearchPersonAdapter.AdapterListener {

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
    PullLoadMoreRecyclerView mRecyclerview;
    SearchModel mModel;
    Context mContext;
    @BindView(R.id.rootview)
    RelativeLayout mRootview;
    int currpage = 1, pagecount = 1;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
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

    DefaultEmptyAdapter mEmptyAdapter;
    List<SearchPersonResponse.InfoBean> mPersonList = new ArrayList<>();

    private void initview() {
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 36);
        mTitle.setTextColor(Color.WHITE);
        mTitle.setText("以书会友");
        mBack.setVisibility(View.VISIBLE);
        AutoUtils.autoTextSize(mTitle);
        mTitleMore.setImageResource(R.drawable.ic_location);
        mAnimaview.setImageAssetsFolder("images/");//为有图片资源的动画设置路径
        mRecyclerview.setLinearLayout();
        SearchPersonAdapter mAdapter = new SearchPersonAdapter(mContext, mPersonList);
        mAdapter.setListener(this);
        mEmptyAdapter = new DefaultEmptyAdapter(mAdapter, mContext);
        mRecyclerview.setAdapter(mEmptyAdapter);
        initRefresh();
        setSystemBarColor(false);
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
        systembarColor = R.color.text_black;
        mAnimaview.playAnimation();
        mAnimaview.setVisibility(View.VISIBLE);
        mIvSearch.setVisibility(View.GONE);
        mLocationClient.start();
        mRootview.setBackgroundResource(R.color.text_black);
        isPlay = true;
        mBtnLocation.setVisibility(View.GONE);
        mTvStatus.setVisibility(View.VISIBLE);
        setSystemBarColor(false);
    }

    /**
     * 上传自己的位置信息以获取附近的人
     *
     * @param mLocation
     */
    private void updateLocation(BDLocation mLocation) {
        mLongitude = mLocation.getLongitude();
        mLatitude = mLocation.getLatitude();
        Address mAddress = mLocation.getAddress();
        String mProvince = mAddress.province;
        String mCity = mAddress.city;
        String mDistrict = mAddress.district;
        String mStreet = mAddress.street;
        mLocationClient.stop();
        if (TextUtils.isEmpty(mCity)) {
            Log.e(TAG, "updateLocation() called with: mLocation = [" + mLocation + "]");
            Toast.makeText(mContext, "定位失败" + mLocation.getLocType(), Toast.LENGTH_SHORT).show();
            reset();
            return;
        }
        mModel.stepLocation(mLatitude, mLongitude, mProvince, mCity, mDistrict, mStreet, new AppBaseResponseCallBack<NovateResponse>(mContext) {
            @Override
            public void onSuccee(NovateResponse response) {
                getLocationPerson();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                reset();//失败重置。不然一直在转圈圈
                Log.e(TAG, "onError() called with: e = [" + e + "]");

            }
        });
    }

    /**
     * 获取附近的人
     */
    public void getLocationPerson() {
        currpage = 1;
        mModel.getLocationPerson(currpage, new AppBaseResponseCallBack<NovateResponse<SearchPersonResponse>>(mContext) {
            @Override
            public void onSuccee(NovateResponse<SearchPersonResponse> response) {
                mAnimaview.cancelAnimation();
                isPlay = false;
                systembarColor = R.color.white;
                mRecyclerview.setVisibility(View.VISIBLE);
                mAnimaview.setVisibility(View.GONE);
                mBtnLocation.setVisibility(View.GONE);
                mTvStatus.setVisibility(View.GONE);
                mBack.setVisibility(View.VISIBLE);
                mTitle.setTextColor(getResources().getColor(R.color.text_black));
                mRootview.setBackgroundColor(getResources().getColor(R.color.white));
                pagecount = response.getData().getPage();
                if (response.getData().getInfo() != null && !response.getData().getInfo().isEmpty())
                    mPersonList.addAll(response.getData().getInfo());
                mEmptyAdapter.refresh();
                setSystemBarColor(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mEmptyAdapter.refresh();
                reset();
                Log.e(TAG, "onError() called with: e = [" + e + "]");
            }
        });
    }

    @OnClick({R.id.back, R.id.btn_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Log.e(TAG, "onClick() called with: view = [" + view + "]");
                reset();
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

    /**
     * 恢复默认状态
     */
    private static final String TAG = "SearchFragment";

    private void reset() {
        Log.e(TAG, "reset() called");
        currpage=1;pagecount=1;
        systembarColor = R.color.bg_search;
        mRecyclerview.setVisibility(View.GONE);
        mAnimaview.setVisibility(View.GONE);
        mBtnLocation.setVisibility(View.VISIBLE);
        mTvStatus.setVisibility(View.GONE);
        mBack.setVisibility(View.GONE);
        mIvSearch.setVisibility(View.VISIBLE);
        mRootview.setBackgroundColor(getResources().getColor(R.color.search_green));
        mTitle.setTextColor(getResources().getColor(R.color.white));
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

    int systembarColor = R.color.bg_search;
@Override
   public   void setSystemBarColor(boolean hidden) {
        if (!hidden) {
            ((AppBaseActivity) getActivity()).setSystemBarColor(systembarColor);
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
        mRecyclerview.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                currpage = 1;
                loadPerson(currpage);
            }

            @Override
            public void onLoadMore() {
                if (currpage < pagecount) {
                    currpage++;
                    loadPerson(currpage);
                } else {
                    mRecyclerview.setPullLoadMoreCompleted();
                }
            }
        });
    }

    private void loadPerson(final int mCurrpage) {
        mModel.getLocationPerson(mCurrpage, new AppBaseResponseCallBack<NovateResponse<SearchPersonResponse>>(mContext) {
            @Override
            public void onSuccee(NovateResponse<SearchPersonResponse> response) {
                mRecyclerview.setPullLoadMoreCompleted();
                if (mCurrpage == 1) {
                    mPersonList.clear();
                }
                if (response.getData().getInfo() != null && !response.getData().getInfo().isEmpty())
                    mPersonList.addAll(response.getData().getInfo());
                mEmptyAdapter.refresh();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRecyclerview.setPullLoadMoreCompleted();
                mEmptyAdapter.refresh();
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
        option.setOpenGps(false);
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

    @Override
    public void addfriend(int userid, int pos) {
        showAddFriendDialog(userid + "", pos);
    }

    private void showAddFriendDialog(final String mUserid, final int pos) {
        final CustomDialog mDialog = new CustomDialog(mContext);
        mDialog.setMaxNum(20);
        mDialog.setYesStr("发送");
        mDialog.setTitle(getString(R.string.add_friend_request));
        mDialog.setOnBtnOnclickListener(new CustomDialog.BtnOnclickListener() {
            @Override
            public void onYesClick() {
                addContact(mDialog.getMessage(), mUserid, pos);
                mDialog.dismiss();
            }

            @Override
            public void onNoClick() {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    /**
     * 添加好友，
     *
     * @param mUser_id
     * @param reason
     */
    private void addContact(String reason, String mUser_id, final int pos) {
        SearchUserModel mSearchUserModel = new SearchUserModel(mContext);
        mSearchUserModel.requestAddFriend(mUser_id, reason, new AppBaseResponseCallBack<NovateResponse>(mContext) {
            @Override
            public void onSuccee(NovateResponse response) {
                Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                //发送成功暂时隐藏掉添加好友的Relativelayout
                mPersonList.get(pos).setIs_friend(1);
                mEmptyAdapter.refresh();
            }
        });
    }
}
