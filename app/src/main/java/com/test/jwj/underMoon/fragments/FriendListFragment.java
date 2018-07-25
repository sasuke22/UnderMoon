package com.test.jwj.underMoon.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.jwj.underMoon.CustomView.TitleBarView;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.ChatActivity;
import com.test.jwj.underMoon.activity.SearchFriendActivity;
import com.test.jwj.underMoon.adapter.FriendListAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.User;

import java.util.List;

public class FriendListFragment extends Fragment {
	private Context           mContext;
	private View              mBaseView;
	private TitleBarView      mTitleBarView;
	private ListView          mFriendListView;
	private List<User>        mFriendList;
	private Handler           handler;
	private FriendListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_friendlist, null);
		findView();
		init();
		return mBaseView;
	}

	private void findView() {
		mTitleBarView = (TitleBarView) mBaseView.findViewById(R.id.title_bar);
		mFriendListView = (ListView)mBaseView.findViewById(R.id.friend_list_listview);
		
	}

	private void init() {
		mFriendList = ApplicationData.getInstance().getFriendList();
		adapter = new FriendListAdapter(mContext, mFriendList);
		mFriendListView.setAdapter(adapter);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					mFriendListView.setSelection(mFriendList.size());
					break;
				default:
					break;
				}
			}
		};
		ApplicationData.getInstance().setfriendListHandler(handler);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.VISIBLE);
		mTitleBarView.setTitleText("好友");
		mTitleBarView.setBtnLeft(R.string.control);
		mTitleBarView.setBtnRight(R.drawable.qq_constact);
		
		mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User friend = mFriendList.get(position);
				Intent intent = new Intent(mContext,ChatActivity.class);
				intent.putExtra("friendName", friend.getUserName());
				intent.putExtra("friendId", friend.getId());
				startActivity(intent);
			}
		});
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,SearchFriendActivity.class);
				startActivity(intent);
			}
		});
		
	}
}
