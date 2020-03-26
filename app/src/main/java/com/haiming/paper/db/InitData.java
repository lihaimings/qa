package com.haiming.paper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.haiming.paper.Utils.CommonUtil;

import java.util.Date;

public class InitData {

    private MyOpenHelper mHelper;

    public InitData(Context context) {
        mHelper = new MyOpenHelper(context);
    }

    /**
     * 初始化分组
     */
    public void initGroup(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {
            db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
                    "values(?,?,?,?,?,?)", new String[]{"首页", "1", "#FFFFFF", "0", CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date())});

            db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
                    "values(?,?,?,?,?,?)", new String[]{"汽车", "1", "#FFFFFF", "0", CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date())});
            Log.d("数据","分组初始化完成");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void initUser(){

        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {

            db.execSQL("insert into db_user(u_name, u_password, u_email, u_signature, u_sex, u_isManager) " +
                    "values(?,?,?,?,?,?)", new String[]{"水星", "123456", "1056598@qq.com", "番茄炒鸡蛋","男","0"});

            db.execSQL("insert into db_user(u_name, u_password, u_email, u_signature, u_sex, u_isManager) " +
                    "values(?,?,?,?,?,?)", new String[]{"水星2", "123456", "1056594@qq.com", "番茄炒鸡蛋","女","0"});
            Log.d("数据","用户初始化完成");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void initManager(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {

            db.execSQL("insert into db_manager(m_name, m_password, m_isManager) " +
                    "values(?,?,?)", new String[]{"管理员", "123456", "1"});

            db.execSQL("insert into db_manager(m_name, m_password, m_isManager) " +
                    "values(?,?,?)", new String[]{"123456", "123456", "1"});
            Log.d("数据","管理员初始化完成");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void initAsk(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        try {

            String[] strings = {"为什么中国没有自己的平价跑车？","各大车场都有自己牌子的平民跑车，例如野马，本田86,马自达mx5等……价格其实相比很多奔驰，宝马来说并不算贵。是因为我们没有汽车文化？还是因为什么我们消费水平不够？亦或是汽车技术不过关？我们各车场并没有推出代表自己特点的平价跑车？是什么原因呢？"
                    ,"2","汽车","2","#FFFFFF","0",CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date()),"1",null,null};

            db.execSQL("insert into db_note(n_title, n_content, n_group_id, n_group_name, n_type, n_bg_color," +
                    "n_encrypt,n_create_time,n_update_time,n_user_id,n_answer_size,n_answer_id) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?)", strings);
            strings.clone();

            String[] strings2 = {"刷 LeetCode 对于国内 IT 企业面试帮助大吗？","今年大三，大四要找工作了，没搞过ACM（其实挺后悔的），校招面试都考算法的，我这种没搞过ACM的感觉挺没竞争力的，同学有推荐leetcode的，不知对于国内的IT企业面试帮助大吗？"
                    ,"1","首页","2","#FFFFFF","0",CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date()),"1",null,null};
            db.execSQL("insert into db_note(n_title, n_content, n_group_id, n_group_name, n_type, n_bg_color," +
                    "n_encrypt,n_create_time,n_update_time,n_user_id,n_answer_size,n_answer_id) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?)", strings2);
            strings2.clone();
            Log.d("数据","问题初始化完成");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
