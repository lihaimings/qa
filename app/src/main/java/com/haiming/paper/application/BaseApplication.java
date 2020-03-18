package com.haiming.paper.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;


import com.haiming.paper.Utils.CommonUtil;
import com.haiming.paper.Utils.GreendaoUtils;
import com.haiming.paper.db.DaoSession;


/**
 * Created Time: 2016/11/21 20:44
 *
 * @author lee
 */

public class BaseApplication extends Application {
    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainThreadId;
    private static Looper mMainLooper;
    private static Handler mMainHandler;
    private static String intentUrl;
    private static GreendaoUtils  mGreendaoUtils;
    private static DaoSession mDaoSession;

    private String processName;

    public static Context getContext() {
        return mContext;
    }

    public static Thread getmMainThread() {
        return mMainThread;
    }

    public static long getmMainThreadId() {
        return mMainThreadId;
    }

    public static Looper getmMainLooper() {
        return mMainLooper;
    }

    public static Handler getmMainHandler() {
        return mMainHandler;
    }

    public static String getIntentUrl() {
        return intentUrl;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mMainLooper = getMainLooper();
        mMainHandler = new Handler();
        processName = CommonUtil.getProcessName(this);

        mGreendaoUtils =  GreendaoUtils.newInstance(this);
        mDaoSession = mGreendaoUtils.getDaoSession();

    }


}