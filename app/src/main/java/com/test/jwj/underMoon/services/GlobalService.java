package com.test.jwj.underMoon.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by jiangweijin on 2018/4/12.
 */

public class GlobalService extends Service {
    private Handler mHandler;
    private ToastBinder mBinder = new ToastBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ToastBinder extends Binder{
        public void AlertToast(final String text){
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GlobalService.this,text,Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
