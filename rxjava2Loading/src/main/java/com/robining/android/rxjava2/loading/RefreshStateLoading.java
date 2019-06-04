package com.robining.android.rxjava2.loading;

import com.robining.android.stateview.StateViewAssist;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RefreshStateLoading<T> implements ObservableTransformer<T, T> {
    private IRefreshLayout refreshLayout;
    private boolean isRefresh;
    private StateViewAssist stateViewAssist;
    private InfoProvider<T> infoProvider;

    private T data;
    private boolean isRunOnNext = false;
    private Throwable exception;
    private boolean isUseRefreshMode = false;

    private RefreshStateLoading(IRefreshLayout refreshLayout, StateViewAssist stateViewAssist, boolean isRefresh, InfoProvider<T> infoProvider) {
        this.stateViewAssist = stateViewAssist;
        if (stateViewAssist.getTarget() == null) {
            throw new IllegalArgumentException("the stateViewAssist default target cannot be null");
        }
        this.refreshLayout = refreshLayout;
        this.isRefresh = isRefresh;
        this.infoProvider = infoProvider;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (isRefresh) {
                            if (infoProvider.pageIsHaveData()) {
                                isUseRefreshMode = true;
                                refreshLayout.onStartRefresh();
                            } else {
                                stateViewAssist.show(StateViewAssist.STATE_LOADING, null);
                            }
                        } else {
                            refreshLayout.onLoadMore();
                        }
                    }
                })
                .doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        data = t;
                        isRunOnNext = true;
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        exception = throwable == null ? new UnKnowException() : throwable;
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        exception = new DisposeException();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (isRunOnNext && exception != null) { //成功
                            if (isRefresh) {
                                if (isUseRefreshMode) {
                                    refreshLayout.refreshCompleted();
                                }
                            } else {
                                refreshLayout.loadMoreCompleted();
                            }
                            //判断是否加载完数据
                            if (infoProvider.isLoadedAll(data)) {
                                refreshLayout.loadedAll();
                            }

                            //若用户已自主切换了布局则不再处理
                            if (stateViewAssist.getCurrentState() == StateViewAssist.STATE_LOADING) {
                                //若此时页面仍然没有数据，切换至空布局;若已有数据恢复正常页面
                                if (infoProvider.pageIsHaveData()) {
                                    stateViewAssist.restore();
                                } else {
                                    stateViewAssist.show(StateViewAssist.STATE_EMPTY, null);
                                }
                            }
                        } else { //失败
                            Throwable ex = exception == null ? new UnKnowException() : exception;
                            if (isRefresh) {
                                if (isUseRefreshMode) {
                                    refreshLayout.refreshFailed(ex);
                                } else if(stateViewAssist.getCurrentState() == StateViewAssist.STATE_LOADING){
                                    infoProvider.onSwitchErrorLayout(ex, stateViewAssist);
                                }
                            } else {
                                refreshLayout.loadMoreFailed(ex);
                                //若用户已自主切换了布局则不再处理
                                if (stateViewAssist.getCurrentState() == StateViewAssist.STATE_LOADING) {
                                    stateViewAssist.restore();
                                }
                            }
                        }
                    }
                });
    }

    public static <T> RefreshStateLoading<T> create(IRefreshLayout refreshLayout, StateViewAssist stateViewAssist, boolean isRefresh, InfoProvider<T> infoProvider) {
        return new RefreshStateLoading<>(refreshLayout, stateViewAssist, isRefresh, infoProvider);
    }

    public interface InfoProvider<T> {
        boolean pageIsHaveData();

        boolean isLoadedAll(T data);

        void onSwitchErrorLayout(Throwable ex, StateViewAssist stateViewAssist);
    }
}
