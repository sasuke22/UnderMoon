package com.test.jwj.underMoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.test.jwj.underMoon.CustomView.TitleBarView;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.ChatMessageAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.ChatEntity;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.database.ImDB;
import com.test.jwj.underMoon.fragments.EmotionMainFragment;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.utils.SpUtil;

import java.io.Serializable;
import java.util.List;


public class ChatActivity extends BaseActivity {
	private TitleBarView       mTitleBarView;
	private int                friendId;
	private String             friendName;
	private ListView           chatMeessageListView;
	private ChatMessageAdapter chatMessageAdapter;
	private List<ChatEntity>   chatList;
	private Handler            handler;
	private User               mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		mUser = ApplicationData.getInstance().getUserInfo();
		Intent intent = getIntent();
		friendName = intent.getStringExtra("friendName");
		friendId = intent.getIntExtra("friendId", 0);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE);
		mTitleBarView.setTitleText("与" + friendName + "对话");
		chatMeessageListView = (ListView) findViewById(R.id.chat_Listview);
		initEmotionMainFragment();
	}

	@Override
	protected void initEvents() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					chatMessageAdapter.notifyDataSetChanged();
					chatMeessageListView.setSelection(chatList.size());
					break;
				default:
					break;
				}
			}
		};
		ApplicationData.getInstance().setChatHandler(handler);
		chatList = ApplicationData.getInstance().getChatMessagesMap()
				.get(friendId);
		if(chatList == null){
			chatList = ImDB.getInstance(ChatActivity.this).getChatMessage(friendId);
			ApplicationData.getInstance().getChatMessagesMap().put(friendId, chatList);
		}
		chatMessageAdapter = new ChatMessageAdapter(ChatActivity.this,chatList);
		chatMeessageListView.setAdapter(chatMessageAdapter);
		chatMeessageListView.setSelection(chatList.size());
//		sendButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				SharedPreferences sp = SpUtil.getSharePreference(ChatActivity.this);
//				int score = SpUtil.getSPScore(sp);
//				if (score <= 0)
//					showCustomToast("您剩余的积分不足，请及时充值");
//				else {
//					String content = inputEdit.getText().toString();
//					inputEdit.setText("");
//					ChatEntity chatMessage = new ChatEntity();
//					chatMessage.setContent(content);
//					chatMessage.setSenderId(ApplicationData.getInstance()
//							.getUserInfo().getId());
//					chatMessage.setReceiverId(friendId);
//					chatMessage.setMessageType(ChatEntity.SEND);
//					Date date = new Date();
//					SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
//					String sendTime = sdf.format(date);
//					chatMessage.setSendTime(sendTime);
//					chatList.add(chatMessage);
//					chatMessageAdapter.notifyDataSetChanged();
//					chatMeessageListView.setSelection(chatList.size());
//					UserAction.sendMessage(chatMessage);
//					ImDB.getInstance(ChatActivity.this)
//							.saveChatMessage(chatMessage);
//					if (mUser.getGender() == 1) {
//						SpUtil.setIntSharedPreference(sp, "score", score - 1);
//						mUser.setScore(score - 1);
//					}
//				}
//			}
//		});
	}

	@Override
	protected void onPause() {
		if (mUser.getGender() == 1) {
			UserAction.updateScore(mUser.getId(), SpUtil.getSPScore(SpUtil.getSharePreference(this)));
		}
		super.onPause();
	}

	/**
	 * 初始化表情面板
	 */
	public void initEmotionMainFragment(){
		//构建传递参数
		Bundle bundle = new Bundle();
		//绑定主内容编辑框
		bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT,true);
		//隐藏控件
		bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN,false);

		bundle.putInt("userID",mUser.getId());
		bundle.putInt("friendID",friendId);
		//替换fragment
		//创建修改实例
		EmotionMainFragment emotionMainFragment = new EmotionMainFragment();
		bundle.putSerializable("callback",new MsgCallback() {
			@Override
			public void onMsgCallback(ChatEntity chatMsg) {
				chatList.add(chatMsg);
				chatMessageAdapter.notifyDataSetChanged();
				chatMeessageListView.setSelection(chatList.size());
			}
		});
		emotionMainFragment.setArguments(bundle);
		emotionMainFragment.bindToContentView(chatMeessageListView);
		FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
		// Replace whatever is in thefragment_container view with this fragment,
		// and add the transaction to the backstack
		transaction.replace(R.id.fl_activity_chat_emotion,emotionMainFragment);
		//        transaction.addToBackStack(null);
		//提交修改
		transaction.commit();
	}

	public interface MsgCallback extends Serializable{
		void onMsgCallback(ChatEntity chatMsg);
	}

}
