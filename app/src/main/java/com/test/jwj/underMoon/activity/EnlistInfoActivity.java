package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.jwj.underMoon.CustomView.DividerGridItemDecoration;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.RecyclerViewAdapter;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.ClientListenThread;
import com.test.jwj.underMoon.network.IMessageArrived;
import com.test.jwj.underMoon.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/5/12.
 */

public class EnlistInfoActivity extends Activity implements IMessageArrived<User>, View.OnClickListener {
    private int userId;
    private ProgressBar pb;
    private User enlister;
    private final Object key = new Object();
    private RecyclerView rv_photos;
    private RecyclerViewAdapter adapter;
    private ArrayList mPhotoList;
    private Handler mHandler;
    private TextView enlit_info_id;
    private TextView enlit_info_age;
    private TextView enlit_info_city;
    private TextView enlit_info_job;
    private TextView enlit_info_name;
    private ImageView enlist_info_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlist_info);
        pb = (ProgressBar) findViewById(R.id.enlist_info_pb);
        rv_photos = (RecyclerView) findViewById(R.id.rv_enlist_photos);
        findViewById(R.id.enlist_info_bt_chat).setOnClickListener(this);
        enlit_info_id = ((TextView) findViewById(R.id.enlist_info_id));
        enlit_info_age = ((TextView) findViewById(R.id.enlist_info_age));
        enlit_info_city = ((TextView) findViewById(R.id.enlist_info_city));
        enlit_info_job = ((TextView) findViewById(R.id.enlist_info_job));
        enlit_info_name = ((TextView) findViewById(R.id.enlist_info_name));
        enlist_info_photo = (ImageView) findViewById(R.id.enlist_info_photo);
        ClientListenThread.setMiDataListener(this);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0)
                    initPhotos();
                super.handleMessage(msg);
            }
        };
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
                synchronized (key){
                    try {
                        key.wait();
                        Log.e("tag","info wait");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void OnDataArrived(User user) {
        Log.e("tag","info " + user.getPhotos());
        enlister = user;
        if (user.getPhotos() == null || user.getPhotos().equals("")){
            mPhotoList = new ArrayList();
        }else{
            String[] urlArray = user.getPhotos().split("\\|");
            mPhotoList = new ArrayList(Arrays.asList(urlArray));
        }
        synchronized (key){
            key.notify();
        }
        Log.e("tag","init view");

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("friendName", enlister.getUserName());
        intent.putExtra("friendId", enlister.getId());
        startActivity(intent);
    }

    private void initPhotos(){
        enlit_info_id.setText(String.valueOf(enlister.getId()));
        enlit_info_age.setText(String.valueOf(enlister.getAge()) + "岁");
        enlit_info_city.setText(enlister.getLocation());
        enlit_info_job.setText(enlister.getJob());
        enlit_info_name.setText(enlister.getUserName());
        enlist_info_photo.setImageBitmap(PhotoUtils.getBitmap(enlister.getPhoto()));
        pb.setVisibility(View.GONE);
        adapter = new RecyclerViewAdapter(this,mPhotoList,userId);
        adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LayoutInflater inflater = LayoutInflater.from(EnlistInfoActivity.this);
                View bigPhoto = inflater.inflate(R.layout.dialog_big_photo,null);
                final AlertDialog dialog = new AlertDialog.Builder(EnlistInfoActivity.this).create();

                WindowManager wm = (WindowManager) EnlistInfoActivity.this
                        .getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int width =display.getWidth();
                int height=display.getHeight();

                Glide.with(EnlistInfoActivity.this).load("http://192.168.107.41:8089/" + userId + "/" + mPhotoList.get(position) + ".jpg")
                        .placeholder(R.mipmap.ic_launcher).crossFade().override(width,height).into((ImageView) bigPhoto.findViewById(R.id.large_photo));
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
        rv_photos.setAdapter(adapter);
        rv_photos.setLayoutManager(new GridLayoutManager(this,4));
        rv_photos.addItemDecoration(new DividerGridItemDecoration(this));
    }
}
