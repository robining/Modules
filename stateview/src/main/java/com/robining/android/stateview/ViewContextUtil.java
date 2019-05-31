package com.robining.android.stateview;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

class ViewContextUtil {
    public static Activity getActivity(View view) {
        if (null != view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof Activity) {
                    return (Activity) context;
                }
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }
}
