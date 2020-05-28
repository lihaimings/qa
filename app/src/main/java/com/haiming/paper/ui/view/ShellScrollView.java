package com.haiming.paper.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 不拦截事件
 */
public class ShellScrollView extends ScrollView {
    public ShellScrollView(Context context) {
        super(context);
    }

    public ShellScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShellScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
