package com.test.jwj.underMoon.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.MeetingDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */

public class BaseFragment extends Fragment {
    protected static List<MeetingDetail> mAllContributesList;
    protected void addToBackStack(Fragment fragment){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void setMeetingList(ArrayList meetingList){
        mAllContributesList = meetingList;
    }
//    protected void addToMeetingBackStack(Fragment fragment){
//        FragmentTransaction transaction=getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fl_meeting_content,fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}
