package com.haiming.paper.db;

import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserBean {

    @Id(autoincrement = true)
    private Long id;

    @NonNull
    private String name;

    @Generated(hash = 759355387)
    public UserBean(Long id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
