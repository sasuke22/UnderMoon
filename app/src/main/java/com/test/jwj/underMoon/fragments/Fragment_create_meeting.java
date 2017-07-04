package com.test.jwj.underMoon.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/3/18.
 */

public class Fragment_create_meeting extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_meeting,container,false);
        return view;
    }
}
