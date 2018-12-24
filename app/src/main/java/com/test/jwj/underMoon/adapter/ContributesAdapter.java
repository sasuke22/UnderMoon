package com.test.jwj.underMoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.MeetingDetail;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class ContributesAdapter extends BaseAdapter {
    private List<MeetingDetail> mMeetingDetailList;
    private LayoutInflater mInflater;
    private Context mContext;

    public ContributesAdapter(Context context, List<MeetingDetail> meetingDetailList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mMeetingDetailList = meetingDetailList;
    }

    class ViewHolder {
        public TextView city;
        public TextView summary;
        public TextView date;
//        public TextView read;
        public TextView approve;
    }

    public int getCount() {
        return mMeetingDetailList == null ? 0 : mMeetingDetailList.size();
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
        MeetingDetail meetingDetail = mMeetingDetailList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_today_contributes, parent,false);
            holder = new ViewHolder();
            holder.city = (TextView) convertView.findViewById(R.id.tv_today_contributes_city);
            holder.summary = (TextView) convertView.findViewById(R.id.tv_today_contributes_describe);
            holder.date = (TextView) convertView.findViewById(R.id.tv_today_contributes_date);
//            holder.read = (TextView) convertView.findViewById(R.id.tv_today_contributes_read);
            holder.approve = (TextView) convertView.findViewById(R.id.tv_today_contributes_approve);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.city.setText(meetingDetail.city);
        holder.summary.setText(meetingDetail.summary);
        holder.date.setText(meetingDetail.date.toString());
//        holder.read.setText(meetingDetail.read ? "已读" : "未读");
        switch (meetingDetail.approve){
            case 1:
                holder.approve.setText("审核通过");
                holder.approve.setTextColor(mContext.getResources().getColor(R.color.limegreen));
                break;
            case 0:
                holder.approve.setText("审核通过");
                holder.approve.setTextColor(mContext.getResources().getColor(R.color.yellow));
                break;
            case -1:
                holder.approve.setText("审核不通过");
                break;
        }
        return convertView;
    }
}
