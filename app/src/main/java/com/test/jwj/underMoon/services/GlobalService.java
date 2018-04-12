package com.test.jwj.underMoon.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by jiangweijin on 2018/4/12.
 */

public class GlobalService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void AlertToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

}
