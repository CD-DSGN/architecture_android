package com.grandmagic.readingmate.fragment;


import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.grandmagic.readingmate.R;
import com.grandmagic.readingmate.base.AppBaseFragment;
import com.grandmagic.readingmate.listener.LocationListener;
import com.grandmagic.readingmate.view.IrregularImageView;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.location)
    TextView mTVLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        mLocationClient = new LocationClient(getActivity());
        initdata();
        initoption();
        return view;
    }

    private void initdata() {
        mLocationHandler = new Handler() {
            int i = 0;

            @Override
            public void handleMessage(Message msg) {
                String location = (String) msg.obj;
                if (!location.contains("161") && i < 5) {
                    mLocationClient.stop();
                    Toast.makeText(getActivity(), "获取位置信息失败", Toast.LENGTH_SHORT).show();
                    mLocationClient.start();
                    mTVLocation.setText(location);

                    i++;
                } else {
                    mTVLocation.setText(location);
                    mLocationClient.stop();
                }
            }
        };
        mBDLocationListener = new LocationListener(mLocationHandler);
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stop();
    }

    @OnClick(R.id.btn_location)
    public void onClick() {
        new RxPermissions(getActivity()).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean mBoolean) {
                if (mBoolean) {
                    mLocationClient.start();
                }
            }
        });
    }
}
