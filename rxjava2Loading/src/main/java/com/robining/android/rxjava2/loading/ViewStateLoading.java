package com.robining.android.rxjava2.loading;

import com.robining.android.stateview.StateViewAssist;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ViewStateLoading<T> implements ObservableTransformer<T, T> {
    private StateViewAssist stateViewAssist;
    private InfoProvider infoProvider;

    private boolean isRunOnNext = false;
    private Throwable exception;

    private ViewStateLoading(StateViewAssist stateViewAssist, InfoProvider infoProvider) {
        this.stateViewAssist = stateViewAssist;
        if (stateViewAssist.getTarget() == null) {
            throw new IllegalArgumentException("the stateViewAssist default target cannot be null");
        }
        this.infoProvider = infoProvider;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        stateViewAssist.show(StateViewAssist.STATE_LOADING, null);
                    }
                })
                .doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
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
                            if (stateViewAssist.getCurrentState() == StateViewAssist.STATE_LOADING) {
                                infoProvider.onSwitchErrorLayout(ex, stateViewAssist);
                            }
                        }
                    }
                });
    }

    public static <T> ViewStateLoading<T> create(StateViewAssist stateViewAssist, InfoProvider infoProvider) {
        return new ViewStateLoading<>(stateViewAssist, infoProvider);
    }

    public interface InfoProvider {
        boolean pageIsHaveData();
        void onSwitchErrorLayout(Throwable ex, StateViewAssist stateViewAssist);
    }
}
