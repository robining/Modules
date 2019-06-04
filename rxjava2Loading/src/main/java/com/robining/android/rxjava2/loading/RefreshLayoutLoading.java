package com.robining.android.rxjava2.loading;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RefreshLayoutLoading<T> implements ObservableTransformer<T, T> {
    private IRefreshLayout refreshLayout;
    private boolean isRefresh = true;
    private InfoProvider<T> infoProvider;

    private T data;
    private boolean isRunOnNext = false;
    private Throwable exception;

    private RefreshLayoutLoading(IRefreshLayout refreshLayout, boolean isRefresh, InfoProvider<T> infoProvider) {
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
                            refreshLayout.onStartRefresh();
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
                                refreshLayout.refreshCompleted();
                            } else {
                                refreshLayout.loadMoreCompleted();
                            }
                            //判断是否加载完数据
                            if (infoProvider.isLoadedAll(data)) {
                                refreshLayout.loadedAll();
                            }
                        } else { //失败
                            Throwable ex = exception == null ? new UnKnowException() : exception;
                            if (isRefresh) {
                                refreshLayout.refreshFailed(ex);
                            } else {
                                refreshLayout.loadMoreFailed(ex);
                            }
                        }
                    }
                });
    }

    public static <T> RefreshLayoutLoading<T> create(IRefreshLayout refreshLayout, boolean isRefresh, InfoProvider<T> infoProvider) {
        return new RefreshLayoutLoading<>(refreshLayout, isRefresh, infoProvider);
    }

    public interface InfoProvider<T> {
        boolean isLoadedAll(T data);
    }
}
