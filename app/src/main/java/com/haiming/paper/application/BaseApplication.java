package com.haiming.paper.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.haiming.paper.Utils.CommonUtil;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.db.InitData;
import com.haiming.paper.thread.ThreadManager;
import com.sendtion.xrichtext.IImageLoader;
import com.sendtion.xrichtext.XRichText;

import androidx.multidex.MultiDexApplication;


public class BaseApplication extends MultiDexApplication {
    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainThreadId;
    private static Looper mMainLooper;
    private static Handler mMainHandler;
    private InitData initData;

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


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mMainLooper = getMainLooper();
        mMainHandler = new Handler();
        processName = CommonUtil.getProcessName(this);


        //设置根据二进制设置图片
        initPic();

        SharedPreferences preferences=getSharedPreferences("first", Context.MODE_PRIVATE);
        boolean firstLogin = preferences.getBoolean("login",true);
        if (firstLogin){
            ThreadManager.getLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("数据","开始执行插入数据");
                    initData = new InitData(mContext);
                    initData.initGroup();
                    initData.initManager();
                    initData.initUser();
                    initData.initAsk();
                }
            });
        }

        SharedPreferences.Editor sharedPreferences = getSharedPreferences("first",Context.MODE_PRIVATE).edit();
        sharedPreferences.putBoolean("login",false);
        sharedPreferences.commit();

    }

    private void initPic() {
        XRichText.getInstance().setImageLoader(new IImageLoader() {
            @Override
            public void loadImage(String imagePath, ImageView imageView, int imageHeight) {
                if (imageHeight > 0) {//固定高度
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);//固定图片高度，记得设置裁剪剧中
                    lp.bottomMargin = 10;//图片的底边距
                    imageView.setLayoutParams(lp);


                    Glide.with(getApplicationContext()).load(UIUtil.string2byte(imagePath)).centerCrop().into(imageView);
                } else {//自适应高度
                    Glide.with(getApplicationContext()).load(UIUtil.string2byte(imagePath)).centerCrop()
                            .into(imageView);
//                    Glide.with(getApplicationContext()).load(UIUtil.string2Bitmap(imagePath))
//                            .placeholder(R.mipmap.img_load_fail).error(R.mipmap.img_load_fail).into(new TransformationScale(imageView));
                }
            }
        });
    }


}