package com.test.jwj.underMoon.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test.jwj.underMoon.InvitationDetailActivity;
import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/3/25.
 */

public class Fragment_today_contributes extends BaseFragment {
    private ListView mLv_today_contributes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_contributes,container,false);
        mLv_today_contributes = (ListView) view.findViewById(R.id.lv_today_contributes);
        mLv_today_contributes.setAdapter(new TodayContributesAdapter(getActivity()));
        mLv_today_contributes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), InvitationDetailActivity.class));
            }
        });
        return view;
    }

    private class TodayContributesAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        public TodayContributesAdapter(Context context){
            mInflater = LayoutInflater.from(context);
        }
        class ViewHolder{
            TextView tv_today_contributes_province;
            TextView tv_today_contributes_city;
            TextView tv_today_contributes_describe;
            TextView tv_today_contributes_date;
        }
        @Override
        public int getCount() {
            return 0;           //通过访问数据库获得信息
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null){
                convertView = mInflater.inflate(R.layout.item_today_contributes,null);
                holder = new ViewHolder();
                holder.tv_today_contributes_province = (TextView) convertView.findViewById(R.id.tv_today_contributes_province);
                holder.tv_today_contributes_city = (TextView) convertView.findViewById(R.id.tv_today_contributes_city);
                holder.tv_today_contributes_describe = (TextView) convertView.findViewById(R.id.tv_today_contributes_describe);
                holder.tv_today_contributes_date = (TextView) convertView.findViewById(R.id.tv_today_contributes_date);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            //TODO 给各个tv设置数据
            return convertView;
        }
    }
}
