package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.PersonalInfoActivity;
import com.test.jwj.underMoon.activity.WomenPhotoActivity;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_personal_center extends BaseFragment implements View.OnClickListener {
    private boolean ismale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ismale = user.getGender() == 1;// 1为男
        View view = inflater.inflate(R.layout.fragment_personal_center,container,false);
        view.findViewById(R.id.rl_personal_info).setOnClickListener(this);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        View rl_vip_center = view.findViewById(R.id.rl_vip_center);
        View rl_women_photo = view.findViewById(R.id.rl_women_photo);
        if (ismale){
            rl_vip_center.setVisibility(View.VISIBLE);
            rl_women_photo.setVisibility(View.GONE);
            rl_women_photo.setOnClickListener(this);
        }else{
            rl_vip_center.setVisibility(View.GONE);
            rl_women_photo.setVisibility(View.VISIBLE);
            rl_vip_center.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_personal_info:
                Intent PersonalInfoIntent = new Intent(getActivity(), PersonalInfoActivity.class);
                //TODO 可能需要从上个界面传过来的数据，到底是哪个人等信息，然后传到下个界面进行网络访问获取具体数据
                startActivity(PersonalInfoIntent);
                break;
            case R.id.rl_vip_center:

                break;
            case R.id.rl_women_photo:
                Intent WomenIntent = new Intent(getActivity(), WomenPhotoActivity.class);
                //TODO 可能需要从上个界面传过来的数据，到底是哪个人等信息，然后传到下个界面进行网络访问获取具体数据
                startActivity(WomenIntent);
                break;
        }
    }

    @Override
    public void setResourceAndItemClick() {

    }
}
