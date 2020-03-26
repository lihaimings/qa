package com.haiming.paper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.ui.view.ImageIndicator;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import androidx.viewpager.widget.ViewPager;

public class QuestionAndAnswerNavigatorAdapter extends CommonNavigatorAdapter {

    protected String[] mTitles;
    protected ViewPager viewPager;
    private Context mContext;

    public QuestionAndAnswerNavigatorAdapter(Context context, String[] mTitles, ViewPager viewPager) {
        this.mTitles = mTitles;
        this.viewPager = viewPager;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        SimplePagerTitleView titleView = new SimplePagerTitleView(context);
        titleView.setNormalColor(UIUtil.getColor(mContext, R.color.color_e8e8e8));
        titleView.setSelectedColor(UIUtil.getColor(mContext, R.color.color_f7f7f7));
        titleView.setText(mTitles[index]);
        titleView.setPadding(0, UIUtil.getResources().getDimensionPixelOffset(R.dimen.x17), 0, 0);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, UIUtil.getResources().getDimensionPixelSize(R.dimen.x12));
        titleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
        return titleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        ImageIndicator imageIndicator = new ImageIndicator(context);
        Bitmap bitmap = BitmapFactory.decodeResource(UIUtil.getResources(), R.drawable.select_q_and_a_tab);
        imageIndicator.setIndicatorImage(bitmap, 3);
        return imageIndicator;
    }
}
