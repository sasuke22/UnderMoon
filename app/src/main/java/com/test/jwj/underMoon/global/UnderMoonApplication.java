package com.test.jwj.underMoon.global;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;

import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.services.GlobalService;

/**
 * Created by Administrator on 2017/7/28.
 */

public class UnderMoonApplication extends Application {
    private User                      mUser;
    private ServiceConnection         mConnection;
    public GlobalService.ToastBinder mBinder;
    public static UnderMoonApplication mContext;
    //    private Friends mUser;

    public void setUser(User user){
        mUser = user;
    }

    public User getUser(){return mUser;}

    public void setUserId(int id) {
        this.mUser.setId(id);
    }

    public void setUserGender(int gender) {
        this.mUser.setGender(gender);
    }

    public void setUserName(String name) {
        this.mUser.setUserName(name);
    }

    public void setUserTouxiang(BitmapDrawable touxiang) {
//        this.mUser.setPhoto();
    }

    public int getUserId() {
        return mUser.getId();
    }

    public int getUserGender() {
        return mUser.getGender();
    }

    public String getUserName() {
        return mUser.getUserName();
    }

    @Override
    public void onCreate() {
        mContext = this;
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = (GlobalService.ToastBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBinder = null;
            }
        };
        bindService(new Intent(this, GlobalService.class),mConnection,BIND_AUTO_CREATE);
        super.onCreate();
        initNetwork();
    }

    private void initNetwork() {
//        NetWorkUtils.initRetrofit();
    }
}
