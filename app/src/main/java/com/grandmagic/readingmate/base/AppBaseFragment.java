package com.grandmagic.readingmate.base;


import android.app.Fragment;

import com.tamic.novate.RxApiManager;

/**
 * Created by zhangmengqi on 2017/1/22.
 */

public class AppBaseFragment extends Fragment {
     RxApiManager mRxApiManager = new RxApiManager();
    @Override
    public void onDestroy() {
        super.onDestroy();
      mRxApiManager.removeAll();
    }
}
