package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.IMessageArrived;

/**
 * Created by Administrator on 2018/5/12.
 */

public class EnlistInfoActivity extends Activity implements IMessageArrived<User>, View.OnClickListener {
    private int userId;
    private ProgressBar pb;
    private User enlister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlist_info);
        pb = (ProgressBar) findViewById(R.id.enlist_info_pb);
        findViewById(R.id.enlist_info_bt_chat).setOnClickListener(this);
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
        ((TextView) findViewById(R.id.enlist_info_id)).setText(user.getId());
        ((TextView) findViewById(R.id.enlist_info_age)).setText(user.getAge() + "岁");
        ((TextView) findViewById(R.id.enlist_info_city)).setText(user.getLocation());
        ((TextView) findViewById(R.id.enlist_info_job)).setText(user.getJob());
        ((TextView) findViewById(R.id.enlist_info_name)).setText(user.getUserName());
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        //TODO open the window of chat
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("friendName", enlister.getUserName());
        intent.putExtra("friendId", enlister.getId());
        startActivity(intent);
    }
}
