package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.test.jwj.underMoon.CustomView.DividerGridItemDecoration;
import com.test.jwj.underMoon.CustomView.EnlisterView;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.RecyclerViewAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.IMessageArrived;

import java.util.ArrayList;
import java.util.HashMap;

public class InvitationDetailActivity extends Activity implements View.OnClickListener,IMessageArrived<MeetingDetail> {
    private User        mUser;
    private int         meetingId;
    private ProgressBar loadingBar;
    private final Object key = new Object();
    private MeetingDetail mInvitationDetail;
    private EnlisterView tv_enlist_people;
    private HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
        loadingBar = (ProgressBar) findViewById(R.id.invitation_detail_bar);
        UserAction.setMiDataListener(this);
        getInvitationDetailActivity();

    }

    private void getInvitationDetailActivity() {
        loadingBar.setVisibility(View.VISIBLE);
        mUser = ApplicationData.getInstance().getUserInfo();
//        id = getIntent().getIntExtra("id",0);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                meetingId = getIntent().getIntExtra("meetingId",0);
//                UserAction.getInvitationDetail(meetingId);
////                UserAction.getEnlistName(meetingId);
//                synchronized (key){// wait for the callback
//                    try {
//                        key.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (mInvitationDetail != null){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            loadingBar.setVisibility(View.GONE);
//                            initViews();
//                        }
//                    });
//                }else
//                    Toast.makeText(InvitationDetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
//
//            }
//        }).start();
        meetingId = getIntent().getIntExtra("meetingId",0);
        UserAction.getInvitationDetail(meetingId);
    }

    private void initViews() {
        LinearLayout ll_liuyan;
        ll_liuyan = (LinearLayout) findViewById(R.id.ll_liuyan);
        TextView btn_register_meeting = (TextView) findViewById(R.id.bt_register_meeting);
        tv_enlist_people = (EnlisterView) findViewById(R.id.tv_enlist_people);
        TextView btn_send_msg = (TextView) findViewById(R.id.btn_send_msg);
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

        //加载图片
        if (mInvitationDetail.pics > 0){
            final ArrayList<String> picList = new ArrayList<>();
            for (int i = 0;i < mInvitationDetail.pics;i++){
                picList.add(ApplicationData.SERVER_IP + "meeting" + "/" + mInvitationDetail.meetingId + "/" + i + ".jpg");
            }
            RecyclerView rv_pics = (RecyclerView) findViewById(R.id.rv_invitation_detail_pics);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,picList);
            rv_pics.setAdapter(adapter);
            rv_pics.setLayoutManager(new GridLayoutManager(this,4));
            rv_pics.addItemDecoration(new DividerGridItemDecoration(this));
            adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    LayoutInflater inflater = LayoutInflater.from(InvitationDetailActivity.this);
                    View bigPhoto = inflater.inflate(R.layout.dialog_big_photo,null);
                    final AlertDialog dialog = new AlertDialog.Builder(InvitationDetailActivity.this).create();

                    WindowManager wm = (WindowManager) InvitationDetailActivity.this
                            .getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int width =display.getWidth();
                    int height=display.getHeight();

                    Glide.with(InvitationDetailActivity.this).load(picList.get(position))
                            .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).override(width,height)).transition(new DrawableTransitionOptions().crossFade()).into((ImageView) bigPhoto.findViewById(R.id.large_photo));
                    dialog.setView(bigPhoto);
                    dialog.show();
                    bigPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            });
        }

        if (mUser.getId() == mInvitationDetail.id){//自己点进自己的邀请
            // TODO 将其他按钮隐藏，显示报名列表信息
            tv_enlist_people.setVisibility(View.VISIBLE);//显示报名列表
            ll_liuyan.setVisibility(View.GONE);
            btn_register_meeting.setVisibility(View.GONE);
            btn_send_msg.setVisibility(View.GONE);
            initEnlist();
        }else{
            tv_enlist_people.setVisibility(View.GONE);
            ll_liuyan.setVisibility(View.VISIBLE);
            btn_register_meeting.setVisibility(View.VISIBLE);
            btn_send_msg.setVisibility(View.VISIBLE);
            btn_register_meeting.setOnClickListener(this);
        }
        tv_leixing.setText(mInvitationDetail.type);
        tv_loveleixing.setText(mInvitationDetail.loveType);
        tv_age.setText(String.valueOf(mInvitationDetail.age));
        switch (mInvitationDetail.marry){
            case 0:
                tv_marry.setText("单身");
                break;
            case 1:
                tv_marry.setText("已恋");
                break;
            case 2:
                tv_marry.setText("已婚");
                break;
            default:
                tv_marry.setText("不详");
                break;
        }
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
                UserAction.enlist(meetingId,mUser.getId(),mUser.getUserName());
            }
        }).start();

    }

    @Override
    public void OnDataArrived(MeetingDetail invitationDetail) {
        mInvitationDetail = invitationDetail;
        map = new HashMap<>();
        String[] idArray = mInvitationDetail.registId.split("\\|");
        String[] nameArray = mInvitationDetail.enlistersName.split("\\|");
        for (int i = 0;i < idArray.length;i++){
            Log.e("tag","id " + idArray[i] + ",name " + nameArray[i]);
            map.put(idArray[i],nameArray[i]);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingBar.setVisibility(View.GONE);
                initViews();
            }
        });
    }

    private void initEnlist(){
        tv_enlist_people.setData(map);
    }

}
