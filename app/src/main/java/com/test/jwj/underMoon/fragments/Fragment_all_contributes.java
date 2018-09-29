package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Created by Administrator on 2017/3/25.
 */

public class Fragment_all_contributes extends BaseFragment {
    private View rootView;
    private ListView mLv_all_contributes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_contributes,container,false);
        mLv_all_contributes = (ListView) rootView.findViewById(R.id.lv_all_contributes);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            setCurrentFragment(this);
            showDialogGetAllContributes();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void showDialogGetAllContributes(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAction.getAllContributes(user.getId());
                synchronized (key){
                    try {
                        key.wait();
                        Log.e("tag","all wait");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((GoMeetingActivity)getActivity()).mBar.setVisibility(View.GONE);
                    }
                });

            }
        }).start();

    }

    @Override
    public void setResourceAndItemClick() {
        mLv_all_contributes.setAdapter(new ContributesAdapter(act,mAllContributesList));
        mLv_all_contributes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
}
