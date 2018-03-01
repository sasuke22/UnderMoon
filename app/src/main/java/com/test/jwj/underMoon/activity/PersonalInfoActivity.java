package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.User;

/**
 * Created by Administrator on 2017/4/11.
 */

public class PersonalInfoActivity extends Activity implements View.OnClickListener {
    User user;
    GridView gv_personal_info_photo;
    private EditText mEt_wechat;
    private TextView mTv_userId;
    private EditText mEt_age;
    private EditText mEt_height;
    private EditText mEt_job;
    private EditText mEt_leixing;
    private EditText mEt_lovetype;
    private EditText mEt_marry;
    private EditText mEt_xingzuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = ApplicationData.getInstance().getUserInfo();
        setContentView(R.layout.activity_personal_info);
        initViews();

    }

    private void initViews() {
        gv_personal_info_photo = (GridView) findViewById(R.id.gv_personal_info_photo);
        mEt_wechat = (EditText)findViewById(R.id.personal_info_wechat);
        mTv_userId = (TextView) findViewById(R.id.personal_info_userId);
        mEt_age = (EditText) findViewById(R.id.personal_info_age);
        mEt_height = (EditText) findViewById(R.id.personal_info_height);
        mEt_job = (EditText) findViewById(R.id.personal_info_job);
        mEt_leixing = (EditText) findViewById(R.id.personal_info_leixing);
        mEt_lovetype = (EditText) findViewById(R.id.personal_info_lovetype);
        mEt_marry = (EditText) findViewById(R.id.personal_info_marry);
        mEt_xingzuo = (EditText) findViewById(R.id.personal_info_xingzuo);
        findViewById(R.id.personal_info_btnSave).setOnClickListener(this);
        if (user.getGender() == 0){//女的
            gv_personal_info_photo.setVisibility(View.INVISIBLE);
        }else{
            gv_personal_info_photo.setVisibility(View.VISIBLE);
            //TODO 网络获取图片赋值gridView
        }
    }

    @Override
    public void onClick(View v) {
        // TODO 保存个人信息到网上
        user.setAge(Integer.parseInt(mEt_age.getText().toString()));
        user.setHeight(Integer.parseInt(mEt_height.getText().toString()));
    }
}
