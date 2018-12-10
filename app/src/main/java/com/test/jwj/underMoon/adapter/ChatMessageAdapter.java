package com.test.jwj.underMoon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.ChatEntity;
import com.test.jwj.underMoon.utils.EmotionUtils;
import com.test.jwj.underMoon.utils.ImageUtils;
import com.test.jwj.underMoon.utils.SpanStringUtils;

import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
	private List<ChatEntity> chatEntities;
	private LayoutInflater   mInflater;
	private Context          mContext;

	public ChatMessageAdapter(Context context, List<ChatEntity> vector) {
		this.chatEntities = vector;
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LinearLayout leftLayout;
		RelativeLayout rightLayout;
		TextView leftMessageView;
		TextView rightMessageView;
		TextView timeView;
		ImageView leftPhotoView;
		ImageView rightPhotoView;
		view = mInflater.inflate(R.layout.chat_message_item_, null);
		ChatEntity chatEntity = chatEntities.get(position);
		leftLayout = (LinearLayout) view
				.findViewById(R.id.chat_friend_left_layout);
		rightLayout = (RelativeLayout) view
				.findViewById(R.id.chat_user_right_layout);
		timeView = (TextView) view.findViewById(R.id.message_time);
		leftPhotoView = (ImageView) view
				.findViewById(R.id.message_friend_userphoto);
		rightPhotoView = (ImageView) view
				.findViewById(R.id.message_user_userphoto);
		leftMessageView = (TextView) view.findViewById(R.id.friend_message);
		rightMessageView = (TextView) view.findViewById(R.id.user_message);

		timeView.setText(chatEntity.getSendTime());
		if (chatEntity.getMessageType() == ChatEntity.SEND) {
			rightLayout.setVisibility(View.VISIBLE);
			leftLayout.setVisibility(View.GONE);

			ImageUtils.load(mContext,ApplicationData.getInstance().getUserPhoto(),rightPhotoView);

			rightMessageView.setText(SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
							mContext, rightMessageView, chatEntity.getContent()));
		} else if (chatEntity.getMessageType() == ChatEntity.RECEIVE) {// 本身作为接收方
			leftLayout.setVisibility(View.VISIBLE);
			rightLayout.setVisibility(View.GONE);
			Bitmap photo = ApplicationData.getInstance().getFriendPhotoMap()
					.get(chatEntity.getSenderId());
			if (photo != null)
				leftPhotoView.setImageBitmap(photo);
			leftMessageView.setText(SpanStringUtils.getEmotionContent(EmotionUtils.EMOTION_CLASSIC_TYPE,
					mContext, leftMessageView, chatEntity.getContent()));
		}
		return view;
	}

	@Override
	public int getCount() {
		return chatEntities.size();
	}

	@Override
	public Object getItem(int position) {
		return chatEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
