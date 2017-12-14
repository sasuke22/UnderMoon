package com.test.jwj.underMoon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_settings extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        return view;
    }

}
