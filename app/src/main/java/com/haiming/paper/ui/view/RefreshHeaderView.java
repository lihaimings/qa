package com.haiming.paper.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;

import pl.droidsonroids.gif.GifImageView;

public class RefreshHeaderView extends SwipeRefreshHeaderLayout {

    private RelativeLayout rlRefreshHeader;

    private TextView tvRefreshing;

    private ImageView ivRefreshSuc;

    private GifImageView gifvLoader;

    private int mHeaderHeight;

    private String completeHint = "";

    private boolean rotated = false;

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        rlRefreshHeader = findViewById(R.id.rl_refresh_header);
        tvRefreshing = findViewById(R.id.tv_refresh);
        ivRefreshSuc = findViewById(R.id.iv_refresh_success);
        gifvLoader = findViewById(R.id.gifv_loader);

        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.x54);
    }

    @Override
    public void onRefresh() {
        ivRefreshSuc.setVisibility(View.INVISIBLE);
        gifvLoader.setVisibility(View.VISIBLE);
        tvRefreshing.setText(R.string.refresh_refreshing);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            gifvLoader.setVisibility(View.INVISIBLE);
            ivRefreshSuc.setVisibility(View.INVISIBLE);
            if (y > mHeaderHeight) {
                tvRefreshing.setText(R.string.refresh_release_to_refresh);
                if (!rotated) {
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    rotated = false;
                }
                tvRefreshing.setText(R.string.refresh_swipe_to_refresh);
            }
        }
    }

    @Override
    public void onRelease() {
        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        rotated = false;
        ivRefreshSuc.setVisibility(View.VISIBLE);
        gifvLoader.setVisibility(View.INVISIBLE);
        tvRefreshing.setText(completeHint.length()==0? UIUtil.getString(R.string.refresh_complete):completeHint);
    }

    @Override
    public void onReset() {
        rotated = false;
        gifvLoader.setVisibility(View.INVISIBLE);
    }

    public void setCompleteHint(String completeHint){
        this.completeHint = completeHint;
    }

}
