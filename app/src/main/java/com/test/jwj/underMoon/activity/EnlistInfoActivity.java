package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.test.jwj.underMoon.CustomView.DividerGridItemDecoration;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.RecyclerViewAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.IMessageArrived;
import com.test.jwj.underMoon.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Administrator on 2018/5/12.
 */

public class EnlistInfoActivity extends Activity implements IMessageArrived<User>, View.OnClickListener {
    private int userId;
    private ProgressBar pb;
    private User enlister;
    private RecyclerView rv_photos;
    private RecyclerViewAdapter adapter;
    private ArrayList mPhotoList;
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
        UserAction.setMiDataListener(this);
        findViewById(R.id.header_back).setOnClickListener(this);
        findViewById(R.id.header_option).setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        userId = Integer.parseInt(intent.getStringExtra("enlistId"));
        UserAction.getUserInfo(userId);
    }

    @Override
    public void OnDataArrived(User user) {
        enlister = user;
        if (user.getPhotos() == null || user.getPhotos().equals("")){
            mPhotoList = new ArrayList();
        }else{
            String[] urlArray = user.getPhotos().split("\\|");
            mPhotoList = new ArrayList<String>(Arrays.asList(urlArray));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPhotos();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.enlist_info_bt_chat:
                Intent intent = new Intent(this,ChatActivity.class);
                intent.putExtra("friendName", enlister.getUserName());
                intent.putExtra("friendId", enlister.getId());
                startActivity(intent);
                break;
        }
    }

    private void initPhotos(){
        enlit_info_id.setText(String.valueOf(enlister.getId()));
        enlit_info_age.setText(String.format(Locale.CHINA,"%d岁", enlister.getAge()));
        enlit_info_city.setText(enlister.getLocation());
        enlit_info_job.setText(enlister.getJob());
        enlit_info_name.setText(enlister.getUserName());
        ImageUtils.load(this, ApplicationData.SERVER_IP + userId + "/" + "0.jpg",enlist_info_photo);
        pb.setVisibility(View.GONE);
        adapter = new RecyclerViewAdapter(this,mPhotoList,userId);
        adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LayoutInflater inflater = LayoutInflater.from(EnlistInfoActivity.this);
                View bigPhoto = inflater.inflate(R.layout.dialog_big_photo,null);
                final AlertDialog dialog = new AlertDialog.Builder(EnlistInfoActivity.this).create();

                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;

                Glide.with(EnlistInfoActivity.this).load(ApplicationData.SERVER_IP + userId + "/" + mPhotoList.get(position) + ".jpg")
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
        rv_photos.setAdapter(adapter);
        rv_photos.setLayoutManager(new GridLayoutManager(this,4));
        rv_photos.addItemDecoration(new DividerGridItemDecoration(this));

        ((TextView) findViewById(R.id.header_title)).setText(enlister.getUserName());
    }
}
