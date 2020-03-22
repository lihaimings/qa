package com.youcheyihou.toolslib.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字处理
 * Created by Xuefu_Du on 2017/12/28.
 */
public class NumberUtil {

    /**
     * 将每三个数字加上逗号处理;
     *
     * @param str 无逗号的数字
     * @return 加上逗号的数字
     */
    public static String formatNumberWithDot(String str) {
        // 将传进数字反转
        String reverseStr = new StringBuilder(str).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 将 【789,456,】 中最后一个【,】去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
        // 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        return resultStr;
    }

    /**
     * @return String -> int
     */
    public static int parseInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int retNum = 0;
        try {
            retNum = Integer.parseInt(str);
        } catch (Exception e) {
            retNum = 0;
        }
        return retNum;
    }

    /**
     * @return String -> double
     */
    public static double parseDouble(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0d;
        }
        double retNum = 0d;
        try {
            retNum = Double.parseDouble(str);
        } catch (Exception e) {
            retNum = 0d;
        }
        return retNum;
    }

    /**
     * @return String -> int
     */
    public static long parseLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        long retNum = 0;
        try {
            retNum = Long.parseLong(str);
        } catch (Exception e) {
            retNum = 0L;
        }
        return retNum;
    }

    /**
     * PS:如果返回的String再转换成double，如4会变成4.0
     *
     * @return 确保小数点后有1位小数；不足补0；两位以上（发现：<=5舍>入）
     */
    public static String get1DecimalStr(double d) {
        DecimalFormat df = new DecimalFormat("0.0");
        String retStr = df.format(d);
        if (TextUtils.isEmpty(retStr)) {
            return "0.0";
        }
        return retStr;
    }

    /**
     * PS:如果返回的String再转换成double，如4.40会变成4.4
     *
     * @return 确保小数点后有两位小数；不足补0；两位以上（发现：<=5舍>入）
     */
    public static String get2DecimalStr(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        String retStr = df.format(d);
        if (TextUtils.isEmpty(retStr)) {
            return "0.00";
        }
        return retStr;
    }

    /**
     * 写法：DecimalFormat df = new DecimalFormat("#.##")，则保留小数点后面不为0的两位小数，
     * 这种写法不能保证保留2为小数，但能保证最后一位数不为0；
     *
     * @return 保留小数点后四位（发现：<=5舍>入）
     */
    private static double keepMax4Decimal(double d) {
        DecimalFormat df = new DecimalFormat("#.####");
        return Double.valueOf(df.format(d));
    }

    public static int keepMax4DecimalThenCeil(double d) {
        double src = keepMax4Decimal(d);
        return (int) Math.ceil(src);
    }

    public static int multiplyTenThousand(double srcNumber) {
        return (int) (srcNumber * 10000d);
    }

    /**
     * @return 从str中提取出数字
     */
    public static int pickUpNumber(@NonNull String str) {
        //匹配出所有非数字
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(str);
        String numberStr = m.replaceAll("").trim();
        return parseInt(numberStr);
    }

}