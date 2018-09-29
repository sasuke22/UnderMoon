package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.jwj.underMoon.CustomView.ItemLayout;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.PersonalInfoActivity;
import com.test.jwj.underMoon.activity.RestMoneyActivity;
import com.test.jwj.underMoon.activity.WomenPhotoActivity;
import com.test.jwj.underMoon.utils.PhotoUtils;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_personal_center extends BaseFragment implements ItemLayout.LayoutClickListener {
    private boolean ismale;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ismale = user.getGender() == 1;// 1为男
        rootView = inflater.inflate(R.layout.fragment_personal_center,container,false);
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
        ((TextView)view.findViewById(R.id.age)).setText(user.getAge() + "岁");
        ((TextView)view.findViewById(R.id.id)).setText(String.valueOf(user.getId()));
        ((TextView)view.findViewById(R.id.province)).setText(user.getLocation());
        ((TextView)view.findViewById(R.id.job)).setText(user.getJob());
        ((ImageView)view.findViewById(R.id.head_photo)).setImageBitmap(PhotoUtils.getBitmap(user.getPhoto()));
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
