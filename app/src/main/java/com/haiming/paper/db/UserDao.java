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
//            db.execSQL("create table db_user(u_id integer primary key autoincrement,u_name varchar,u_number varchar,"+
//                    "u_password varchar,u_email varchar ,u_signature varchar, u_sex varchar,u_isManager integer,u_image varchar)");
            cursor = db.query("db_user", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int userId = cursor.getInt(cursor.getColumnIndex("u_id"));
                String userName = cursor.getString(cursor.getColumnIndex("u_name"));
                String useNumber = cursor.getString(cursor.getColumnIndex("u_number"));
                String userPassword = cursor.getString(cursor.getColumnIndex("u_password"));
                String userEmail = cursor.getString(cursor.getColumnIndex("u_email"));
                String userSignature = cursor.getString(cursor.getColumnIndex("u_signature"));
                String userSex = cursor.getString(cursor.getColumnIndex("u_sex"));
                int userIsManager = cursor.getInt(cursor.getColumnIndex("u_isManager"));
                String userImage = cursor.getString(cursor.getColumnIndex("u_image"));
                //生成一个类
                user = new User();
                user.setId(userId);
                user.setName(userName);
                user.setNumber(useNumber);
                user.setPassword(userPassword);
                user.setEmail(userEmail);
                user.setSignature(userSignature);
                user.setSex(userSex);
                user.setIsManager(userIsManager);
                user.setImagePath(userImage);
                userList.add(user);
                Log.d("查询用户","成功");
            }

        } catch (Exception e) {
            Log.d("查询用户","失败");
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


    public int userLogin(String userNumber,String password) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int id = 0;
        String sql="select * from db_user where u_number=? and u_password=?";
        Cursor cursor=db.rawQuery(sql, new String[]{userNumber,password});
        if(cursor.moveToFirst()==true){
            id = cursor.getInt(cursor.getColumnIndex("u_id"));
            cursor.close();
            db.close();
            return id;
        }
        db.close();
        return id;
    }

    public boolean haveUserNum(String userNumbeer) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int id = 0;
        String sql="select * from db_user where u_number=?";
        Cursor cursor=db.rawQuery(sql, new String[]{userNumbeer});
        if(cursor.moveToFirst()==true){
            return true;
        }
        db.close();
        return false;
    }

    /**
     * 根据用户名查询分类
     */
    public User queryGroupByName(String userNumber) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_user", null, "u_number=?", new String[]{userNumber}, null, null, null);
            while (cursor.moveToNext()) {
                int userId = cursor.getInt(cursor.getColumnIndex("u_id"));
                String userName = cursor.getString(cursor.getColumnIndex("u_name"));
                String useNumber = cursor.getString(cursor.getColumnIndex("u_number"));
                String userPassword = cursor.getString(cursor.getColumnIndex("u_password"));
                String userEmail = cursor.getString(cursor.getColumnIndex("u_email"));
                String userSignature = cursor.getString(cursor.getColumnIndex("u_signature"));
                String userSex = cursor.getString(cursor.getColumnIndex("u_sex"));
                int userIsManager = cursor.getInt(cursor.getColumnIndex("u_isManager"));
                String userImage = cursor.getString(cursor.getColumnIndex("u_image"));
                //生成一个类
                user = new User();
                user.setId(userId);
                user.setName(userName);
                user.setNumber(useNumber);
                user.setPassword(userPassword);
                user.setEmail(userEmail);
                user.setSignature(userSignature);
                user.setSex(userSex);
                user.setIsManager(userIsManager);
                user.setImagePath(userImage);
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
                // 创建用户表
                String userName = cursor.getString(cursor.getColumnIndex("u_name"));
                String useNumber = cursor.getString(cursor.getColumnIndex("u_number"));
                String userPassword = cursor.getString(cursor.getColumnIndex("u_password"));
                String userEmail = cursor.getString(cursor.getColumnIndex("u_email"));
                String userSignature = cursor.getString(cursor.getColumnIndex("u_signature"));
                String userSex = cursor.getString(cursor.getColumnIndex("u_sex"));
                int userIsManager = cursor.getInt(cursor.getColumnIndex("u_isManager"));
                String userImage = cursor.getString(cursor.getColumnIndex("u_image"));
                //生成一个类
                user = new User();
                user.setId(userId);
                user.setNumber(useNumber);
                user.setName(userName);
                user.setPassword(userPassword);
                user.setEmail(userEmail);
                user.setSignature(userSignature);
                user.setSex(userSex);
                user.setIsManager(userIsManager);
                user.setImagePath(userImage);
                return user;
            }
        } catch (Exception e) {
            Log.d("用户查询","异常");
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
            values.put("u_number",user.getNumber());
            values.put("u_password", user.getPassword());
            values.put("u_email", user.getEmail());
            values.put("u_signature", user.getSignature());
            values.put("u_sex", user.getSex());
            values.put("u_isManager",user.getIsManager());
            values.put("u_image",user.getImagePath());
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
            Log.d("更新","开始更新用户");
            ContentValues values = new ContentValues();
            values.put("u_id",user.getId());
            values.put("u_name", user.getName());
            values.put("u_number",user.getNumber());
            values.put("u_password", user.getPassword());
            values.put("u_email", user.getEmail());
            values.put("u_signature", user.getSignature());
            values.put("u_sex", user.getSex());
            values.put("u_isManager",user.getIsManager());
            values.put("u_image",user.getImagePath());
            Log.d("更新","查Id更新用户="+user.getId());
            db.update("db_user", values, "u_id=?", new String[]{user.getId() + ""});
            Log.d("更新","更新用户信息完成");
        } catch (Exception e) {
            Log.d("更新","更新用户信息异常");
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
