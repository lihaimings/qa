package com.haiming.paper.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.haiming.paper.application.BaseApplication;

import java.lang.reflect.Method;

import androidx.core.content.ContextCompat;

/**
 * Created Time: 2016/11/21 20:50
 *
 * @author lee
 */

public class UIUtil {

    public static Context getContext() {
        return BaseApplication.getContext();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static Integer getInteger(int resId){
        return getResources().getInteger(resId);
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static int[] getIntrArray(int resId){
        return getResources().getIntArray(resId);
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static int getColor(int resId) {
        return ContextCompat.getColor(getContext(),resId);
    }

    public static Drawable getDrawable(int resId){
        return ContextCompat.getDrawable(getContext(),resId);
    }

    public static Handler getMainHandler() {
        return BaseApplication.getmMainHandler();
    }

    public static long getMainThreadId() {
        return BaseApplication.getmMainThreadId();
    }

    public static String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    public static String getDeviceId(){
        return Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * 让任务在主线程中执行
     *
     * @param task
     */
    public static void post(Runnable task) {
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            //在主线程中执行
            task.run();
        } else {
            //在子线程中执行
            getMainHandler().post(task);
        }
    }

    /**
     * dip转px
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        //公式： dp = px / (dpi / 160)		px = dp * (dpi / 160)
        //dp = px / denisity
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dip * density + 0.5f);
    }

    /**
     * px转dip
     *
     * @param px
     * @return
     */
    public static int px2dip(int px) {
        //dp = px / denisity
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (px / density + 0.5f);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static float px2sp( float pxValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale);
    }


    /**
     * 执行延迟的任务
     *
     * @param task
     * @param delayedTime
     */
    public static void postDelayed(Runnable task, int delayedTime) {
        getMainHandler().postDelayed(task, delayedTime);
    }

    /**
     * 移除执行的任务
     *
     * @param task
     */
    public static void removeCallBacks(Runnable task) {
        getMainHandler().removeCallbacks(task);
    }

    /**
     * 获取屏幕的相关参数
     *
     * @return
     */
    public static DisplayMetrics getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕density
     *
     * @return
     */
    public static float getDeviceDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * 获取状态栏高度
     */

    public static int getStateBarHeight(){
        int statusBarHeight1 = 0;
//获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
       return statusBarHeight1;
    }


    /**
     * 获取底部导航栏高度
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

//       //判断是否存在NavigationBar
//    public static boolean checkDeviceHasNavigationBar(Context context) {
//        boolean hasNavigationBar = false;
//        Resources rs = context.getResources();
//        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
//        if (id > 0) {
//            hasNavigationBar = rs.getBoolean(id);
//        }
//        try {
//            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
//            Method m = systemPropertiesClass.getMethod("get", String.class);
//            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
//            if ("1".equals(navBarOverride)) {
//                hasNavigationBar = false;
//            } else if ("0".equals(navBarOverride)) {
//                hasNavigationBar = true;
//            }
//        } catch (Exception e) {
//
//        }
//        return hasNavigationBar;
//
//    }


}
