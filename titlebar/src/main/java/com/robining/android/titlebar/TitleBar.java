package com.robining.android.titlebar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by Administrator on 2017\11\15 0015.
 */

public class TitleBar extends Toolbar implements ITitleBar {
    private ViewGroup leftContainer, rightContainer;
    private TextView tvTitle;
    private View btnBack, bottomLine;

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setContentInsetsAbsolute(0, 0);
        setContentInsetsRelative(0, 0);
        setContentInsetEndWithActions(0);
        setContentInsetStartWithNavigation(0);
        inflate(getContext(), R.layout.uiimpl_layout_toolbar_content, this);
        leftContainer = findViewById(R.id.tv_titlebar_left_container);
        rightContainer = findViewById(R.id.tv_titlebar_right_container);
        btnBack = findViewById(R.id.btn_titlebar_back);
        tvTitle = findViewById(R.id.tv_titlebar_title);
        bottomLine = findViewById(R.id.tv_titlebar_line);
        setBackButtonEnable(true);
    }

    @Override
    public void setTitleTextColor(int color) {
        if (tvTitle != null) {
            tvTitle.setTextColor(color);
        } else {
            super.setTitleTextColor(color);
        }
    }

    @Override
    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }

    @Override
    public TextView getTitleView() {
        return tvTitle;
    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    @Override
    public void addRightButton(View view) {
        rightContainer.addView(view);
    }

    @Override
    public void setBackButtonEnable(boolean enable) {
        btnBack.setVisibility(enable ? VISIBLE : GONE);
    }

    @Override
    public void setBackButtonClickListener(OnClickListener clickListener) {
        btnBack.setOnClickListener(clickListener);
    }

    @Override
    public void addLeftButton(View view) {
        leftContainer.addView(view);
    }

    @Override
    public void setVisible(int visible) {
        setVisibility(visible);
    }

    @Override
    public Toolbar getToolbar() {
        return this;
    }

    @Override
    public void showBottomLine(boolean isShow) {
        bottomLine.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public View getView() {
        return this;
    }
}
