package com.test.jwj.underMoon.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.utils.VerifyUtils;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/3/18.
 */

public class Fragment_create_meeting extends BaseFragment implements View.OnClickListener,DatePicker.OnDateChangedListener {
    private View     viewRoot;
    private EditText Et_city_create_meeting;
    private TextView Tv_date_create_meeting;
    private EditText Et_content_create_meeting;
    private EditText Et_lovetype_create_meeting;
    private int year, month, day;
    private StringBuffer date = new StringBuffer("");
    private Date meetingDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_create_meeting,container,false);
        initView();
        return viewRoot;
    }

    private void initView() {
        viewRoot.findViewById(R.id.upload_meeting).setOnClickListener(this);
        Et_city_create_meeting = (EditText) viewRoot.findViewById(R.id.city_create_meeting);
        Tv_date_create_meeting = (TextView) viewRoot.findViewById(R.id.date_create_meeting);
        Tv_date_create_meeting.setOnClickListener(this);
        Et_content_create_meeting = (EditText) viewRoot.findViewById(R.id.et_create_content);
        Et_lovetype_create_meeting = (EditText) viewRoot.findViewById(R.id.et_create_lovetype);
        ((TextView) viewRoot.findViewById(R.id.header_title)).setText("主动邀约");
        viewRoot.findViewById(R.id.header_option).setVisibility(View.GONE);
        viewRoot.findViewById(R.id.header_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:
                getActivity().onBackPressed();
                break;
            case R.id.date_create_meeting:
                initDateTime();
                ChooseDate();
                break;
            case R.id.upload_meeting:
                if (VerifyUtils.isNull(Et_city_create_meeting) || Tv_date_create_meeting.getText().toString().trim().length() <= 0 || VerifyUtils.isNull(Et_content_create_meeting)
                        || VerifyUtils.isNull(Et_lovetype_create_meeting)){
                    Toast.makeText(getActivity(),"请输入完整信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = ApplicationData.getInstance().getUserInfo();
                if (user.getGender() == 1 && user.getScore() < 100)
                    Toast.makeText(getActivity(),"您剩余的积分不足，请及时充值",Toast.LENGTH_SHORT).show();
                else {
                    MeetingDetail meetingDetail = new MeetingDetail();
                    meetingDetail.id = user.getId();
                    meetingDetail.city = String.valueOf(Et_city_create_meeting.getText());
                    meetingDetail.date = meetingDate;
                    meetingDetail.content = String.valueOf(Et_content_create_meeting.getText());
                    meetingDetail.type = ApplicationData.getInstance().getUserInfo().getFigure();
                    meetingDetail.loveType = String.valueOf(Et_lovetype_create_meeting.getText());
                    meetingDetail.age = user.getAge();
                    meetingDetail.marry = user.getMarry();
                    meetingDetail.height = user.getHeight();
                    meetingDetail.job = user.getJob();
                    meetingDetail.figure = user.getFigure();
                    meetingDetail.xingzuo = user.getXingzuo();
                    meetingDetail.score = user.getScore();
                    UserAction.addContribute(meetingDetail);
                    getActivity().onBackPressed();
                }
                break;
        }

    }

    private void ChooseDate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date != null) { //清除上次记录的日期
                    date.delete(0, date.length());
                }
                Tv_date_create_meeting.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(getActivity(), R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month - 1, day, this);
    }

    @Override
    public void setResourceAndItemClick() {

    }

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        meetingDate = new Date(calendar.getTime().getTime());
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        Calendar meetingCalendar = Calendar.getInstance();
        meetingCalendar.set(year,monthOfYear,dayOfMonth);
        meetingDate = new java.sql.Date(meetingCalendar.getTime().getTime());
    }
}
