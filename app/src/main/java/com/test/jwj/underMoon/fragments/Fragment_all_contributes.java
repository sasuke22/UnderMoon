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

public class Fragment_all_contributes extends BaseFragment {
    private ListView mLv_all_contributes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_contributes,container,false);
        mLv_all_contributes = (ListView) view.findViewById(R.id.lv_all_contributes);
        mLv_all_contributes.setAdapter(new AllContributesAdapter(getActivity()));
        mLv_all_contributes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), InvitationDetailActivity.class));
            }
        });
        //TODO 获取数据库信息，展示消息
        return view;
    }

    private class AllContributesAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public AllContributesAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        class ViewHolder {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_today_contributes, null);
                holder = new ViewHolder();
                holder.tv_today_contributes_province = (TextView) convertView.findViewById(R.id.tv_today_contributes_province);
                holder.tv_today_contributes_city = (TextView) convertView.findViewById(R.id.tv_today_contributes_city);
                holder.tv_today_contributes_describe = (TextView) convertView.findViewById(R.id.tv_today_contributes_describe);
                holder.tv_today_contributes_date = (TextView) convertView.findViewById(R.id.tv_today_contributes_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //TODO 给各个tv设置数据
            return convertView;
        }
    }
}
