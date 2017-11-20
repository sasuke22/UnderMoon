package com.test.jwj.underMoon.global;

import android.app.Application;
import android.graphics.drawable.BitmapDrawable;

import com.test.jwj.underMoon.bean.Friends;

/**
 * Created by Administrator on 2017/7/28.
 */

public class UnderMoonApplication extends Application {
    private Friends mUser;

    public void setUser(Friends user){
        mUser = user;
    }

    public void setUserId(int id) {
        this.mUser.id = id;
    }

    public void setUserGender(int gender) {
        this.mUser.gender = gender;
    }

    public void setUserType(String type) {
        this.mUser.type = type;
    }

    public void setUserLoveType(String loveType) {
        this.mUser.loveType = loveType;
    }

    public void setUserAge(String age) {
        this.mUser.age = age;
    }

    public void setUserHeight(int height) {
        this.mUser.height = height;
    }

    public void setUserName(String name) {
        this.mUser.name = name;
    }

    public void setUserJob(String job) {
        this.mUser.job = job;
    }

    public void setUserXingzuo(String xingzuo) {
        this.mUser.xingzuo = xingzuo;
    }

    public void setUserMarry(String marry) {
        this.mUser.marry = marry;
    }

    public void setUserFigure(String figure) {
        this.mUser.figure = figure;
    }

    public void setUserCity(String city) {
        this.mUser.city = city;
    }

    public void setUserTouxiang(BitmapDrawable touxiang) {
        this.mUser.touxiang = touxiang;
    }

    public int getUserId() {
        return mUser.id;
    }

    public int getUserGender() {
        return mUser.gender;
    }

    public String getUserType() {
        return mUser.type;
    }

    public String getUserLoveType() {
        return mUser.loveType;
    }

    public String getUserAge() {
        return mUser.age;
    }

    public int getUserHeight() {
        return mUser.height;
    }

    public String getUserName() {
        return mUser.name;
    }

    public String getUserJob() {
        return mUser.job;
    }

    public String getUserXingzuo() {
        return mUser.xingzuo;
    }

    public String getUserMarry() {
        return mUser.marry;
    }

    public String getUserFigure() {
        return mUser.figure;
    }

    public String getUserCity() {
        return mUser.city;
    }

    public BitmapDrawable getUserTouxiang() {
        return mUser.touxiang;
    }
}
