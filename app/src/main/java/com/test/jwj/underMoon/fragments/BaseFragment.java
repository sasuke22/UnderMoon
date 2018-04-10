package com.test.jwj.underMoon.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.GoMeetingActivity;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.User;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/18.
 */

public abstract class BaseFragment extends Fragment {
    public GoMeetingActivity act;
    public static final Object key  = new Object();
    protected              User   user = ApplicationData.getInstance().getUserInfo();
    protected static ArrayList<MeetingDetail> mAllContributesList;
    public static Dialog loadingDialog;
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
        transaction.replace(R.id.fl_content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void setMeetingList(ArrayList meetingList){
        mAllContributesList = meetingList;
//        loadingDialog.dismiss();
        Log.e("tag","notify " + (Looper.getMainLooper() == Looper.myLooper()));
        synchronized (key){
            key.notify();
        }
    }

    public abstract void setResourceAndItemClick();

    public class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            Log.e("tag","receive message");
            if (msg.what == 0) {
                Log.e("tag","handle " + mCurrentFragment);
                mCurrentFragment.setResourceAndItemClick();
//                loadingDialog.dismiss();
            }
        }
    }

    public void setCurrentFragment(BaseFragment fragment){
        this.mCurrentFragment = fragment;
    }
}
