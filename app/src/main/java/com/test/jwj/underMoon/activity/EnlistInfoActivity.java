package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.IMessageArrived;

/**
 * Created by Administrator on 2018/5/12.
 */

public class EnlistInfoActivity extends Activity implements IMessageArrived<User>{
    private int userId;
    private ProgressBar pb;
    private User enlister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlist_info);
        pb = (ProgressBar) findViewById(R.id.enlist_info_pb);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        userId = Integer.parseInt(intent.getStringExtra("enlistId"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAction.getUserInfo(userId);
            }
        }).start();
    }

    @Override
    public void OnDataArrived(User user) {
        enlister = user;
        pb.setVisibility(View.GONE);
    }
}
