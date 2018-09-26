package com.test.jwj.underMoon.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
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

public class GoMeetingActivity extends FragmentActivity implements View.OnClickListener, OnTabSelectListener, ViewPager.OnPageChangeListener {
    public  Dialog           mLoadingDialog;
    private ViewPager        mGo_meeting_pager;
    public  ProgressBar      mBar;
    private SegmentTabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_meeting);
        mLoadingDialog = new Dialog(this);
        mGo_meeting_pager = (ViewPager) findViewById(R.id.go_meeting_viewpager);
        mBar = (ProgressBar) findViewById(R.id.meetings_bar);
        initFragment();
        changeFragment(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] tabs = {"今日投稿", "所有投稿", "我的报名", "我的邀约"};

        tablayout = ((SegmentTabLayout) findViewById(R.id.go_meeting_tab));
        tablayout.setTabData(tabs);
        tablayout.setOnTabSelectListener(this);
        mGo_meeting_pager.addOnPageChangeListener(this);
    }

    private void initFragment() {
        mGo_meeting_pager.setAdapter(new ViewPagerAdapter(GoMeetingActivity.this, getSupportFragmentManager(), new ArrayList<>(Arrays.asList(
                Fragment_today_contributes.class,
                Fragment_all_contributes.class,
                Fragment_my_register.class,
                Fragment_my_invitation.class
        ))));
    }

    @Override
    public void onClick(View v) {
        mBar.setVisibility(View.VISIBLE);
    }

    private void changeFragment(int index) {
        mGo_meeting_pager.setCurrentItem(index);
    }

    @Override
    public void onTabSelect(int position) {
        changeFragment(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tablayout.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
