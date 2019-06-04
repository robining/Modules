package com.robining.android.rxjava2.loading;

/**
 * 刷新组件的基本功能
 * Created by LuoHaifeng on 2017/3/9.
 */

public interface IRefreshLayout {
    /**
     * 刷新回调
     */
    void onStartRefresh();

    /**
     * 加载更多回调
     */
    void onLoadMore();

    /**
     * 刷新完成
     */
    void refreshCompleted();

    /**
     * 刷新失败
     */
    void refreshFailed(Throwable ex);

    /**
     * 加载更多完成
     */
    void loadMoreCompleted();

    /**
     * 加载更多失败
     */
    void loadMoreFailed(Throwable ex);

    /**
     * 加载结束
     */
    void loadedAll();
}
