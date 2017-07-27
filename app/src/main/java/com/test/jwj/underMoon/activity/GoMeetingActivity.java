package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.fragments.Fragment_all_contributes;
import com.test.jwj.underMoon.fragments.Fragment_my_invitation;
import com.test.jwj.underMoon.fragments.Fragment_my_register;
import com.test.jwj.underMoon.fragments.Fragment_today_contributes;
import com.test.jwj.underMoon.javabean.MeetingDetail;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25.
 */

public class GoMeetingActivity extends Activity implements View.OnClickListener {
    private List<MeetingDetail> mMeetingDetailList;
//    ListView meetingList;
    Fragment_today_contributes mFragmentTodayContributes;
    Fragment_all_contributes mFragmentAllContributes;
    Fragment_my_register mFragmentMyRegister;
    Fragment_my_invitation mFragmentMyInvitation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_meeting);
        findViewById(R.id.rb_today_contributes).setOnClickListener(this);
        findViewById(R.id.rb_all_contributes).setOnClickListener(this);
        findViewById(R.id.rb_my_register).setOnClickListener(this);
        findViewById(R.id.rb_my_invitation).setOnClickListener(this);
//        meetingList = (ListView) findViewById(R.id.lv_meeting_list);
//        meetingList.setAdapter(new MyAdapter(this));
        // TODO 获取网络数据将mMeetingDetailList初始化
        initFragment();
        changeFragment(mFragmentTodayContributes);
    }

    private void initFragment() {
        mFragmentTodayContributes = new Fragment_today_contributes();
        mFragmentAllContributes = new Fragment_all_contributes();
        mFragmentMyRegister = new Fragment_my_register();
        mFragmentMyInvitation = new Fragment_my_invitation();
    }

    //TODO 这里的点击用来获取列表数据进行网络刷新
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_today_contributes:
                changeFragment(mFragmentTodayContributes);
                break;
            case R.id.rb_all_contributes:
                changeFragment(mFragmentAllContributes);
//                meetingList.invalidate();
                break;
            case R.id.rb_my_register:
                changeFragment(mFragmentMyRegister);
//                meetingList.invalidate();
                break;
            case R.id.rb_my_invitation:
                changeFragment(mFragmentMyInvitation);
//                meetingList.invalidate();
                break;
        }
    }

//    class MyAdapter extends BaseAdapter {
//        private LayoutInflater mInflater;
//        private MeetingDetail meetingDetail;
//
//        MyAdapter(Context context) {
//            mInflater = LayoutInflater.from(context);
//        }
//
//        class ViewHolder {
//            public TextView city;
//            public TextView summary;
//            public TextView date;
//            public TextView read;
//            public TextView approve;
//        }
//
//        public int getCount() {
//            return mMeetingDetailList.size();
//        }
//
//        public Object getItem(int item) {
//            return item;
//        }
//
//        public long getItemId(int id) {
//            return id;
//        }
//
//        //创建View方法
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            meetingDetail = mMeetingDetailList.get(position);
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.item_today_contributes, null);
//                holder = new ViewHolder();
//                holder.city = (TextView) convertView.findViewById(R.id.tv_city);
//                holder.summary = (TextView) convertView.findViewById(R.id.tv_today_contributes_describe);
//                holder.date = (TextView) convertView.findViewById(R.id.tv_today_contributes_date);
//                holder.read = (TextView) convertView.findViewById(R.id.tv_today_contributes_read);
//                holder.approve = (TextView) convertView.findViewById(R.id.tv_today_contributes_approve);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            //TODO 获取网络数据加载到listview
//            holder.city.setText(meetingDetail.city);
//            holder.summary.setText(meetingDetail.summary);
//            holder.date.setText(meetingDetail.date);
//            holder.read.setText(meetingDetail.read ? "已读" : "未读");
//            holder.approve.setText(meetingDetail.approve ? "对方已通过你的报名" : "对方未通过你的报名");
//            return convertView;
//        }
//    }

    private void changeFragment(Fragment frag){
        FragmentManager supportFragmentManager=getFragmentManager();
        FragmentTransaction transaction=supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_meeting_content,frag);
        transaction.commit();
    }
}
