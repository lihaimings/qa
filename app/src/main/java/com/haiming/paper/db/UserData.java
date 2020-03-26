package com.haiming.paper.db;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {

    public static void saveUserId(Context context,int userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putInt("user_id",userId);
        editor.commit();

    }

    public static int getUserId(Context context){
        SharedPreferences preferences=context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        int id = preferences.getInt("user_id",0);
        return id;
    }

    public static boolean isUserLogin(Context context) {
        SharedPreferences preferences=context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        int id = preferences.getInt("user_id",0);
        if (id != 0){
            return true;
        }
        return false;
    }
}
