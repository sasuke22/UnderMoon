package com.test.jwj.underMoon.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.MeetingDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */

public class BaseFragment extends Fragment {
    public static final Object key = new Object();
    protected static List<MeetingDetail> mAllContributesList;
    public static Dialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        loadingDialog = new Dialog(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void addToBackStack(Fragment fragment){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void setMeetingList(ArrayList meetingList){
        mAllContributesList = meetingList;
        loadingDialog.dismiss();
        synchronized (key){
            key.notify();
        }
    }
//    protected void addToMeetingBackStack(Fragment fragment){
//        FragmentTransaction transaction=getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fl_meeting_content,fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}
