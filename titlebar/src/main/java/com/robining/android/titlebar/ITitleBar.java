package com.robining.android.titlebar;

import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017\11\15 0015.
 */

public interface ITitleBar {
    TextView getTitleView();

    void setTitle(CharSequence title);

    void setTitle(@StringRes int title);

    void addRightButton(View view);

    void setBackButtonEnable(boolean enable);

    void setBackButtonClickListener(View.OnClickListener clickListener);

    void addLeftButton(View view);

    void setVisible(int visible);

    Toolbar getToolbar();

    void showBottomLine(boolean isShow);

    View getView();
}
