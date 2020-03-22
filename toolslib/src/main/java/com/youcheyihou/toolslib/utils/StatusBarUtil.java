package com.youcheyihou.toolslib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.youcheyihou.toolslib.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏处理工具类；透明状态栏+不考虑有ActionBar
 * 针对Android4.4（含）以上才设置透明状态栏，其他使用默认状态栏
 * 参考：[https://github.com/yazhi1992/Practice]
 * Created by Xuefu_Du on 2018/1/30.
 */
public class StatusBarUtil {

    private Activity mActivity;
    //状态栏颜色;不要使用数字标识未设置状态，如-1其实就是白色
    private Integer mColor;

    private StatusBarUtil(Activity activity) {
        mActivity = activity;
    }

    public static StatusBarUtil with(Activity activity) {
        return new StatusBarUtil(activity);
    }

    public StatusBarUtil setColor(int color) {
        mColor = color;
        return this;
    }

    public void init() {
        fullScreen(mActivity);
        //设置状态栏颜色
        if (mColor != null) {
            addStatusViewWithColor(mActivity, mColor);
        }
    }

    /**
     * 添加状态栏占位视图，可设置颜色，也可设置drawable，例如渐变色(查看git历史)；
     * Android4.4以上系统版本可以修改状态栏颜色
     *
     * @param color 状态栏(背景)颜色
     */
    private void addStatusViewWithColor(Activity activity, int color) {
        //api19-Android4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        //设置 paddingTop
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        //增加占位状态栏
        View statusBarView = new View(activity);
        statusBarView.setId(R.id.custom_status_bar_view);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(color);
        decorView.addView(statusBarView, lp);
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    private void fullScreen(Activity activity) {
        //api21-Android5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api19-Android4.4
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            window.setAttributes(attributes);
        }
    }

    /**
     * @param visible 控制自定义的状态栏的显隐
     */
    public void setCustomStatusViewVisible(int visible) {
        //api19-Android4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        View decorView = mActivity.getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        if (rootView != null) {
            int paddingTop = visible == View.VISIBLE ? getStatusBarHeight(mActivity) : 0;
            rootView.setPadding(0, paddingTop, 0, 0);
        }
        View statusBarView = decorView.findViewById(R.id.custom_status_bar_view);
        if (statusBarView != null) {
            statusBarView.setVisibility(visible);
        }
    }

    /**
     * @param color        更新状态栏(背景)的颜色
     * @param forceVisible 强制显示状态栏
     */
    private void updateStatusViewWithColor(int color, boolean forceVisible) {
        //api19-Android4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        View decorView = mActivity.getWindow().getDecorView();
        View statusBarView = decorView.findViewById(R.id.custom_status_bar_view);
        if (forceVisible && statusBarView != null) {
            statusBarView.setVisibility(View.VISIBLE);
        } else if (statusBarView == null || statusBarView.getVisibility() != View.VISIBLE) {
            return;
        }
        statusBarView.setBackgroundColor(color);
    }

    /**
     * 其实很多国内大厂Android系统都有深色状态栏字体模式，但是目前只看到了小米和魅族公开了各自的实现方法。
     * 而Android官方在6.0版本才有了深色状态栏字体API。
     *
     * @param dark true:把状态栏文字和图标改成深色模式(想改成其他自定义颜色暂时没有办法)
     */
    public void setStatusBarFontIconDark(boolean dark) {
        //api19-Android4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        // 小米MIUI
        boolean miuiFlag = miuiStatusBarFontIconDark(dark);

        // 魅族FlymeUI
        boolean flymeFlag = flymeStatusBarFontIconDark(dark);

        //api23-android6.0
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (dark) {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            } else if (!miuiFlag && !flymeFlag) {
                //无法设置状态栏上的字体颜色
                updateStatusViewWithColor(Color.parseColor("#99000000"), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param dark true:把状态栏文字和图标改成深色模式(小米MiUI)
     * @return 是否真正处理了字体颜色
     */
    private boolean miuiStatusBarFontIconDark(boolean dark) {
        // 小米MIUI
        if (!OsUtil.isMIUI()) {
            return false;
        }
        boolean flag = true;
        try {
            Window window = mActivity.getWindow();
            Class clazz = window.getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method mExtraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                mExtraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                mExtraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * @param dark true:把状态栏文字和图标改成深色模式(魅族FlymeUI)
     * @return 是否真正处理了字体颜色
     */
    private boolean flymeStatusBarFontIconDark(boolean dark) {
        // 魅族FlymeUI
        if (!OsUtil.isFlyme()) {
            return false;
        }
        boolean flag = true;
        try {
            Window window = mActivity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    //----工具方法----Start

    /**
     * @return 利用反射获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getStatusBarHeightIfCanTranslucentMode(Context context) {
        if (isCanTranslucentStatusBar()) {
            int barHeight = getStatusBarHeight(context);
            if (barHeight <= 0) {
                barHeight = context.getResources().getDimensionPixelSize(R.dimen.state_bar_def_height);//默认高
            }
            return barHeight;
        }
        return 0;
    }

    /**
     * @return 是否可以沉浸状态栏
     */
    private static boolean isCanTranslucentStatusBar() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    //----工具方法----End

}