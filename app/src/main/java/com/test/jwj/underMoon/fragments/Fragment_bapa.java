package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.GoMeetingActivity;
import com.test.jwj.underMoon.activity.SearchActivity;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_bapa extends BaseFragment implements View.OnClickListener {
    Fragment mFragment_create_meeting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bapa,container,false);
        initView(view);
        initFragment();
        return view;
    }

    private void initFragment() {
        mFragment_create_meeting = new Fragment_create_meeting();
    }

    private void initView(View view) {
        view.findViewById(R.id.zhudongyue).setOnClickListener(this);
        view.findViewById(R.id.fuyue).setOnClickListener(this);
        view.findViewById(R.id.search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhudongyue:
                addToBackStack(mFragment_create_meeting);
                break;
            case R.id.fuyue:
                startActivity(new Intent(getActivity(),GoMeetingActivity.class));
                break;
            case R.id.search:
                startActivity(new Intent(getActivity(),SearchActivity.class));
                break;
        }
    }

}
