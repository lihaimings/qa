package com.haiming.paper.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以锁定的（不能滑动）ViewPager
 */
public class ViewPagerFixed extends SuperViewPager {

    private boolean mIsCanSlide = true;

    public ViewPagerFixed(Context context) {
        super(context);
    }

    public ViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isCanSlide() {
        return mIsCanSlide;
    }

    public void setIsCanSlide(boolean isCanSlide) {
        this.mIsCanSlide = isCanSlide;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (!mIsCanSlide) {
                return false;
            }
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (!mIsCanSlide) {
                return false;
            }
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}