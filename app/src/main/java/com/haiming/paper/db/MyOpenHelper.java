package com.haiming.paper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyOpenHelper extends SQLiteOpenHelper {

        private final static String DB_NAME = "note.db";// 数据库文件名
        private final static int DB_VERSION = 1;// 数据库版本

        public MyOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("数据","库：onCreate");
            // 创建分类表
            db.execSQL("create table db_group(g_id integer primary key autoincrement, " +
                    "g_name varchar, g_order integer, g_color varchar, g_encrypt integer," +
                    "g_create_time datetime, g_update_time datetime )");

            // 创建问题表
            db.execSQL("create table db_note(n_id integer primary key autoincrement, n_title varchar, " +
                    "n_content varchar, n_group_id integer, n_group_name varchar, n_type integer, " +
                    "n_bg_color varchar, n_encrypt integer, n_create_time datetime," +
                    "n_update_time datetime ,n_user_id integer,n_answer_size integer,n_answer_id varchar)");

            // 创建用户表
            db.execSQL("create table db_user(u_id integer primary key autoincrement,u_name varchar,"+
                    "u_password varchar,u_email varchar ,u_signature varchar, u_sex varchar,u_isManager integer)");

            // 创建管理员表
            db.execSQL("create table db_manager(m_id integer primary key autoincrement,m_name varchar,"+
            "m_password varchar , m_isManager integer)");

            //创建回答表
            db.execSQL("create table db_answer(a_id integer primary key autoincrement,a_note_id integer,a_content varchar,a_user_id integer)");

//            db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
//                    "values(?,?,?,?,?,?)", new String[]{"首页", "1", "#FFFFFF", "0", CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date())});

//            db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
//                    "values(?,?,?,?,?,?)", new String[]{"汽车", "1", "#FFFFFF", "0", CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date())});

//            db.execSQL("insert into db_note(n_title,n_content,n_group_id,n_group_name," +
//                    "n_type,n_bg_color,n_encrypt,n_create_time,n_update_time,n_user_id,n_answer_size,n_answer_id) " +
//                    "values(?,?,?,?,?,?,?,?,?,?,?,?)",new String[]{"你好","11111111",1+"","首页",2+"","#FFFFFF","0",CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date())
//            ,"0","0","null"});
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("数据库","onUpgrade");
        }
    }

