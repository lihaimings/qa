package com.youcheyihou.toolslib.utils;

import android.text.TextUtils;

/**
 * 金额相关处理类
 * Created by Xuefu_Du on 2017/12/28.
 */
public class MoneyUtil {

    /**
     * 去除字串中的逗号和金钱符号-空串返回0
     *
     * @param str 可能存在逗号的字串
     * @return 数字
     */
    public static int transToNumFormat(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        str = str.replace("¥", "");
        str = str.replace(",", "");
        int retNum = 0;
        try {
            retNum = Integer.parseInt(str);
        } catch (Exception e) {
            retNum = 0;
        }
        return retNum;
    }
}