package com.test.jwj.underMoon.bean;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Administrator on 2017/4/22.
 */

public class Friends {
    public int id;
    public int gender;//TODO 0--man;1--woman
    public String type;
    public String loveType;
    public String age;
    public int height;
    public String name;
    public String job;
    public String xingzuo;
    public String marry;
    public String figure;
    public String city;
    public BitmapDrawable touxiang;

    public Friends(int id, int gender, String type, String loveType, String age, int height, String name, String job,
                   String xingzuo, String marry, String figure, String city, BitmapDrawable touxiang) {
        this.id = id;
        this.gender = gender;
        this.type = type;
        this.loveType = loveType;
        this.age = age;
        this.height = height;
        this.name = name;
        this.job = job;
        this.xingzuo = xingzuo;
        this.marry = marry;
        this.figure = figure;
        this.city = city;
        this.touxiang = touxiang;
    }

}
