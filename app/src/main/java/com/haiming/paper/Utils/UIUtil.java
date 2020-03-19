package com.haiming.paper.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.haiming.paper.application.BaseApplication;

import java.lang.reflect.Method;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * Created Time: 2016/11/21 20:50
 *
 * @author lee
 */

public class UIUtil {

    private static int sScreenW;
    private static int sScreenH;
    private static float sScreenDensity;
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

    public static boolean isListBlank(List list) {
        return (list == null) || (list.size() <= 0);
    }

    public static void showToast(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
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
