package com.robining.android.titlebar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017\11\15 0015.
 * 必须有三个子控件,中间的控件会永远保持居中
 */

public class TitleBarContainerView extends FrameLayout {
    public TitleBarContainerView(Context context) {
        super(context);
        init();
    }

    public TitleBarContainerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBarContainerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int firstWidth = getChildAt(0).getMeasuredWidth();
        int lastWidth = getChildAt(2).getMeasuredWidth();

        int middleWidth = getMeasuredWidth() - 2 * Math.max(firstWidth, lastWidth);
        getChildAt(1).measure(MeasureSpec.makeMeasureSpec(middleWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
    }
}
