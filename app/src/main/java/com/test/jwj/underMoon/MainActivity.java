package com.test.jwj.underMoon;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.test.jwj.underMoon.fragments.Fragment_personal_center;
import com.test.jwj.underMoon.fragments.Fragment_friends;
import com.test.jwj.underMoon.fragments.Fragment_bapa;

public class MainActivity extends Activity implements View.OnClickListener {
    FragmentTransaction fragmentTransaction;
    Fragment mFragmentBapa;
    Fragment mFragmentFriends;
    Fragment mFragmentpersonal_center;
//    Fragment mFragmentSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout fl_content = (FrameLayout) findViewById(R.id.fl_content);
        findViewById(R.id.rb_personal_center).setOnClickListener(this);
        findViewById(R.id.rb_friends).setOnClickListener(this);
        findViewById(R.id.rb_bapa).setOnClickListener(this);
//        findViewById(R.id.rb_settings).setOnClickListener(this);
        initFragment();
        changeFragment(mFragmentBapa);
    }

    private void initFragment() {
        mFragmentBapa = new Fragment_bapa();
        mFragmentFriends = new Fragment_friends();
        mFragmentpersonal_center = new Fragment_personal_center();
//        mFragmentSettings = new Fragment_settings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_bapa:
                changeFragment(mFragmentBapa);
                break;
            case R.id.rb_friends:
                changeFragment(mFragmentFriends);
                break;
            case R.id.rb_personal_center:
                changeFragment(mFragmentpersonal_center);
                break;
//            case R.id.rb_settings:
//                changeFragment(mFragmentSettings);
//                break;
        }
    }

    private void changeFragment(Fragment frag){

        FragmentManager supportFragmentManager=getFragmentManager();
        FragmentTransaction transaction=supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content,frag);
        transaction.commit();
    }
}
