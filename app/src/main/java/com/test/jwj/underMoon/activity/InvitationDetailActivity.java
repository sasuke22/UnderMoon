package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/4/6.
 */

public class InvitationDetailActivity extends Activity implements View.OnClickListener {
    private int id;
    private int meetingId;
    private Button btn_register_meeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
        id = getIntent().getIntExtra("id",0);
        meetingId = getIntent().getIntExtra("meetingId",0);
        //TODO 通过id查找表格找到对应的要求数据

        initViews();
        btn_register_meeting.setOnClickListener(this);


    }

    private void initViews() {
        LinearLayout ll_liuyan;
        ll_liuyan = (LinearLayout) findViewById(R.id.ll_liuyan);
        btn_register_meeting = (Button) findViewById(R.id.bt_register_meeting);
        ListView lv_chat_msg;
        lv_chat_msg = (ListView) findViewById(R.id.lv_chat_msg);
        Button btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        Button btn_stop_chat = (Button) findViewById(R.id.btn_stop_chat);
        if (getIntent().getBooleanExtra("chat",false)){
            ll_liuyan.setVisibility(View.GONE);//隐藏留言
            btn_register_meeting.setVisibility(View.GONE);//隐藏包名按钮
            lv_chat_msg.setVisibility(View.VISIBLE);//显示聊天信息
            btn_send_msg.setVisibility(View.VISIBLE);
            btn_stop_chat.setVisibility(View.VISIBLE);
        }else{
            ll_liuyan.setVisibility(View.VISIBLE);
            btn_register_meeting.setVisibility(View.VISIBLE);
            lv_chat_msg.setVisibility(View.GONE);
            btn_send_msg.setVisibility(View.GONE);
            btn_stop_chat.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        //TODO 报名的相关网络操作
    }
}
