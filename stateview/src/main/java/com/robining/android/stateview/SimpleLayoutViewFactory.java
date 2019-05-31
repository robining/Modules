package com.robining.android.stateview;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Map;

public class SimpleLayoutViewFactory extends StateViewAssist.ViewFactory {
    private @LayoutRes
    int layout;

    public SimpleLayoutViewFactory(@LayoutRes int layout) {
        this.layout = layout;
    }

    @Override
    public View provideView(View target, StateViewAssist stateViewAssist, Map<String, Object> args, Map<String, Object> defaultArgs) {
        return LayoutInflater.from(target.getContext()).inflate(layout, null);
    }
}
