package com.robining.android.rxjava2.loading;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class FragmentDialogLoading<T> implements ObservableTransformer<T, T> {
    private DialogFragment dialog;
    private FragmentManager fragmentManager;
    private String tag;

    private FragmentDialogLoading(DialogFragment dialog, FragmentManager fragmentManager, String tag) {
        this.dialog = dialog;
        this.fragmentManager = fragmentManager;
        this.tag = tag;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show(fragmentManager, tag);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                });
    }

    public static <T> FragmentDialogLoading<T> create(DialogFragment dialog, FragmentManager fragmentManager, String tag) {
        return new FragmentDialogLoading<>(dialog, fragmentManager, tag);
    }
}
