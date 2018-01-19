package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.ClientListenThread;
import com.test.jwj.underMoon.network.IMessageArrived;

public class InvitationDetailActivity extends Activity implements View.OnClickListener,IMessageArrived<MeetingDetail> {
    private int id;
    private int meetingId;
    private Button btn_register_meeting;
    private final Object key = new Object();
    private Dialog mDialog;
    private MeetingDetail mInvitationDetail;
    private TextView tv_leixing;
    private TextView tv_loveleixing;
    private TextView tv_age;
    private TextView tv_marry;
    private TextView tv_tall;
    private TextView tv_job;
    private TextView tv_tixing;
    private TextView tv_xingzuo;
    private TextView tv_address;
    private TextView tv_time;
    private TextView tv_invitation_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
        ClientListenThread.setMiDataListener(this);
        mDialog = new Dialog(this);
        getInvitationDetailActivity();
        initViews();

    }

    private void getInvitationDetailActivity() {
        id = getIntent().getIntExtra("id",0);
        meetingId = getIntent().getIntExtra("meetingId",0);
        UserAction.getInvitationDetail(meetingId);
        mDialog.show();
        synchronized (key){
            try {
                key.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initViews() {
        LinearLayout ll_liuyan;
        ll_liuyan = (LinearLayout) findViewById(R.id.ll_liuyan);
        btn_register_meeting = (Button) findViewById(R.id.bt_register_meeting);
        ListView lv_chat_msg;
        lv_chat_msg = (ListView) findViewById(R.id.lv_chat_msg);
        Button btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        Button btn_stop_chat = (Button) findViewById(R.id.btn_stop_chat);
        tv_leixing = (TextView) findViewById(R.id.tv_leixing);
        tv_loveleixing = (TextView) findViewById(R.id.tv_loveleixing);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_marry = (TextView) findViewById(R.id.tv_marry);
        tv_tall = (TextView) findViewById(R.id.tv_tall);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_tixing = (TextView) findViewById(R.id.tv_tixing);
        tv_xingzuo = (TextView) findViewById(R.id.tv_xingzuo);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_invitation_detail = (TextView) findViewById(R.id.tv_invitation_detail);
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
        tv_leixing.setText(mInvitationDetail.type);
        tv_loveleixing.setText(mInvitationDetail.loveType);
        tv_age.setText(mInvitationDetail.age + "");
        tv_marry.setText(mInvitationDetail.marry + "");
        tv_tall.setText(mInvitationDetail.height + "");
        tv_job.setText(mInvitationDetail.job);
        tv_tixing.setText(mInvitationDetail.figure);
        tv_xingzuo.setText(mInvitationDetail.xingzuo);
        tv_address.setText(mInvitationDetail.city);
        tv_time.setText(mInvitationDetail.date);
        tv_invitation_detail.setText(mInvitationDetail.content);

        btn_register_meeting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO 报名的相关网络操作
    }

    @Override
    public void OnDataArrived(MeetingDetail invitationDetail) {
        mInvitationDetail = invitationDetail;
        synchronized (key){
            key.notify();
            mDialog.dismiss();
        }
    }
}
