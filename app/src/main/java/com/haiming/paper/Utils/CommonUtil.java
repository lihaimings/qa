package com.haiming.paper.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import top.zibin.luban.Luban;

/**
 * Created Time: 2016/11/22 19:56
 * Descripted:
 *
 * @author lee
 */

public class CommonUtil {

    /**
     * 判断应用是否处于后台
     * @param context
     * @return
     */
    public static boolean isAppOnBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否锁屏
     * @param context
     * @return
     */
    public static boolean isLockScreeen(Context context){
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (isScreenOn){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 分享文字笔记
     */
    public static void shareText(Context context, String content){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享单张图片
     * @param context
     * @param imagePath
     */
    public static void shareImage(Context context, String imagePath) {
        //String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        Uri imageUri = Uri.fromFile(new File(imagePath));//由文件得到uri
        Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 分享功能
     *
     * @param context
     *            上下文
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public static void shareTextAndImage(Context context, String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }

    /**
     * 获得屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static String date2string(Date date) {
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        strDate = sdf.format(date);
        return strDate;
    }


    /**
     * 判断包是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 安装应用程序
     *
     * @param context
     * @param apkFile
     */
    public static void installApp(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开应用程序
     *
     * @param context
     * @param packageName
     */
    public static void openApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 判断是否存在该app并获取该app的包名
     *
     * @param context
     * @param name
     * @return
     */
    public static boolean CheckInstalled(Context context, String name) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String NAME = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            if (NAME.equals(name)) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * 获取下载的所有应用
//     *
//     * @param context
//     * @return
//     */
//    public synchronized static Map<String, String> getAllInstallPackage(Context context) {
//        Map<String, String> map = new HashMap<>();
//
//        try{
//
//            for (int i = 0; i < packages.size(); i++) {
//                PackageInfo packageInfo = packages.get(i);
//                String name = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
//                String packageName = packageInfo.packageName;
//                map.put(name, packageName);
//            }
//            return map;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return map;
//    }

    //Android6.0读写权限解决方式
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        if (getSDKVersionNumber() >= 23) {
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }
        }
    }

    /**
     * @return 获取sdk版本号
     */
    public static int getSDKVersionNumber() {

        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK);

        } catch (NumberFormatException e) {

            sdkVersion = 0;
        }
        return sdkVersion;
    }

    public static boolean isWiFiActive(Context context) {
       WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
       if(mWifiManager!=null){
           WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
           int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
           if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
               System.out.println("**** WIFI is on");
               return true;

           } else {
               System.out.println("**** WIFI is off");
               return false;
           }
       }
       return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean checkInternet(Context context){
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

//    public static class CheckUpdateRunnable implements Runnable {
//
//        private UpdateVersionBean mUpdateVersionBean;
//        private Activity activity;
//        private boolean isShowHint;
//
//        public CheckUpdateRunnable(Activity activity,boolean isShowHint){
//            this.activity = activity;
//            this.isShowHint = isShowHint;
//        }
//
//        @Override
//        public void run() {
//            UpdateVersionProtocol mUpdateVersionProtocol = new UpdateVersionProtocol();
//            try {
//                mUpdateVersionBean = mUpdateVersionProtocol.loadData(0, false).getD();
//                if (mUpdateVersionBean != null) {
//                    //显示升级弹窗
//                    if(mUpdateVersionBean.getIs_new_version() == 2){
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showUpdateDialog(mUpdateVersionBean);
//                            }
//                        });
//                    } else if (mUpdateVersionBean.getIs_new_version() == 1 && isShowHint)
//                        ToastUtil.show(activity, UIUtil.getString(R.string.check_update_latest));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        /**
//         * 显示升级提醒的对话框
//         *
//         */
//        private void showUpdateDialog(final UpdateVersionBean data) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.win_style_dialog);
//            View view = View.inflate(activity,R.layout.update_version_dialog,null);
//            RecyclerView explainList = view.findViewById(R.id.explain_list);
//            Button btnUpdate = view.findViewById(R.id.btn_update);
//
//            explainList.setHasFixedSize(true);
//            explainList.setLayoutManager(new LinearLayoutManager(activity));
//            explainList.setAdapter(new UpdateVersionExplainListAdapter(data.getExplain()));
//
//            builder.setView(view);
//            final AlertDialog dialog = builder.create();
//
//            btnUpdate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    Uri content_url = Uri.parse(data.getDownload_url());
//                    intent.setData(content_url);
//                    activity.startActivity(intent);
//                }
//            });
//
//            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    if(data.getIs_force() == 2){
//                        System.exit(1);
//                    }
//                }
//            });
//            dialog.show();
//        }
//    }

    public static void addTextToClipboard(String clipText){
        ClipData clipData = ClipData.newPlainText("text",clipText);
        ((ClipboardManager) UIUtil.getContext().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(clipData);
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    public static void initInputMoneyEdt(EditText editText){
        initInputMoneyEdt(editText,2);
    }

    public static void initInputMoneyEdt(EditText editText,final int precision){
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                precisionLimit(edt,precision);
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
    }

    public static void precisionLimit(Editable edt,int precision){
        if(edt.toString().equals(".")){
            edt.insert(0,String.valueOf(0));
        }else{
            String temp = edt.toString();
            int posDot = temp.indexOf(".");
            if (posDot <= 0) return;
            if (temp.length() - posDot - 1 > precision)
            {
                edt.delete(posDot + precision+1, posDot + precision+2);
            }
        }
    }

    public static String setFractionNum(Double number,int f){
        NumberFormat format = NumberFormat.getNumberInstance() ;
        format.setMaximumFractionDigits(f);
        return format.format(number);
    }

    /**
     * 压缩图片
     * @param context
     * @param filePathList 原图集合
     * @return 压缩后的图集合
     */
    public static ArrayList<File> compressFiles(Context context,ArrayList<String> filePathList){
        ArrayList<File> fileList=new ArrayList<>();
        for (int i =0;i<filePathList.size();i++){
            File pic=new File(filePathList.get(i));
            File compressedImageFile = null;
            try {
                compressedImageFile = Luban.with(context).load(pic).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(compressedImageFile);
        }
        return fileList;
    }

    //文件类型后缀
    private static final String[][] MIME_MapTable = { { ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" }, { ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" }, { ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" }, { ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" }, { ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" }, { ".xls", "application/vnd.ms-excel" }, { ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" }, { ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" }, { ".jar", "application/java-archive" }, { ".java", "text/plain" }, { ".jpeg", "image/jpeg" }, { ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, { ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" }, { ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" }, { ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" }, { ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, { ".pdf", "application/pdf" }, { ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" }, { ".ppt", "application/vnd.ms-powerpoint" }, { ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" }, { ".prop", "text/plain" }, { ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" }, { ".sh", "text/plain" }, { ".tar", "application/x-tar" }, { ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" }, { ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" }, { ".z", "application/x-compress" }, { ".zip", "application/x-zip-compressed" }, { "", "*/*" } };

    /**
     * 打开文件
     * @param file
     */
    public static void openFile(File file, Activity activity){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, type);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static Uri getUriForFile(Context paramContext, File paramFile)
    {
        if (Build.VERSION.SDK_INT >= 24)
        {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(paramContext.getPackageName());
            localStringBuilder.append(".fileprovider");
            return FileProvider.getUriForFile(paramContext, localStringBuilder.toString(), paramFile);
        }
        return Uri.fromFile(paramFile);
    }

    /**
     * 获取文件类型后缀
     * @param paramFile
     * @return
     */
    private static String getMIMEType(File paramFile) {
        String str1 = "*/*";
        String str2 = paramFile.getName();
        int i = str2.lastIndexOf(".");
        if (i < 0)
            return str1;
        String str3 = str2.substring(i, str2.length()).toLowerCase();
        if (str3 == "")
            return str1;
        for (int j = 0; ; j++)
        {
            if (j >= MIME_MapTable.length)
                return str1;
            if (str3.equals(MIME_MapTable[j][0]))
                str1 = MIME_MapTable[j][1];
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager =
                (ActivityManager) UIUtil.getContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = UIUtil.getContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance
                    == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回正确的UserAgent
     * @return
     */
    public static String getUserAgent(){
        String userAgent = "";
        StringBuffer sb = new StringBuffer();
        userAgent = System.getProperty("http.agent");//Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo X9L Build/MMB29M)

        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }

        //LogUtils.v("User-Agent","User-Agent: "+ sb.toString());
        return sb.toString();
    }

    /**
     * 包名判断是否为主进程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getProcessName(context));
    }

    /**
     * 获取当前进程名称
     *
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     * counter the XPosed
     * */
    public static void counterXP(){
        try {
            Field v0_1 = ClassLoader.getSystemClassLoader()
                    .loadClass("de.robv.android.xposed.XposedBridge")
                    .getDeclaredField("disableHooks");
            v0_1.setAccessible(true);
            v0_1.set(null, Boolean.valueOf(true));
        }
        catch(Throwable v0) {

        }
    }

}
