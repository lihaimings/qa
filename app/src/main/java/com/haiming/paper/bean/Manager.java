package com.haiming.paper.bean;


public class Manager {

    // id
    private int id;

    // name
    private String name;

    // 密码
    private String password;

    // 是否是管理员
    private int isManager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isManager() {
        return isManager;
    }

    public void setManager(int manager) {
        isManager = manager;
    }
}
