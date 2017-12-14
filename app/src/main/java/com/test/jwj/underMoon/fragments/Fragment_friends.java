package com.test.jwj.underMoon.fragments;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.FriendsAdapter;
import com.test.jwj.underMoon.bean.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class Fragment_friends extends BaseFragment implements View.OnClickListener {
    private List<Friends> mFriendsList = new ArrayList<>();              //TODO 网络获取的朋友列表
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,container,false);
        ListView lv_friends = (ListView) view.findViewById(R.id.lv_friendslist);
        view.findViewById(R.id.tv_all_friends).setOnClickListener(this);
        view.findViewById(R.id.tv_new_friends).setOnClickListener(this);
        // TODO 通过网络请求获取到列表赋值给Friends
        Friends lily = new Friends(1,1,"Lily","23","单身",167,"匀称","design","天秤","单身","苗条","上海 上海",new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)));
        mFriendsList.add(lily);
        lv_friends.setAdapter(new FriendsAdapter(getActivity(),mFriendsList));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_all_friends:
                //TODO 应该是获取网络数据刷新列表
                break;
            case R.id.tv_new_friends:
                //TODO 应该是获取网络数据刷新列表
                break;
        }
    }


    //    class MyAdapter extends BaseAdapter{
//        private LayoutInflater mInflater;
//        private Friends friend;
//
//        MyAdapter(Context context){
//            mInflater = LayoutInflater.from(context);
//        }
//        class ViewHolder {
//            TextView tv_name;
//            TextView tv_age;
//            TextView tv_marry;
//            TextView tv_figure;
//            TextView tv_city;
//            ImageView iv_touxiang;
//        }
//        public int getCount() {
//            return friends.length;
//        }
//
//        public Object getItem(int item) {
//            return item;
//        }
//
//        public long getItemId(int id) {
//            return id;
//        }
//
//        //创建View方法
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            friend = friends[position];
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.item_friends, null);
//                holder = new ViewHolder();
//                holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
//                holder.iv_touxiang = (ImageView) convertView.findViewById(R.id.iv_touxiang);
//                holder.tv_age = (TextView)convertView.findViewById(R.id.tv_age);
//                holder.tv_city = (TextView)convertView.findViewById(R.id.tv_city);
//                holder.tv_figure = (TextView)convertView.findViewById(R.id.tv_figure);
//                holder.tv_marry = (TextView)convertView.findViewById(R.id.tv_marry);
//                convertView.setTag(holder);
//            }else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            //TODO 获取网络数据加载到listview
//            holder.tv_name.setText(friend.name);
//            holder.iv_touxiang.setImageDrawable(friend.touxiang);
//            holder.tv_age .setText(friend.age);
//            holder.tv_city .setText(friend.city);
//            holder.tv_figure.setText(friend.figure);
//            holder.tv_marry.setText(friend.marry);
//            return convertView;
//        }
//    }
}
