package com.test.jwj.underMoon;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.test.jwj.underMoon.fragments.Fragment_all_contributes;
import com.test.jwj.underMoon.fragments.Fragment_my_invitation;
import com.test.jwj.underMoon.fragments.Fragment_my_register;
import com.test.jwj.underMoon.fragments.Fragment_today_contributes;

/**
 * Created by Administrator on 2017/3/25.
 */

public class GoMeetingActivity extends Activity implements View.OnClickListener {
    Fragment mFragmentTodayContributes;
    Fragment mFragmentAllContributes;
    Fragment mFragmentMyRegister;
    Fragment mFragmentMyInvitation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_meeting);
        findViewById(R.id.rb_today_contributes).setOnClickListener(this);
        findViewById(R.id.rb_all_contributes).setOnClickListener(this);
        findViewById(R.id.rb_my_register).setOnClickListener(this);
        findViewById(R.id.rb_my_invitation).setOnClickListener(this);
        initFragment();
        changeFragment(mFragmentTodayContributes);
    }

    private void initFragment() {
        mFragmentTodayContributes = new Fragment_today_contributes();
        mFragmentAllContributes = new Fragment_all_contributes();
        mFragmentMyRegister = new Fragment_my_register();
        mFragmentMyInvitation = new Fragment_my_invitation();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_today_contributes:
                changeFragment(mFragmentTodayContributes);
                break;
            case R.id.rb_all_contributes:
                changeFragment(mFragmentAllContributes);
                break;
            case R.id.rb_my_register:
                changeFragment(mFragmentMyRegister);
                break;
            case R.id.rb_my_invitation:
                changeFragment(mFragmentMyInvitation);
                break;
        }
    }

    private void changeFragment(Fragment frag){

        FragmentManager supportFragmentManager=getFragmentManager();
        FragmentTransaction transaction=supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_meeting_content,frag);
        transaction.commit();
    }
}
