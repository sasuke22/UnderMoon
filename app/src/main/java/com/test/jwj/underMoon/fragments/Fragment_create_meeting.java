package com.test.jwj.underMoon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;

/**
 * Created by Administrator on 2017/3/18.
 */

public class Fragment_create_meeting extends BaseFragment implements View.OnClickListener {
    private View     viewRoot;
    private EditText Et_city_create_meeting;
    private EditText Et_date_create_meeting;
    private EditText Et_content_create_meeting;
    private EditText Et_lovetype_create_meeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_create_meeting,container,false);
        initView();
        return viewRoot;
    }

    private void initView() {
        viewRoot.findViewById(R.id.upload_meeting).setOnClickListener(this);
        Et_city_create_meeting = (EditText) viewRoot.findViewById(R.id.city_create_meeting);
        Et_date_create_meeting = (EditText) viewRoot.findViewById(R.id.date_create_meeting);
        Et_content_create_meeting = (EditText) viewRoot.findViewById(R.id.et_create_content);
        Et_lovetype_create_meeting = (EditText) viewRoot.findViewById(R.id.et_create_lovetype);
    }

    @Override
    public void onClick(View v) {
        User user = ApplicationData.getInstance().getUserInfo();
        MeetingDetail meetingDetail = new MeetingDetail();
        meetingDetail.id = user.getId();
        meetingDetail.city = String.valueOf(Et_city_create_meeting.getText());
        meetingDetail.date = String.valueOf(Et_date_create_meeting.getText());
        meetingDetail.content = String.valueOf(Et_content_create_meeting.getText());
        meetingDetail.loveType = String.valueOf(Et_lovetype_create_meeting.getText());
        meetingDetail.age = user.getAge();
        meetingDetail.marry = user.getMarry();
        meetingDetail.height = user.getHeight();
        meetingDetail.job = user.getJob();
        meetingDetail.figure = user.getFigure();
        meetingDetail.xingzuo = user.getXingzuo();
        UserAction.addContribute(meetingDetail);
    }
}
