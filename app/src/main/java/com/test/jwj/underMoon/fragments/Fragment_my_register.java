package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.InvitationDetailActivity;
import com.test.jwj.underMoon.adapter.ContributesAdapter;
import com.test.jwj.underMoon.bean.MeetingDetail;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25.
 */

public class Fragment_my_register extends BaseFragment {
    private List<MeetingDetail> mMyRegisterList;
    ListView mLv_my_register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_register,container,false);
        mLv_my_register = (ListView) view.findViewById(R.id.lv_my_register);
        //TODO 获取网络数据给list赋值
        mLv_my_register.setAdapter(new ContributesAdapter(getActivity(),mMyRegisterList));
        mLv_my_register.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingDetail detailItem = mMyRegisterList.get(position);
                Intent intent = new Intent(getActivity(), InvitationDetailActivity.class);
                intent.putExtra("chat",true);
                intent.putExtra("id",detailItem.id);
                intent.putExtra("meetingId",detailItem.meetingId);
                startActivity(intent);
            }
        });
        return view;
    }
}
