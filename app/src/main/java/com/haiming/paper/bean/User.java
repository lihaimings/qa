package com.haiming.paper.bean;

import java.io.Serializable;

public class User implements Serializable {

    // id
    private int id;

    // 用户名
    private String name;

    // 密码
    private String password;

    // 邮箱
    private String email;

    // 签名
    private String signature;

    //性别
    private String sex;

    // 是否管理员 0不是 1是
    private int isManager;

    private String imagePath;

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsManager() {
        return isManager;
    }

    public void setIsManager(int isManager) {
        this.isManager = isManager;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", signature='" + signature + '\'' +
                ", sex='" + sex + '\'' +
                ", isManager=" + isManager +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
