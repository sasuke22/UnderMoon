package com.test.jwj.underMoon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.InvitationDetailActivity;
import com.test.jwj.underMoon.adapter.ContributesAdapter;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.global.UserAction;

/**
 * Created by Administrator on 2017/3/25.
 */

public class Fragment_my_register extends BaseFragment {
    private View rootView;
    private ProgressBar mMyRegi_pgbar;
    ListView mLv_my_register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_register,container,false);
        mLv_my_register = (ListView) rootView.findViewById(R.id.lv_my_register);
        mMyRegi_pgbar = (ProgressBar)rootView.findViewById(R.id.myRegi_pgbar);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (rootView == null)
            return;
        if (isVisibleToUser){
            setCurrentFragment(this);
            showDialogGetMyContributes();
//            setResourceAndItemClick();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void showDialogGetMyContributes() {
        mMyRegi_pgbar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("tag","id1 " + user.getId());
                UserAction.getEnlist();
                synchronized (key){
                    try {
                        key.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(0);
//                setResourceAndItemClick();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMyRegi_pgbar.setVisibility(View.GONE);
                    }
                });

            }
        }).start();

    }

    @Override
    public void setResourceAndItemClick() {
        mLv_my_register.setAdapter(new ContributesAdapter(act,mAllContributesList));
        mLv_my_register.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingDetail detailItem = mAllContributesList.get(position);
                Intent intent = new Intent(getActivity(), InvitationDetailActivity.class);
//                intent.putExtra("chat",true);
//                intent.putExtra("id",detailItem.id);
                intent.putExtra("meetingId",detailItem.meetingId);
                startActivity(intent);
            }
        });
    }

}
