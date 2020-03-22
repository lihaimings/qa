package com.youcheyihou.toolslib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 *  
 * Copyright (c) 有车以后
 *
 * @author xuefu_du
 * @description 描述该文件做什么
 * @date 2019-12-10 21:48  
 */
public class DimenUtil {

    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;
    @NonNull
    private volatile static Point[] sRealSizes = new Point[2];

    private volatile static boolean sHasCheckAllScreen;
    private volatile static boolean sIsAllScreenDevice;

    private static int sScreenW;
    private static int sScreenH;
    private static float sScreenDensity;

    public static int getScreenW(Context context) {
        if (sScreenW == 0) {
            initScreen(context);
        }
        return sScreenW;
    }

    public static int getScreenH(Context context) {
        if (sScreenH == 0) {
            initScreen(context);
        }
        return sScreenH;
    }

    public static float getScreenDensity(Context context) {
        if (sScreenDensity == 0) {
            initScreen(context);
        }
        return sScreenDensity;
    }

    private static void initScreen(Context context) {
        try {
            DisplayMetrics metric = context.getResources().getDisplayMetrics();
            sScreenW = metric.widthPixels;
            sScreenH = metric.heightPixels;
            sScreenDensity = metric.density;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------全面屏--------Start

    /**
     * 获取屏幕高度，兼容全面屏；
     */
    public static int getHeightCombineAllScreen(@NonNull Context context) {
        if (!isAllScreenDevice(context)) {
            return getScreenH(context);
        }
        return getScreenRealHeight(context);
    }

    private static int getScreenRealHeight(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getScreenH(context);
        }

        int orientation = context.getResources().getConfiguration().orientation;
        orientation = orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;

        if (sRealSizes[orientation] == null) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager == null) {
                return getScreenH(context);
            }
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            sRealSizes[orientation] = point;
        }
        return sRealSizes[orientation].y;
    }

    /**
     * @return 是否为全面屏
     */
    private static boolean isAllScreenDevice(@NonNull Context context) {
        if (sHasCheckAllScreen) {
            return sIsAllScreenDevice;
        }
        sHasCheckAllScreen = true;
        sIsAllScreenDevice = false;
        // 认为：低于 API 21的，都不会是全面屏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            float w, h;
            if (point.x < point.y) {
                w = point.x;
                h = point.y;
            } else {
                w = point.y;
                h = point.x;
            }
            if (h / w >= 1.97f) {
                sIsAllScreenDevice = true;
            }
        }
        return sIsAllScreenDevice;
    }

    //--------全面屏--------End

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * getScreenDensity(context) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context) + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * location [0]--->x坐标,location [1]--->y坐标
     */
    public static int[] getAbsXyInScreen(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        return location;
    }

}