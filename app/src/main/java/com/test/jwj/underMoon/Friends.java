package com.test.jwj.underMoon;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/4/22.
 */

public class Friends {
    public String name;
    public String age;
    public String marry;
    public String figure;
    public String city;
    public BitmapDrawable touxiang;
    public Friends(String name, String age, String marry, String figure, String city, BitmapDrawable touxiang) {
        this.name = name;
        this.age = age;
        this.marry = marry;
        this.figure = figure;
        this.city = city;
        this.touxiang = touxiang;
    }

}
