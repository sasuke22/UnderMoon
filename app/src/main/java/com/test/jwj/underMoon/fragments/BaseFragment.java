package com.test.jwj.underMoon.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */

public class BaseFragment extends Fragment {
    public Activity act;
    public static final Object key  = new Object();
    protected              User   user = ApplicationData.getInstance().getUserInfo();
    protected static List<MeetingDetail> mAllContributesList;
    public static Dialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        loadingDialog = new Dialog(getActivity());
        super.onCreate(savedInstanceState);
    }

    protected void addToBackStack(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void setMeetingList(ArrayList meetingList){
        mAllContributesList = meetingList;
        loadingDialog.dismiss();
        Log.e("tag","notify " + (Looper.getMainLooper() == Looper.myLooper()));
//        synchronized (key){
//            key.notify();
//        }
    }
}
