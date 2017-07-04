package com.test.jwj.underMoon.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.test.jwj.underMoon.Friends;
import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_friends extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,container,false);
        ListView lv_friends = (ListView) view.findViewById(R.id.lv_friends);
        lv_friends.setAdapter(new MyAdapter(getActivity()));
        return view;
    }

    class MyAdapter extends BaseAdapter{
        private Context context;
        private Friends[] friends;              //网络获取的朋友列表

        MyAdapter(Context context) {
            this.context = context;
        }
        public int getCount() {
            return friends.length;
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

            }else {

            }

        }
    }
}
