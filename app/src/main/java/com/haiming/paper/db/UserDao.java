package com.haiming.paper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haiming.paper.bean.User;
import com.hjq.toast.ToastUtils;

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
            List<User> userList = queryUserAll();
            for (User user1 : userList) {
                if (user1.getName() == user.getName()) {
                    ToastUtils.show("用户名已被注册");
                    return;
                }
                if (!user.getEmail().isEmpty()) {
                    if (user.getEmail() == user1.getEmail()) {
                        ToastUtils.show("邮箱已被注册");
                        return;
                    }
                }
            }
            ContentValues values = new ContentValues();
            values.put("u_name", user.getName());
            values.put("u_password", user.getPassword());
            values.put("u_eamil", user.getEmail());
            values.put("u_signature", user.getSignature());
            values.put("u_sex", user.getSex());
            values.put("u_isManager",0);
            db.insert("db_user", null, values);
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
     * 更新一个用户
     */
    public void updateGroup(User user) {
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
    public int deleteGroup(int userId) {
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
