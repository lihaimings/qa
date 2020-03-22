package com.haiming.paper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haiming.paper.Utils.CommonUtil;

import java.util.Date;


public class MyOpenHelper extends SQLiteOpenHelper {

        private final static String DB_NAME = "note.db";// 数据库文件名
        private final static int DB_VERSION = 1;// 数据库版本

        public MyOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // 创建分类表
            db.execSQL("create table db_group(g_id integer primary key autoincrement, " +
                    "g_name varchar, g_order integer, g_color varchar, g_encrypt integer," +
                    "g_create_time datetime, g_update_time datetime )");

            // 创建笔记表
            db.execSQL("create table db_note(n_id integer primary key autoincrement, n_title varchar, " +
                    "n_content varchar, n_group_id integer, n_group_name varchar, n_type integer, " +
                    "n_bg_color varchar, n_encrypt integer, n_create_time datetime," +
                    "n_update_time datetime )");

            // 创建用户表
            db.execSQL("create table db_user(u_id integer primary key autoincrement,u_name varchar UNIQUE NOT NULL ,"+
                    "u_password varchar NOT NULL,u_email varchar UNIQUE,u_signature varchar, u_sex varchar,u_isManager integer)");

            // 创建管理员表
            db.execSQL("create table db_manager(m_id integer primary key autoincrement,m_name varchar UNIQUE NOT NULL,"+
            "m_password varchar NOT NULL, m_isManager integer)");

            db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
                    "values(?,?,?,?,?,?)", new String[]{"默认笔记", "1", "#FFFFFF", "0", CommonUtil.date2string(new Date()),CommonUtil.date2string(new Date())});
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

