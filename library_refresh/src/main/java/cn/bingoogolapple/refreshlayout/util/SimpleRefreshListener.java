package cn.bingoogolapple.refreshlayout.util;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by lps on 2017/3/16.
 */

public class SimpleRefreshListener implements BGARefreshLayout.BGARefreshLayoutDelegate{
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
