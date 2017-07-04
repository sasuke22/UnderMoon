package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.test.jwj.underMoon.PersonalInfoActivity;
import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_personal_center extends BaseFragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center,container,false);
        view.findViewById(R.id.rl_personal_info).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_personal_info:
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                //TODO 可能需要从上个界面传过来的数据，到底是哪个人，性别等信息，然后传到下个界面进行网络访问获取具体数据
                startActivity(intent);
                break;
        }
    }
}
