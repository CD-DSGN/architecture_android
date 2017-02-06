package com.grandmagic.readingmate.base;


import android.app.Fragment;

import com.tamic.novate.RxApiManager;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class AppBaseFragment extends Fragment {
    private static RxApiManager mRxApiManager = RxApiManager.get();
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRxApiManager != null) {
            mRxApiManager.cancelAll();
        }
    }
}
