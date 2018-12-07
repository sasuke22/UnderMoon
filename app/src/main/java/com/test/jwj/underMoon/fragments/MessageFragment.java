package com.test.jwj.underMoon.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.test.jwj.underMoon.CustomView.BaseDialog;
import com.test.jwj.underMoon.CustomView.SlideCutListView;
import com.test.jwj.underMoon.CustomView.TitleBarView;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.ChatActivity;
import com.test.jwj.underMoon.adapter.FriendMessageAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MessageTabEntity;
import com.test.jwj.underMoon.database.ImDB;
import com.test.jwj.underMoon.global.Result;
import com.test.jwj.underMoon.global.UserAction;

import java.util.List;

public class MessageFragment extends Fragment implements SlideCutListView.RemoveListener {
	private Context                mContext;
	private View                   mBaseView;
	private TitleBarView           mTitleBarView;
	private List<MessageTabEntity> mMessageEntityList;
	private SlideCutListView       mMessageListView;
	private FriendMessageAdapter   adapter;
	private BaseDialog             mDialog;
	private int                    mPosition;
	private MessageTabEntity       chooseMessageEntity;
	private boolean				   firstEnter = true;//第一次初始化这个fragment。给后来每次从chatactivity回来的时候执行onresume做标志位

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_message, null);
		findView();
		init();
		firstEnter = false;
		return mBaseView;
	}

	@Override
	public void onResume() {
		if (!firstEnter){//每次从chatactivity回来都需要重新刷新下数据
			mMessageEntityList = ApplicationData.getInstance().getMessageEntities();
			adapter.setMessageEntities(mMessageEntityList);
		}
		super.onResume();
	}

	private void findView() {
		mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mMessageListView = (SlideCutListView) mBaseView.findViewById(R.id.message_list_listview);
	}

	private void init() {
		mMessageListView.setRemoveListener(this);
		initDialog();
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						adapter.notifyDataSetChanged();
						mMessageListView.setSelection(mMessageEntityList.size());
						break;
					default:
						break;
				}
			}
		};
		ApplicationData.getInstance().setMessageHandler(handler);
		mMessageEntityList = ApplicationData.getInstance().getMessageEntities();
		mMessageListView.setSelection(mMessageEntityList.size());
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE);
		mTitleBarView.setTitleText("消息");
		adapter = new FriendMessageAdapter(mContext, mMessageEntityList);
		mMessageListView.setAdapter(adapter);
		mMessageListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						chooseMessageEntity = mMessageEntityList.get(position);
						chooseMessageEntity.setUnReadCount(0);
						adapter.notifyDataSetChanged();
						ImDB.getInstance(mContext).updateMessages(chooseMessageEntity);
						mPosition = position;
						if (chooseMessageEntity.getMessageType() == MessageTabEntity.MAKE_FRIEND_REQUEST)
							mDialog.show();
						else if (chooseMessageEntity.getMessageType() == MessageTabEntity.MAKE_FRIEND_RESPONSE_ACCEPT) {
							
						}else {
							Intent intent = new Intent(mContext,ChatActivity.class);
							intent.putExtra("friendName", chooseMessageEntity.getName());
							intent.putExtra("friendId", chooseMessageEntity.getSenderId());
							startActivity(intent);
						}
					}
				});
	}



	private void initDialog() {
		mDialog = BaseDialog.getDialog(mContext, "是否接受好友请求?", "", "接受",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						UserAction.sendFriendRequest(
								Result.FRIEND_REQUEST_RESPONSE_ACCEPT,
								chooseMessageEntity.getSenderId());
						mMessageEntityList.remove(mPosition);
						ImDB.getInstance(mContext).deleteMessage(
								chooseMessageEntity);

					}
				}, "拒绝", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						UserAction.sendFriendRequest(
								Result.FRIEND_REQUEST_RESPONSE_REJECT,
								chooseMessageEntity.getSenderId());
						mMessageEntityList.remove(mPosition);
						ImDB.getInstance(mContext).deleteMessage(
								chooseMessageEntity);
						adapter.notifyDataSetChanged();
					}
				});
		mDialog.setButton1Background(R.drawable.btn_default_popsubmit);
	}

	// 滑动删除之后的回调方法
	@Override
	public void removeItem(SlideCutListView.RemoveDirection direction, int position) {
		MessageTabEntity temp = mMessageEntityList.get(position);
		mMessageEntityList.remove(position);
		adapter.notifyDataSetChanged();
		switch (direction) {
		default:
			ImDB.getInstance(mContext).deleteMessage(temp);
			break;
		}

	}
}
