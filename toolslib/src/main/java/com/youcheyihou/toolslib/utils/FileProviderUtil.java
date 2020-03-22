package com.youcheyihou.toolslib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * @author ppjun
 * @date 2018/10/08.
 * FileProvider 工具类
 */
public class FileProviderUtil {
    /**
     * 防止反射
     */
    private FileProviderUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 系统24及以上用FileProvider，24以下用Uri.formFile
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (isAboveNougat()) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * FileProvider 获取uri
     * @param context
     * @param file
     * @return
     */
    private static Uri getUriForFile24(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context
                , "com.youcheyihou.iyoursuv.fileprovider", file);
        return fileUri;
    }

    /** 构造 Intent 的 setDataAndType 方法
     *
     * @param context
     * @param intent
     * @param type
     * @param file
     * @param writeable
     */
    public static void setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writeable) {

        if (isAboveNougat()) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeable) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    /** 构造 Intent 的 setData 方法
     *
     * @param context
     * @param intent
     * @param type
     * @param file
     * @param writeable
     */
    public static void setIntentData(Context context, Intent intent, String type, File file, boolean writeable) {
        if (isAboveNougat()) {
            intent.setData(getUriForFile24(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeable) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setData(Uri.fromFile(file));
        }
    }

    /**
     * 在 android7 以下版本直接使用 fileprovider 会提示 Permission Denial，要请求权限 grantUriPermission
     * android7 也会遇到 Permission Denial，没有添加 write 和 read 的权限
     *
     * @param context
     * @param intent
     * @param uri
     * @param writeAble
     */
    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeAble) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }

    /**
     * 判断 sdk版本 24及以上
     * @return
     */
    public static boolean isAboveNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
