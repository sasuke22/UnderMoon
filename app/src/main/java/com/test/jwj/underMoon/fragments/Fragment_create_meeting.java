package com.test.jwj.underMoon.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.PlusImageActivity;
import com.test.jwj.underMoon.adapter.GridViewAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MainConstant;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.utils.PhotoUtils;
import com.test.jwj.underMoon.utils.VerifyUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

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
    private GridView gv_photos;
    private GridViewAdapter mGridViewAddImgAdapter;
    private ArrayList<String> mPicList = new ArrayList<>(); //上传的图片凭证的数据源

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

        gv_photos = (GridView) viewRoot.findViewById(R.id.create_meeting_photos);
        initPhotos();
    }

    private void initPhotos() {
        mGridViewAddImgAdapter = new GridViewAdapter(getActivity(), mPicList);
        gv_photos.setAdapter(mGridViewAddImgAdapter);
        gv_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                    if (mPicList.size() == 9) {
                        //最多添加9张图片
                        viewPluImg(position);
                    } else {
                        //添加凭证图片
                        selectPic(MainConstant.MAX_SELECT_PIC_NUM - mPicList.size());
                    }
                } else {
                    viewPluImg(position);
                }
            }
        });
    }

    //查看大图
    private void viewPluImg(int position) {
        Intent intent = new Intent(getActivity(), PlusImageActivity.class);
        intent.putStringArrayListExtra(MainConstant.IMG_LIST, mPicList);
        intent.putExtra(MainConstant.POSITION, position);
        startActivityForResult(intent, MainConstant.REQUEST_CODE_MAIN);
    }

    /**
     * 打开相册或者照相机选择凭证图片，最多5张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private void selectPic(int maxTotal) {
        PhotoUtils.initMultiConfig(getActivity(), maxTotal);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    refreshAdapter(PictureSelector.obtainMultipleResult(data));
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }
        if (requestCode == MainConstant.REQUEST_CODE_MAIN && resultCode == MainConstant.RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            ArrayList<String> toDeletePicList = data.getStringArrayListExtra(MainConstant.IMG_LIST); //要删除的图片的集合
            mPicList.clear();
            mPicList.addAll(toDeletePicList);
            mGridViewAddImgAdapter.notifyDataSetChanged();
        }
    }

    // 处理选择的照片的地址
    private void refreshAdapter(List<LocalMedia> picList) {
        for (LocalMedia localMedia : picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed()) {
                String compressPath = localMedia.getCompressPath(); //压缩后的图片路径
                mPicList.add(compressPath); //把图片添加到将要上传的图片数组中
                mGridViewAddImgAdapter.notifyDataSetChanged();
            }
        }
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
                for (String url : mPicList){
                    UserAction.uploadNewPic();
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
