package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.global.UnderMoonApplication;

/**
 * Created by Administrator on 2017/4/11.
 */

public class PersonalInfoActivity extends Activity {
    private UnderMoonApplication mUnderMoonApplication;
    GridView gv_personal_info_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        mUnderMoonApplication = (UnderMoonApplication) getApplication();
        initViews();

    }

    private void initViews() {
        gv_personal_info_photo = (GridView) findViewById(R.id.gv_personal_info_photo);
        if (mUnderMoonApplication.getUserGender() == 1){//女的
            gv_personal_info_photo.setVisibility(View.INVISIBLE);
        }else{
            gv_personal_info_photo.setVisibility(View.VISIBLE);
            //TODO 网络获取图片赋值gridView
        }
    }
}
