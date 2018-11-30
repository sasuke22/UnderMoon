package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.GoMeetingActivity;
import com.test.jwj.underMoon.activity.InvitationDetailActivity;
import com.test.jwj.underMoon.adapter.ContributesAdapter;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.global.UserAction;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/25.
 */

public class Fragment_today_contributes extends BaseFragment{
    private ListView mLv_today_contributes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today_contributes, container, false);
        mLv_today_contributes = (ListView) rootView.findViewById(R.id.lv_today_contributes);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVleToUser) {
        if (isVleToUser) {
            setCurrentFragment(this);
            UserAction.setMiDataListener(this);
            showDialogGetTodayContributes();
        }
        super.setUserVisibleHint(isVleToUser);

    }

    private void showDialogGetTodayContributes() {
        UserAction.getTodayContributes(user.getId());
    }

    @Override
    public void setResourceAndItemClick() {
        mLv_today_contributes.setAdapter(new ContributesAdapter(act,mAllContributesList));
        mLv_today_contributes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingDetail detailItem = mAllContributesList.get(position);
                Intent intent = new Intent(getActivity(), InvitationDetailActivity.class);
//                intent.putExtra("chat",true);
                intent.putExtra("id",detailItem.id);
                intent.putExtra("meetingId",detailItem.meetingId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnDataArrived(final ArrayList<MeetingDetail> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((GoMeetingActivity)getActivity()).mBar.setVisibility(View.GONE);
                setMeetingList(list);
            }
        });
    }
}
