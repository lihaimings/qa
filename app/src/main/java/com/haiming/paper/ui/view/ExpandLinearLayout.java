package com.haiming.paper.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Xuefu_Du on 2018/3/9.
 */

public class ExpandLinearLayout extends LinearLayout {
    public ExpandLinearLayout(Context context) {
        super(context);
    }

    public ExpandLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(expandSpec, heightMeasureSpec);
    }
}
