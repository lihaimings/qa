package com.haiming.paper.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

public class ImageIndicator extends View implements IPagerIndicator {
    private RectF rectF = new RectF();

    private Bitmap indicatorBitmap;
    private Paint mPaint;
    private List<PositionData> mPositionDataList;

    private float ratio;//图片宽高比

    // 控制动画
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private Interpolator mEndInterpolator = new LinearInterpolator();

    public ImageIndicator(Context context) {
        super(context);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (indicatorBitmap!=null)
            canvas.drawBitmap(indicatorBitmap,null,rectF,mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        float leftX;
        float nextLeftX;
        float rightX;
        float nextRightX;

        leftX = current.mLeft;
        nextLeftX = next.mLeft;
        rightX = current.mRight;
        nextRightX = next.mRight;

        rectF.left = leftX + (nextLeftX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        rectF.right = rightX + (nextRightX - rightX) * mEndInterpolator.getInterpolation(positionOffset);
        rectF.top = getHeight() - (rectF.right-rectF.left) /ratio;
        rectF.bottom = getHeight() ;

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public void setIndicatorImage(Bitmap indicatorBitmap, float ratio) {
        this.ratio = ratio;
        this.indicatorBitmap = indicatorBitmap;
    }
}
