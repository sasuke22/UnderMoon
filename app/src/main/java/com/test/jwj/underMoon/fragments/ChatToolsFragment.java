package com.test.jwj.underMoon.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.jwj.underMoon.CustomView.PictureWithText;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.utils.PhotoUtils;

/**
 * Created by jiangweijin on 2018/12/11.
 */

public class ChatToolsFragment extends Fragment implements PictureWithText.LayoutClickListener {
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_chat_tools, null);
        initView();
        return mRootView;
    }

    private void initView() {
        ((PictureWithText)mRootView.findViewById(R.id.fragment_chat_send_pic)).setLayoutClickListener(this);
    }

    @Override
    public void onLayoutClick(View view) {
        switch (view.getId()){
            case R.id.fragment_chat_send_pic:
                PhotoUtils.initMultiConfig(getActivity(), 1);
                break;
        }
    }
}
