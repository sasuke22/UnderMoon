package com.test.jwj.underMoon.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MessageTabEntity;
import com.test.jwj.underMoon.utils.ImageUtils;

import java.util.List;

public class FriendMessageAdapter extends BaseAdapter {
	private List<MessageTabEntity> mMessageEntities;
	private LayoutInflater         mInflater;
	private Context                mContext0;

	public FriendMessageAdapter(Context context, List<MessageTabEntity> vector) {
		this.mMessageEntities = vector;
		mInflater = LayoutInflater.from(context);
		mContext0 = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		ImageView avatarView;
		TextView nameView;
		TextView unReadCountView;
		TextView sendTimeView;
		TextView contentView;
		MessageTabEntity message = mMessageEntities.get(position);
		Integer senderId = message.getSenderId();
		String name = message.getName();
		int messageType = message.getMessageType();
		String sendTime = message.getSendTime();
		int unReadCount = message.getUnReadCount();
		String content = message.getContent();
		convertView = mInflater.inflate(R.layout.fragment_message_item, null);
		avatarView = (ImageView) convertView.findViewById(R.id.user_photo);
		nameView = (TextView) convertView.findViewById(R.id.user_name);
		contentView = (TextView) convertView.findViewById(R.id.user_message);
		unReadCountView = (TextView) convertView.findViewById(R.id.unread_message_count);
		ImageView unReadBack = (ImageView) convertView.findViewById(R.id.unread_message_back);
		sendTimeView = (TextView) convertView.findViewById(R.id.send_time);
		if (unReadCount == 0) {
			unReadCountView.setVisibility(View.GONE);
			unReadBack.setVisibility(View.GONE);
		} else if (unReadCount > 9) {
			unReadCountView.setText("9+");
		} else {
			unReadCountView.setText(String.valueOf(message.getUnReadCount()));
		}
		ImageUtils.load(mContext0,ApplicationData.SERVER_IP + senderId + "/0.jpg",avatarView);
		nameView.setText(name);
		sendTimeView.setText(sendTime);

		contentView.setText(content);

		return convertView;
	}

	public int getCount() {
		return mMessageEntities.size();
	}

	public Object getItem(int position) {
		return mMessageEntities.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void setMessageEntities(List<MessageTabEntity> messageEntities) {
		mMessageEntities = messageEntities;
		notifyDataSetChanged();
	}

}
