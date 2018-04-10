package com.test.jwj.underMoon.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.ViewPagerAdapter;
import com.test.jwj.underMoon.fragments.Fragment_all_contributes;
import com.test.jwj.underMoon.fragments.Fragment_my_invitation;
import com.test.jwj.underMoon.fragments.Fragment_my_register;
import com.test.jwj.underMoon.fragments.Fragment_today_contributes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/25.
 */

public class GoMeetingActivity extends FragmentActivity implements View.OnClickListener {
    public Dialog mLoadingDialog;
    ViewPager                  mGo_meeting_pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_meeting);
        mLoadingDialog = new Dialog(this);
        mGo_meeting_pager = (ViewPager) findViewById(R.id.go_meeting_viewpager);
        initFragment();
        changeFragment(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.rb_today_contributes).setOnClickListener(this);
        findViewById(R.id.rb_all_contributes).setOnClickListener(this);
        findViewById(R.id.rb_my_register).setOnClickListener(this);
        findViewById(R.id.rb_my_invitation).setOnClickListener(this);
    }

    private void initFragment() {
        mGo_meeting_pager.setAdapter(new ViewPagerAdapter(GoMeetingActivity.this,getSupportFragmentManager(),new ArrayList<>(Arrays.asList(
                Fragment_today_contributes.class,
                Fragment_all_contributes.class,
                Fragment_my_register.class,
                Fragment_my_invitation.class
        ))));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_today_contributes:
                changeFragment(0);
                break;
            case R.id.rb_all_contributes:
                changeFragment(1);
                break;
            case R.id.rb_my_register:
                changeFragment(2);
                break;
            case R.id.rb_my_invitation:
                changeFragment(3);
                break;
        }
    }

    private void changeFragment(int index){
        mGo_meeting_pager.setCurrentItem(index);
    }

}
