package com.haiming.paper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.haiming.paper.bean.User;

import java.util.ArrayList;
import java.util.List;


public class UserDao {

    private MyOpenHelper mHelper;

    public UserDao(Context context) {
        mHelper = new MyOpenHelper(context);
    }

    /**
     * 查询所有的用户
     */
    public List<User> queryUserAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<User> userList = new ArrayList<>();

        User user;
        Cursor cursor = null;
        try {
            cursor = db.query("db_user", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int userId = cursor.getInt(cursor.getColumnIndex("u_id"));
                String userName = cursor.getString(cursor.getColumnIndex("u_name"));
                String userPassword = cursor.getString(cursor.getColumnIndex("u_password"));
                String userEmail = cursor.getString(cursor.getColumnIndex("u_eamil"));
                String userSignature = cursor.getString(cursor.getColumnIndex("u_signature"));
                String userSex = cursor.getString(cursor.getColumnIndex("u_sex"));
                int userIsManager = cursor.getInt(cursor.getColumnIndex("u_isManager"));
                //生成一个类
                user = new User();
                user.setId(userId);
                user.setName(userName);
                user.setPassword(userPassword);
                user.setEmail(userEmail);
                user.setSignature(userSignature);
                user.setSex(userSex);
                user.setIsManager(userIsManager);
                userList.add(user);
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
        return userList;
    }


    public int userLogin(String username,String password) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int id = 0;
        String sql="select * from db_user where u_name=? and u_password=?";
        Cursor cursor=db.rawQuery(sql, new String[]{username,password});
        if(cursor.moveToFirst()==true){
            id = cursor.getInt(cursor.getColumnIndex("u_id"));
            cursor.close();
            db.close();
            return id;
        }
        db.close();
        return id;
    }

    public boolean haveUserNum(String username) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int id = 0;
        String sql="select * from db_user where u_name=?";
        Cursor cursor=db.rawQuery(sql, new String[]{username});
        if(cursor.moveToFirst()==true){
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 根据用户名查询分类
     */
    public User queryGroupByName(String userName) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_user", null, "u_name=?", new String[]{userName}, null, null, null);
            while (cursor.moveToNext()) {
                int userId = cursor.getInt(cursor.getColumnIndex("u_id"));
                String userPassword = cursor.getString(cursor.getColumnIndex("u_password"));
                String userEmail = cursor.getString(cursor.getColumnIndex("u_eamil"));
                String userSignature = cursor.getString(cursor.getColumnIndex("u_signature"));
                String userSex = cursor.getString(cursor.getColumnIndex("u_sex"));
                int userIsManager = cursor.getInt(cursor.getColumnIndex("u_isManager"));
                //生成一个类
                user = new User();
                user.setId(userId);
                user.setName(userName);
                user.setPassword(userPassword);
                user.setEmail(userEmail);
                user.setSignature(userSignature);
                user.setSex(userSex);
                user.setIsManager(userIsManager);
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
        return user;
    }

    /**
     * 根据用户ID查询分类
     *
     * @return
     */
    public User queryUserById(int userId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_user", null, "u_id=?", new String[]{userId + ""}, null, null, null);
            while (cursor.moveToNext()) {
                String userName = cursor.getString(cursor.getColumnIndex("u_name"));
                String userPassword = cursor.getString(cursor.getColumnIndex("u_password"));
                String userEmail = cursor.getString(cursor.getColumnIndex("u_eamil"));
                String userSignature = cursor.getString(cursor.getColumnIndex("u_signature"));
                String userSex = cursor.getString(cursor.getColumnIndex("u_sex"));
                int userIsManager = cursor.getInt(cursor.getColumnIndex("u_isManager"));
                //生成一个类
                user = new User();
                user.setId(userId);
                user.setName(userName);
                user.setPassword(userPassword);
                user.setEmail(userEmail);
                user.setSignature(userSignature);
                user.setSex(userSex);
                user.setIsManager(userIsManager);
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
        return user;
    }


    /**
     * 添加一个分用户
     */
    public void insertUser(User user) {

        SQLiteDatabase db = mHelper.getWritableDatabase();

        Cursor cursor = null;
        try {
            ContentValues values = new ContentValues();
            values.put("u_name", user.getName());
            values.put("u_password", user.getPassword());
            values.put("u_email", user.getEmail());
            values.put("u_signature", user.getSignature());
            values.put("u_sex", user.getSex());
            values.put("u_isManager",user.getIsManager());
            db.insert("db_user", null, values);
            Log.d("数据","用户注册完成");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("数据","用户注册异常");
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
     * 更新一个用户
     */
    public void updateUser(User user) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("u_name", user.getName());
            values.put("u_password", user.getPassword());
            values.put("u_eamil", user.getEmail());
            values.put("u_signature", user.getSignature());
            values.put("u_sex", user.getSex());
            values.put("u_isManager",0);
            db.update("db_user", values, "u_id=?", new String[]{user.getId() + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除一个用户
     */
    public int deleteUser(int userId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        int ret = 0;
        try {
            ret = db.delete("db_user", "u_id=?", new String[]{userId + ""});

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
