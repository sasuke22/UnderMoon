package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.ClientListenThread;
import com.test.jwj.underMoon.network.IMessageArrived;

public class InvitationDetailActivity extends Activity implements View.OnClickListener,IMessageArrived<MeetingDetail> {
    private int id;
    private int meetingId;
    private ProgressBar loadingBar;
    private Button btn_register_meeting;
    private final Object key = new Object();
    private MeetingDetail mInvitationDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
        loadingBar = (ProgressBar) findViewById(R.id.invitation_detail_bar);
        ClientListenThread.setMiDataListener(this);
        getInvitationDetailActivity();

    }

    private void getInvitationDetailActivity() {
        loadingBar.setVisibility(View.VISIBLE);
        id = ApplicationData.getInstance().getUserInfo().getId();
//        id = getIntent().getIntExtra("id",0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                meetingId = getIntent().getIntExtra("meetingId",0);
                UserAction.getInvitationDetail(meetingId);
                synchronized (key){// wait for the callback
                    try {
                        key.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("tag","init views");
                        loadingBar.setVisibility(View.GONE);
                        initViews();
                    }
                });

            }
        }).start();

    }

    private void initViews() {
        LinearLayout ll_liuyan;
        ll_liuyan = (LinearLayout) findViewById(R.id.ll_liuyan);
        btn_register_meeting = (Button) findViewById(R.id.bt_register_meeting);
        ListView lv_chat_msg;
        lv_chat_msg = (ListView) findViewById(R.id.lv_chat_msg);
        ListView lv_enlist_people;
        lv_enlist_people = (ListView) findViewById(R.id.lv_enlist_people);
        Button btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
        Button btn_stop_chat = (Button) findViewById(R.id.btn_stop_chat);
        TextView tv_leixing = (TextView) findViewById(R.id.tv_leixing);
        TextView tv_loveleixing = (TextView) findViewById(R.id.tv_loveleixing);
        TextView tv_age = (TextView) findViewById(R.id.tv_age);
        TextView tv_marry = (TextView) findViewById(R.id.tv_marry);
        TextView tv_tall = (TextView) findViewById(R.id.tv_tall);
        TextView tv_job = (TextView) findViewById(R.id.tv_job);
        TextView tv_tixing = (TextView) findViewById(R.id.tv_tixing);
        TextView tv_xingzuo = (TextView) findViewById(R.id.tv_xingzuo);
        TextView tv_address = (TextView) findViewById(R.id.tv_address);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);
        TextView tv_invitation_detail = (TextView) findViewById(R.id.tv_invitation_detail);
        if (id == mInvitationDetail.id){//自己点进自己的邀请
            // TODO 将其他按钮隐藏，显示报名列表信息
            lv_enlist_people.setVisibility(View.VISIBLE);//显示报名列表
            ll_liuyan.setVisibility(View.GONE);
            btn_register_meeting.setVisibility(View.GONE);
            lv_chat_msg.setVisibility(View.GONE);
            btn_send_msg.setVisibility(View.GONE);
            btn_stop_chat.setVisibility(View.GONE);
            initEnlist();
//        }
//        if (getIntent().getBooleanExtra("chat",false)){
//            lv_enlist_people.setVisibility(View.GONE);
//            ll_liuyan.setVisibility(View.GONE);//隐藏留言
//            btn_register_meeting.setVisibility(View.GONE);//隐藏包名按钮
//            lv_chat_msg.setVisibility(View.VISIBLE);//显示聊天信息
//            btn_send_msg.setVisibility(View.VISIBLE);
//            btn_stop_chat.setVisibility(View.VISIBLE);
//            initChatList();
        }else{
            lv_enlist_people.setVisibility(View.GONE);
            ll_liuyan.setVisibility(View.VISIBLE);
            btn_register_meeting.setVisibility(View.VISIBLE);
            lv_chat_msg.setVisibility(View.VISIBLE);
            btn_send_msg.setVisibility(View.VISIBLE);
            btn_stop_chat.setVisibility(View.VISIBLE);
            btn_register_meeting.setOnClickListener(this);
            initChatList();
        }
        tv_leixing.setText(mInvitationDetail.type);
        tv_loveleixing.setText(mInvitationDetail.loveType);
        tv_age.setText(String.valueOf(mInvitationDetail.age));
        tv_marry.setText(String.valueOf(mInvitationDetail.marry));//需要根据具体的值改成对应的文字
        tv_tall.setText(String.valueOf(mInvitationDetail.height));
        tv_job.setText(mInvitationDetail.job);
        tv_tixing.setText(mInvitationDetail.figure);
        tv_xingzuo.setText(mInvitationDetail.xingzuo);
        tv_address.setText(mInvitationDetail.city);
        tv_time.setText(mInvitationDetail.date.toString());
        tv_invitation_detail.setText(mInvitationDetail.content);

    }

    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAction.enlist(meetingId,id);
            }
        }).start();

    }

    @Override
    public void OnDataArrived(MeetingDetail invitationDetail) {
        Log.e("tag","data arrived");
        mInvitationDetail = invitationDetail;
        synchronized (key){
            key.notify();
        }
    }

    private void initEnlist(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAction.getEnlist(meetingId);
            }
        }).start();
    }

    private void initChatList(){
        // TODO 初始化聊天列表
    }
}
