package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.PersonalInfoActivity;
import com.test.jwj.underMoon.activity.WomenPhotoActivity;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.utils.PhotoUtils;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_personal_center extends BaseFragment implements View.OnClickListener {
    private boolean ismale;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user = ApplicationData.getInstance().getUserInfo();
        ismale = user.getGender() == 1;// 1为男
        rootView = inflater.inflate(R.layout.fragment_personal_center,container,false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        rootView.findViewById(R.id.rl_personal_info).setOnClickListener(this);
        View rl_vip_center = view.findViewById(R.id.rl_vip_center);
        view.findViewById(R.id.rl_women_photo).setOnClickListener(this);
        if (ismale){
            rl_vip_center.setVisibility(View.VISIBLE);
        }else{
            rl_vip_center.setVisibility(View.GONE);
        }
        ((TextView)view.findViewById(R.id.name)).setText(user.getUserName());
        ((TextView)view.findViewById(R.id.age)).setText(user.getAge() + "岁");
        ((TextView)view.findViewById(R.id.id)).setText(String.valueOf(user.getId()));
        ((TextView)view.findViewById(R.id.province)).setText(user.getLocation());
        ((TextView)view.findViewById(R.id.job)).setText(user.getJob());
        ((ImageView)view.findViewById(R.id.head_photo)).setImageBitmap(PhotoUtils.getBitmap(user.getPhoto()));
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
                WomenIntent.putExtra("id",user.getId());
                startActivity(WomenIntent);
                break;
        }
    }

    @Override
    public void setResourceAndItemClick() {

    }
}
