package com.haiming.paper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haiming.paper.bean.Manager;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ManagerDao {
    private MyOpenHelper mHelper;

    public ManagerDao(Context context) {
        mHelper = new MyOpenHelper(context);
    }

    /**
     * 查询所有的管理员
     */
    public List<Manager> queryManagerAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<Manager> managerList = new ArrayList<>();

        Manager manager;
        Cursor cursor = null;
        try {
            cursor = db.query("db_manager", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int managerId = cursor.getInt(cursor.getColumnIndex("m_id"));
                String managerName = cursor.getString(cursor.getColumnIndex("m_name"));
                String managerPassword = cursor.getString(cursor.getColumnIndex("m_password"));
                int managerIsManager = cursor.getInt(cursor.getColumnIndex("m_isManager"));
                //生成一个类
                manager = new Manager();
                manager.setId(managerId);
                manager.setName(managerName);
                manager.setPassword(managerPassword);
                manager.setManager(managerIsManager);
                managerList.add(manager);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return managerList;
    }

    /**
     * 根据管理员名查询分类
     */
    public Manager queryManagerByName(String managerName) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Manager manager = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_manager", null, "m_name=?", new String[]{managerName + ""}, null, null, null);
            while (cursor.moveToNext()) {
                int managerId = cursor.getInt(cursor.getColumnIndex("m_id"));
                String managerPassword = cursor.getString(cursor.getColumnIndex("m_password"));
                int managerIsManager = cursor.getInt(cursor.getColumnIndex("m_isManager"));
                //生成一个类
                manager = new Manager();
                manager.setId(managerId);
                manager.setName(managerName);
                manager.setPassword(managerPassword);
                manager.setManager(managerIsManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return manager;
    }

    /**
     * 根据管理员ID查询分类
     *
     * @return
     */
    public Manager queryManagerById(int managerId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Manager manager = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_manager", null, "m_id=?", new String[]{managerId + ""}, null, null, null);
            while (cursor.moveToNext()) {
                String managerName = cursor.getString(cursor.getColumnIndex("m_name"));
                String managerPassword = cursor.getString(cursor.getColumnIndex("m_password"));
                int managerIsManager = cursor.getInt(cursor.getColumnIndex("m_isManager"));
                //生成一个类
                manager = new Manager();
                manager.setId(managerId);
                manager.setName(managerName);
                manager.setPassword(managerPassword);
                manager.setManager(managerIsManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return manager;
    }

    /**
     * 添加一个管理员
     */
    public void insertManager(Manager manager) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Cursor cursor = null;
        try {
            List<Manager> managerList = queryManagerAll();
            for (Manager manager1 : managerList) {
                if (manager1.getName() == manager.getName()) {
                    ToastUtils.show("管理员名已被注册");
                    return;
                }
            }
            // 创建管理员表
            db.execSQL("create table db_manager(m_id integer primary key autoincrement,m_name varchar UNIQUE NOT NULL,"+
                    "m_password varchar NOT NULL, m_isManager integer)");
            ContentValues values = new ContentValues();
            values.put("m_id", manager.getId());
            values.put("m_name", manager.getName());
            values.put("m_password", manager.getPassword());
            values.put("m_isManager", 1);
            db.insert("db_manager", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 更新一个管理员
     */
    public void updateManager(Manager manager) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("m_id", manager.getId());
            values.put("m_name", manager.getName());
            values.put("m_password", manager.getPassword());
            values.put("m_isManager", 1);
            db.update("db_user", values, "m_id=?", new String[]{manager.getId() + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除一个管理员
     */
    public int deleteManager(int managerId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        int ret = 0;
        try {
            ret = db.delete("db_user", "m_id=?", new String[]{managerId + ""});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }
}
