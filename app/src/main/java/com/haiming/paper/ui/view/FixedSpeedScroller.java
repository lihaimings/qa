package com.haiming.paper.ui.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 类描述:为控制ViewPager翻页速度、节奏
 *
 * @author Xuefu_Du
 * @date [2016-1-18]
 */
public class FixedSpeedScroller extends Scroller {

    //默认使用的补间器
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    private int mDuration = 300;
    private boolean mNoDuration;//标识：界面滑动不需要时间间隔

    public FixedSpeedScroller(Context context) {
        this(context, sInterpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        if (mNoDuration) {
            //界面滑动不需要时间间隔
            super.startScroll(startX, startY, dx, dy, 0);
        } else {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    public void setNoDuration(boolean mNoDuration) {
        this.mNoDuration = mNoDuration;
    }

}

