package com.test.jwj.underMoon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.User;

import java.util.List;
import java.util.Map;

public class FriendListAdapter extends BaseAdapter {
	private List<User>     mFriendList;
	private LayoutInflater mInflater;
	private Context		   mContext;
	private ImageView 	   avatarView;
	private User 		   user;
	private Map<Integer, Bitmap> friendPhotoMap;

	public FriendListAdapter(Context context, List<User> vector) {
		this.mContext = context;
		this.mFriendList = vector;
		mInflater = LayoutInflater.from(context);
		friendPhotoMap = ApplicationData.getInstance().getFriendPhotoMap();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		TextView nameView;
		ImageView isOnline;
		TextView introView;
		user = mFriendList.get(position);
		convertView = mInflater.inflate(R.layout.friend_list_item,null);
		avatarView = (ImageView) convertView.findViewById(R.id.user_photo);
		Glide.with(mContext).asBitmap().load(ApplicationData.SERVER_IP + user.getId() + "/0.jpg").into(new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
				avatarView.setImageBitmap(resource);
				Log.e("tag","frimap " + friendPhotoMap.size() + ",userid " + user.getId());
				friendPhotoMap.put(user.getId(),resource);
				ApplicationData.getInstance().setFriendPhotoMap(friendPhotoMap);
			}
		});
		String name = user.getUserName();
		String briefIntro = user.getUserBriefIntro();


		nameView = (TextView) convertView.findViewById(R.id.friend_list_name);
		isOnline = (ImageView)convertView.findViewById(R.id.stateicon);
		
		introView = (TextView) convertView.findViewById(R.id.friend_list_brief);
		
		nameView.setText(name);
		
		if(!user.isOnline()) {
			isOnline.setVisibility(View.GONE);
			
		}
		introView.setText(briefIntro);
	

		return convertView;
	}

	public int getCount() {
		return mFriendList == null ? 0 : mFriendList.size();
	}

	public Object getItem(int position) {
		return mFriendList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}
