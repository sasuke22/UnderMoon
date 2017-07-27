package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/7/27.
 */
public class FriendDetailActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        getIntent().getIntExtra("friendId",0);
        //TODO 根据ID查表进行更新

        initViews();
    }

    private void initViews() {
        findViewById(R.id.btn_add_friend).setOnClickListener(this);
        findViewById(R.id.btn_send_msg).setOnClickListener(this);
        findViewById(R.id.btn_stop_chat).setOnClickListener(this);
    }

    //TODO 相应的网络操作
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_friend:

                break;
            case R.id.btn_send_msg:

                break;
            case R.id.btn_stop_chat:

                break;
        }
    }
}
