package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.os.Bundle;

import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/7/27.
 */
public class FriendDetailActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        getIntent().getIntExtra("friendId",0);
        //TODO 根据ID查表进行更新


    }
}
