package com.test.jwj.underMoon.javabean;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Administrator on 2017/4/22.
 */

public class Friends {
    public int id;
    public String name;
    public String age;
    public String marry;
    public String figure;
    public String city;
    public BitmapDrawable touxiang;
    public Friends(int id, String name, String age, String marry, String figure, String city, BitmapDrawable touxiang) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.marry = marry;
        this.figure = figure;
        this.city = city;
        this.touxiang = touxiang;
    }

}
