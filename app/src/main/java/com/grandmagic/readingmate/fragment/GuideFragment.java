package com.grandmagic.readingmate.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.grandmagic.readingmate.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.webView)
    WebView mWebView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    private void intitview() {
//        int mPosition = getArguments().getInt("position", -1);
//        if (mPosition==-1)return;
//        mAnimaview.setAnimation("guidepage"+mPosition+".json");
//        mAnimaview.setImageAssetsFolder("guidepage"+mPosition+"/");
//        mAnimaview.playAnimation();
//        mWebView.loadUrl("file:///android_asset/guide1.gif");
        mWebView.loadUrl("file:///android_asset/guide.html");
        WebSettings mSettings = mWebView.getSettings();
        mSettings.setJavaScriptEnabled(true);
        mSettings.setAppCacheEnabled(false);
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


    }

    @Override
    public void onResume() {
        super.onResume();
        intitview();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden){
//            mWebView.loadUrl("file:///android_asset/guide.html");
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            mWebView.loadUrl("file:///android_asset/guide.html");
//        }
//        if (isVisibleToUser) {
//           mWebView.loadUrl("javascript:funFromjs()");
//        }
        Log.d(TAG, "setUserVisibleHint() called with: isVisibleToUser = [" + isVisibleToUser + "]");
    }

    private static final String TAG = "GuideFragment";
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
