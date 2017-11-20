package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.jwj.underMoon.activity.InvitationDetailActivity;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.ContributesAdapter;
import com.test.jwj.underMoon.bean.MeetingDetail;

import java.util.List;

/**
 * Created by Administrator on 2017/3/25.
 */

public class Fragment_my_invitation extends BaseFragment {
    private List<MeetingDetail> mMyInvitationList;
    ListView mLv_my_invitation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_invitation,container,false);
        mLv_my_invitation = (ListView) view.findViewById(R.id.lv_my_invitation);
        //TODO 获取网络数据给list赋值
        mLv_my_invitation.setAdapter(new ContributesAdapter(getActivity(),mMyInvitationList));
        mLv_my_invitation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), InvitationDetailActivity.class));
            }
        });
        return view;
    }
}
