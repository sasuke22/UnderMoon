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

public class Fragment_all_contributes extends BaseFragment {
    private View rootView;
    private ProgressBar mAll_PGBar;
    private ListView mLv_all_contributes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_contributes,container,false);
        mLv_all_contributes = (ListView) rootView.findViewById(R.id.lv_all_contributes);
        mAll_PGBar = (ProgressBar) rootView.findViewById(R.id.all_pgbar);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (rootView == null)
            return;
        if (isVisibleToUser) {
            setCurrentFragment(this);
            showDialogGetAllContributes();
            //            setResourceAndItemClick();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    public void showDialogGetAllContributes(){
        mAll_PGBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAction.getAllContributes(user.getId());
                synchronized (key){
                    try {
                        key.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("tag","send message ");
                mHandler.sendEmptyMessage(0);
//                setResourceAndItemClick();
                mAll_PGBar.setVisibility(View.GONE);
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


    //    private class AllContributesAdapter extends BaseAdapter {
//        private LayoutInflater mInflater;
//
//        public AllContributesAdapter(Context context) {
//            mInflater = LayoutInflater.from(context);
//        }
//
//        class ViewHolder {
//            TextView tv_today_contributes_province;
//            TextView tv_today_contributes_city;
//            TextView tv_today_contributes_describe;
//            TextView tv_today_contributes_date;
//        }
//
//        @Override
//        public int getCount() {
//            return 0;           //通过访问数据库获得信息
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.item_today_contributes, null);
//                holder = new ViewHolder();
//                holder.tv_today_contributes_province = (TextView) convertView.findViewById(R.id.tv_today_contributes_city);
//                holder.tv_today_contributes_city = (TextView) convertView.findViewById(R.id.tv_today_contributes_city);
//                holder.tv_today_contributes_describe = (TextView) convertView.findViewById(R.id.tv_today_contributes_describe);
//                holder.tv_today_contributes_date = (TextView) convertView.findViewById(R.id.tv_today_contributes_date);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            //TODO 给各个tv设置数据
//            return convertView;
//        }
//    }
}
