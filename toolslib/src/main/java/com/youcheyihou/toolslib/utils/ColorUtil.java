package com.youcheyihou.toolslib.utils;

import android.graphics.Color;

/**
 * Created by Xuefu_Du on 2018/8/30.
 */
public class ColorUtil {

    /**
     * @return 异常时返回透明色
     */
    public static int parseColor(String colorString) {
        if (colorString == null || colorString.isEmpty()) {
            return 0;
        }
        try {
            return Color.parseColor(colorString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
