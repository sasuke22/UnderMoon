package com.test.jwj.underMoon.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.GoMeetingActivity;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.network.IMessageArrived;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment implements IMessageArrived<ArrayList<MeetingDetail>> {
    public GoMeetingActivity act;
    protected User   user = ApplicationData.getInstance().getUserInfo();
    protected static ArrayList<MeetingDetail> mAllContributesList;
    public static Handler mHandler;
    private static BaseFragment mCurrentFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mHandler = new MyHandler();
        super.onActivityCreated(savedInstanceState);
    }

    protected void addToBackStack(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content,fragment,fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void setMeetingList(ArrayList<MeetingDetail> meetingList){
        mAllContributesList = meetingList;
        mCurrentFragment.setResourceAndItemClick();
    }

    public abstract void setResourceAndItemClick();

    public static class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mCurrentFragment.setResourceAndItemClick();
            }
        }
    }

    public void setCurrentFragment(BaseFragment fragment){
        mCurrentFragment = fragment;
    }

    @Override
    public void OnDataArrived(ArrayList<MeetingDetail> list) {

    }
}
