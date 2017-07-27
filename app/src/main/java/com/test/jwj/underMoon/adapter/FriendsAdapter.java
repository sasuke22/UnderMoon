package com.test.jwj.underMoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.javabean.Friends;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class FriendsAdapter extends BaseAdapter {
    private List<Friends> mFriendsList;
    private LayoutInflater mInflater;
    private Friends friend;

    public FriendsAdapter(Context context,List<Friends> list){
        mInflater = LayoutInflater.from(context);
        this.mFriendsList = list;
    }
    class ViewHolder {
        TextView  tv_name;
        TextView  tv_age;
        TextView  tv_marry;
        TextView  tv_figure;
        TextView  tv_city;
        ImageView iv_touxiang;
    }
    public int getCount() {
        return mFriendsList.size();
    }

    public Object getItem(int item) {
        return item;
    }

    public long getItemId(int id) {
        return id;
    }

    //创建View方法
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        friend = mFriendsList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_friends, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            holder.iv_touxiang = (ImageView) convertView.findViewById(R.id.iv_touxiang);
            holder.tv_age = (TextView)convertView.findViewById(R.id.tv_age);
            holder.tv_city = (TextView)convertView.findViewById(R.id.tv_city);
            holder.tv_figure = (TextView)convertView.findViewById(R.id.tv_figure);
            holder.tv_marry = (TextView)convertView.findViewById(R.id.tv_marry);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //TODO 获取网络数据加载到listview
        holder.tv_name.setText(friend.name);
        holder.iv_touxiang.setImageDrawable(friend.touxiang);
        holder.tv_age .setText(friend.age);
        holder.tv_city .setText(friend.city);
        holder.tv_figure.setText(friend.figure);
        holder.tv_marry.setText(friend.marry);
        return convertView;
    }
}
