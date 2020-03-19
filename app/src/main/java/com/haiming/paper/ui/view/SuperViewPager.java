package com.haiming.paper.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

/**
 * 避免ViewPager跨Tab切换时候闪动的ViewPager
 * Created by Xuefu_Du on 2017/6/12.
 */
public class SuperViewPager extends ViewPager {

    private ViewPageHelper helper;

    public SuperViewPager(Context context) {
        this(context, null);
    }

    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        helper = new ViewPageHelper(this);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        FixedSpeedScroller scroller = helper.getScroller();
        if (Math.abs(getCurrentItem() - item) > 1) {
            scroller.setNoDuration(true);
            super.setCurrentItem(item, smoothScroll);
            scroller.setNoDuration(false);
        } else {
            scroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }
}
