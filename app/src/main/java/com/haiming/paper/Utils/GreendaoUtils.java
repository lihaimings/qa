package com.haiming.paper.Utils;

import android.content.Context;

public class GreendaoUtils {
    private static GreendaoUtils mInstance;
    private GreendaoUtils(){


    }

    /**
     * 双重检测锁
     */
    public static GreendaoUtils getInstance(){
        if (mInstance==null){
            synchronized(GreendaoUtils.class){
                if (mInstance==null){
                    mInstance=new GreendaoUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化GreenDao，直接在Application中进行初始化操作
     */
    public void initGreenDao(Context context){




//        //创建daomaster
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,);
//        SQLiteDatabase db = helper.getWritableDatabase();//db读写数据库
//        DaoMaster daoMaster = new DaoMaster(db);
//
//        //创建daosession
//        daoSession = daoMaster.newSession();
    }

//    private DaoSession daoSession;
//    public DaoSession getDaoSession(){
//        return daoSession;
//    }
}
