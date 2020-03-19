package com.haiming.paper.ui.view;

import java.lang.reflect.Field;

import androidx.viewpager.widget.ViewPager;

/**
 * ViewPager工具类，为了避免ViewPager跨Tab切换时候闪动
 * Created by Xuefu_Du on 2017/6/12.
 */
public class ViewPageHelper {

    private ViewPager mViewPager;
    private FixedSpeedScroller mScroller;

    public ViewPageHelper(ViewPager viewPager) {
        this.mViewPager = viewPager;
        init();
    }

    private void init() {
        mScroller = new FixedSpeedScroller(mViewPager.getContext());
        Class<ViewPager> cl = ViewPager.class;
        try {
            Field field = cl.getDeclaredField("mScroller");
            field.setAccessible(true);
            //利用反射设置mScroller域为自己定义的MScroller
            field.set(mViewPager, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentItem(int item, boolean somoth) {
        int current = mViewPager.getCurrentItem();
        //如果页面相隔大于1,就设置页面切换的动画的时间为0
        if (Math.abs(current - item) > 1) {
            mScroller.setNoDuration(true);
            mViewPager.setCurrentItem(item, somoth);
            mScroller.setNoDuration(false);
        } else {
            mScroller.setNoDuration(false);
            mViewPager.setCurrentItem(item, somoth);
        }
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public FixedSpeedScroller getScroller() {
        return mScroller;
    }
}

