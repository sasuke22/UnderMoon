package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.test.jwj.underMoon.CustomView.ItemLayout;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.PersonalInfoActivity;
import com.test.jwj.underMoon.activity.RestMoneyActivity;
import com.test.jwj.underMoon.activity.WomenPhotoActivity;
import com.test.jwj.underMoon.bean.ApplicationData;

import java.util.Locale;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_personal_center extends BaseFragment implements ItemLayout.LayoutClickListener {
    private boolean ismale;
    private ImageView headPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (user != null)
            ismale = user.getGender() == 1;// 1为男
        View rootView = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        ((ItemLayout)view.findViewById(R.id.rl_personal_info)).setLayoutClickListener(this);
        ItemLayout rl_vip_center = (ItemLayout)view.findViewById(R.id.rl_vip_center);
        ((ItemLayout)view.findViewById(R.id.rl_women_photo)).setLayoutClickListener(this);
        if (ismale){
            rl_vip_center.setVisibility(View.VISIBLE);
            rl_vip_center.setLayoutClickListener(this);
            ((TextView)view.findViewById(R.id.score)).setText(String.valueOf(user.getScore()));
        }else{
            rl_vip_center.setVisibility(View.GONE);
            view.findViewById(R.id.score).setVisibility(View.GONE);
        }
        ((TextView)view.findViewById(R.id.name)).setText(user.getUserName());
        ((TextView)view.findViewById(R.id.age)).setText(String.format(Locale.CHINA,"%d 岁",user.getAge()));
        ((TextView)view.findViewById(R.id.id)).setText(String.valueOf(user.getId()));
        ((TextView)view.findViewById(R.id.province)).setText(user.getLocation());
        ((TextView)view.findViewById(R.id.job)).setText(user.getJob());
        headPhoto = (ImageView)view.findViewById(R.id.head_photo);
        RequestOptions options = new RequestOptions().centerCrop().signature(new ObjectKey(System.currentTimeMillis()));
        Glide.with(this).asBitmap().load(ApplicationData.SERVER_IP + user.getId() + "/0.jpg").apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                ApplicationData.getInstance().setmUserHead(resource);
                headPhoto.setImageBitmap(resource);
            }
        });
    }

    @Override
    public void setResourceAndItemClick() {

    }

    @Override
    public void onLayoutClick(View view) {
        switch (view.getId()){
            case R.id.rl_personal_info:
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_vip_center:
                intent = new Intent(getActivity(), RestMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_women_photo:
                intent = new Intent(getActivity(), WomenPhotoActivity.class);
                intent.putExtra("id",user.getId());
                startActivity(intent);
                break;
        }
    }
}
