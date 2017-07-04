package com.test.jwj.underMoon.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/3/18.
 */

public class BaseFragment extends Fragment {
    protected void addToBackStack(Fragment fragment){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
//    protected void addToMeetingBackStack(Fragment fragment){
//        FragmentTransaction transaction=getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fl_meeting_content,fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}
