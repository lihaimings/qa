package com.youcheyihou.toolslib.utils;

import android.util.Log;

import com.youcheyihou.toolslib.config.Constants;

/**
 * Created by xuefu_du on 2018/11/22.
 */
public class LogUtil {

    public static void e(String tag, String msg) {
        if (Constants.PRINT_LOG) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (Constants.PRINT_LOG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (Constants.PRINT_LOG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Constants.PRINT_LOG) {
            Log.w(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (Constants.PRINT_LOG) {
            Log.v(tag, msg);
        }
    }

    public static void println(String tag, String msg) {
        if (Constants.PRINT_LOG) {
            System.out.println(tag + "--" + msg);
        }
    }

}