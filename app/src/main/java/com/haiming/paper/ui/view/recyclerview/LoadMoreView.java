package com.haiming.paper.ui.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiming.paper.R;


/**
 * 展示列表加载更多状态的View
 */
public class LoadMoreView extends RelativeLayout {

    //状态列举
    public static final int STATUS_HIDE = 0x01;
    public static final int STATUS_LOADING = 0x02;
    public static final int STATUS_END = 0x03;

    private View mProgressBar;
    private TextView mTipTv;

    private int mViewStatus;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.recycler_load_more_view, this);
        mProgressBar = findViewById(R.id.progress_bar);
        mTipTv = findViewById(R.id.tip_tv);
        setOnClickListener(null);

        showViewByStatus(STATUS_HIDE);//初始为隐藏状态
    }

    public void showViewByStatus(int viewStatus) {
        if (mViewStatus == viewStatus) {
            return;
        }
        mViewStatus = viewStatus;

        if (viewStatus == STATUS_HIDE) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);

        if (null != mProgressBar) {
            mProgressBar.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
        }
        if (null != mTipTv) {
            mTipTv.setText(viewStatus == STATUS_LOADING ? R.string.load_more_loading : R.string.common_load_more_end);
        }
    }

}
