package com.haiming.paper.ui.view.window;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.haiming.paper.Utils.CommonUtil;
import com.haiming.paper.Utils.UIUtil;


public class CommonPopupWindow extends PopupWindow {

    final PopupController controller;
    private int[] location = new int[2];
    private int minWidowHeight = -1;
    private boolean isYOffAdjust = false;



    @Override
    public int getWidth() {
        return controller.mPopupView.getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return controller.mPopupView.getMeasuredHeight();
    }

    public int getMinWidowHeight() {
        return minWidowHeight;
    }

    public void setMinWidowHeight(int minWidowHeight) {
        this.minWidowHeight = minWidowHeight;
    }

    public interface ViewInterface {
        void getChildView(View view, int layoutResId, CommonPopupWindow popupWindow);
    }

    public interface ViewInit{
        void getViewInit(View view);
    }

    private CommonPopupWindow(Context context) {
        controller = new PopupController(context, this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        controller.setBackGroundLevel(1.0f);
        isYOffAdjust = false;
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        int adjustYoff = yoff;
        if(!isYOffAdjust) {
            adjustYoff = adjustYoff(anchor, yoff);
            isYOffAdjust = true;
        }

        super.showAsDropDown(anchor, xoff, adjustYoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        int adjustYoff = yoff;
        if(!isYOffAdjust) {
            adjustYoff = adjustYoff(anchor, yoff);
            isYOffAdjust = true;
        }
        super.showAsDropDown(anchor, xoff, adjustYoff, gravity);
    }

    private int adjustYoff(View anchor, int yoff) {
        if (anchor == null) {
            return 0;
        }
        anchor.getLocationInWindow(location);
        int availableHeight = UIUtil.getScreenSize().heightPixels -location[1] - anchor.getHeight() - yoff;

        if (availableHeight < controller.mPopupView.getMeasuredHeight()) {
            yoff = -1*yoff - anchor.getHeight() - controller.mPopupView.getMeasuredHeight();
            //yoff -=
        }
        return yoff;
    }

    public static class Builder {
        private final PopupController.PopupParams params;
        private ViewInterface listener;
        private ViewInit viewInit;

        public Builder(Context context) {
            params = new PopupController.PopupParams(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            params.mView = null;
            params.layoutResId = layoutResId;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view) {
            params.mView = view;
            params.layoutResId = 0;
            return this;
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        public Builder setViewOnclickListener(ViewInterface listener) {
            this.listener = listener;
            return this;
        }

        public Builder setInitView(ViewInit viewInit){
            this.viewInit=viewInit;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            params.isShowBg = true;
            params.bg_level = level;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }


        public CommonPopupWindow create() {
            final CommonPopupWindow popupWindow = new CommonPopupWindow(params.mContext);
            params.apply(popupWindow.controller);
            if (listener != null && params.layoutResId != 0) {
                listener.getChildView(popupWindow.controller.mPopupView, params.layoutResId, popupWindow);
            }
            if(viewInit!=null&& params.layoutResId != 0){
                viewInit.getViewInit(popupWindow.controller.mPopupView);
            }
            CommonUtil.measureWidthAndHeight(popupWindow.controller.mPopupView);
            return popupWindow;
        }
    }
}

