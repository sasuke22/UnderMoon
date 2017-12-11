package com.test.jwj.underMoon.global;

import android.app.Application;
import android.graphics.drawable.BitmapDrawable;

import com.test.jwj.underMoon.bean.Friends;
import com.test.jwj.underMoon.bean.User;

/**
 * Created by Administrator on 2017/7/28.
 */

public class UnderMoonApplication extends Application {
    private User mUser;
//    private Friends mUser;

    public void setUser(User user){
        mUser = user;
    }

    public void setUserId(int id) {
        this.mUser.setId(id);
    }

    public void setUserGender(int gender) {
        this.mUser.setGender(gender);
    }

//    public void setUserType(String type) {
//        this.mUser.type = type;
//    }

//    public void setUserLoveType(String loveType) {
//        this.mUser.loveType = loveType;
//    }

//    public void setUserAge(String age) {
//        this.mUser.age = age;
//    }

//    public void setUserHeight(int height) {
//        this.mUser.height = height;
//    }

    public void setUserName(String name) {
        this.mUser.setUserName(name);
    }

//    public void setUserJob(String job) {
//        this.mUser.job = job;
//    }

//    public void setUserXingzuo(String xingzuo) {
//        this.mUser.xingzuo = xingzuo;
//    }

//    public void setUserMarry(String marry) {
//        this.mUser.marry = marry;
//    }

//    public void setUserFigure(String figure) {
//        this.mUser.figure = figure;
//    }

//    public void setUserCity(String city) {
//        this.mUser.city = city;
//    }

    public void setUserTouxiang(BitmapDrawable touxiang) {
        //TODO 需要将其他地方的获取头像方法改成bytes
//        this.mUser.setPhoto();
    }

    public int getUserId() {
        return mUser.getId();
    }

    public int getUserGender() {
        return mUser.getGender();
    }

//    public String getUserType() {
//        return mUser.type;
//    }

//    public String getUserLoveType() {
//        return mUser.loveType;
//    }

//    public String getUserAge() {
//        return mUser.age;
//    }

//    public int getUserHeight() {
//        return mUser.height;
//    }

    public String getUserName() {
        return mUser.getUserName();
    }

//    public String getUserJob() {
//        return mUser.job;
//    }

//    public String getUserXingzuo() {
//        return mUser.xingzuo;
//    }

//    public String getUserMarry() {
//        return mUser.marry;
//    }

//    public String getUserFigure() {
//        return mUser.figure;
//    }

//    public String getUserCity() {
//        return mUser.city;
//    }

//    public BitmapDrawable getUserTouxiang() {
//        return new BitmapDrawable(mUser.getPhoto());
//    }
}
