package com.youcheyihou.toolslib.utils;

import android.text.TextUtils;

/**
 * 图片地址路径的处理
 * 参考：https://developer.qiniu.com/dora/manual/3683/img-directions-for-use
 * 七牛缩略命令参考：https://developer.qiniu.com/dora/manual/1279/basic-processing-images-imageview2
 * Created by Xuefu_Du on 2018/2/2.
 */
public class PicPathUtil {

    //七牛样式
    public static final String STYLE_240x130 = "-240x130";
    public static final String STYLE_690x422 = "-690x422";
    public static final String STYLE_690x340 = "-690x340";
    public static final String STYLE_650x236 = "-650x236";
    public static final String STYLE_750x280 = "-750x280";
    public static final String STYLE_750x350 = "-750x350";
    public static final String STYLE_750x_w = "-750x_w";
    public static final String STYLE_690x = "-690x";
    public static final String STYLE_750x = "-750x";
    public static final String STYLE_400x = "-400x";
    public static final String STYLE_200x = "-200x";
    public static final String STYLE_1x1_100x100 = "-1x1_100x100";
    public static final String STYLE_1x1_200x200 = "-1x1_200x200";
    public static final String STYLE_1x1_300x300 = "-1x1_300x300";
    public static final String STYLE_1x1_400x400 = "-1x1_400x400";
    public static final String STYLE_3x4_360x480 = "-3x4_360x480";
    public static final String STYLE_4x3_200x150 = "-4x3_200x150";
    public static final String STYLE_4x3_226x170 = "-4x3_226x170";
    public static final String STYLE_4x3_400x300 = "-4x3_400x300";
    public static final String STYLE_4x3_750x562 = "-4x3_750x562";
    public static final String STYLE_16x9_200x112 = "-16x9_200x112";
    public static final String STYLE_16x9_320x180 = "-16x9_320x180";
    public static final String STYLE_16x9_500x282 = "-16x9_500x282";
    public static final String STYLE_16x9_750x422 = "-16x9_750x422";

    //自定义
//    private static final String SHARE_MINI_PROGRAM_THUMB = "?imageView2/0/w/250/h/200";//小程序封面缩略
    private static final String SHARE_MINI_PROGRAM_THUMB = "?imageslim";//小程序封面缩略

    /**
     * @param styleStr 七牛处理图片样式
     * @param canWebp  是否使用webp
     * @return 带有七牛图片处理样式的URL
     */
    public static String genStyleUrl(String url, String styleStr, boolean canWebp) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(styleStr)) {
//            LogUtil.d("d_slience", "genStyleUrl - isEmpty url:" + url);
            return url;
        }
        /*
         * 非"七牛"下的图片，直接返回原地址；
         */
        if (!url.contains("suv666") && !url.contains("qiniucdn")) {
            return url;
        }
        /*
         * 如果包含"v.suv666.co"即认为是视频->取的第一帧，则不再加指令返回；
         */
        if (url.contains("v.suv666.com")) {
            return url;
        }
        /*
         * 全局判断如果是gif图的话（图片链接后缀为gif），直接返回原地址；
         */
        if (url.endsWith("gif")) {
            return url;
        }
        /*
         * 是否使用xxxx_webp这种样式；
         */
        if (canWebp) {
            styleStr += "_webp";
        }
        if (!url.contains("?")) {
//            LogUtil.d("d_slience", "genStyleUrl - (url + styleStr):" + (url + styleStr));
            return (url + styleStr);
        }
        /*
         * String.split(String regex); 也就是说里面的参数是正则表达式。
         * ?是特殊字符，想让按照?来拆分，就用正则表达式表示某个特定字符的方式了； [?] 就表示问号
         */
        try {
            String[] urlArr = url.split("[?]");
//            LogUtil.d("d_slience", "genStyleUrl - (urlArr[0] + styleStr):" + (urlArr[0] + styleStr));
            return (urlArr[0] + styleStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @param styleStr 七牛处理图片样式
     * @return 带有七牛图片处理样式的URL
     */
    public static String genStyleUrl(String url, String styleStr) {
        return genStyleUrl(url, styleStr, true);
    }

    /**
     * @return (七牛样式处理过的)用户头像
     */
    public static String getThumbUserIconUrl(String url) {
        return genStyleUrl(url, STYLE_1x1_100x100);
    }

    /**
     * 通过后缀七牛指令处理图片，以达到缩略等效果；
     *
     * @param url        图片地址
     * @param commandStr 七牛命令
     * @return 带七牛命令的地址
     */
    private static String genUrlWithCommand(String url, String commandStr) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(commandStr)) {
            return url;
        }
        /*
         * 非"七牛"下的图片，直接返回原地址；
         */
        if (!url.contains("suv666") && !url.contains("qiniucdn")) {
            return url;
        }
        /*
         * 如果包含"v.suv666.co"即认为是视频->取的第一帧，则不再加指令返回；
         */
        if (url.contains("v.suv666.com")) {
            return url;
        }

        if (!url.contains("?")) {
            return (url + commandStr);
        }
        /*
         * String.split(String regex); 也就是说里面的参数是正则表达式。
         * ?是特殊字符，想让按照?来拆分，就用正则表达式表示某个特定字符的方式了； [?] 就表示问号
         */
        try {
            String[] urlArr = url.split("[?]");
            return (urlArr[0] + commandStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @return 分享小程序的封面缩略图
     */
    public static String getShareMiniProgramThumbUrl(String url) {
        return genUrlWithCommand(url, SHARE_MINI_PROGRAM_THUMB);
    }

}