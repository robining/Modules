package com.robining.android.rxjava2.loading;

import android.app.Dialog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class DialogLoading<T> implements ObservableTransformer<T, T> {
    private Dialog dialog;

    private DialogLoading(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                });
    }

    public static <T> DialogLoading<T> create(Dialog dialog) {
        return new DialogLoading<>(dialog);
    }
}
