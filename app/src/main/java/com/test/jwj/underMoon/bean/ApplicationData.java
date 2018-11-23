package com.test.jwj.underMoon.bean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.test.jwj.underMoon.activity.BaseActivity;
import com.test.jwj.underMoon.activity.LoginActivity;
import com.test.jwj.underMoon.database.ImDB;
import com.test.jwj.underMoon.global.Result;
import com.test.jwj.underMoon.global.UnderMoonApplication;
import com.test.jwj.underMoon.utils.PhotoUtils;
import com.test.jwj.underMoon.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationData {

	private static ApplicationData mInitData;

	private       User                           mUser;
	private       boolean                        mIsReceived;
	private       List<User>                     mFriendList;
	private       TranObject                     mReceivedMessage;
	private       Map<Integer, Bitmap>           mFriendPhotoMap;
	private       Handler                        messageHandler;
	private       Handler                        chatMessageHandler;
	private       Handler                        friendListHandler;
	private       Context                        mContext;
	private       List<User>                     mFriendSearched;
	private       Bitmap                         mUserPhoto;
	private       List<MessageTabEntity>         mMessageEntities;// messageFragment显示的列表
	private       Map<Integer, List<ChatEntity>> mChatMessagesMap;
	private       SharedPreferences              sp;
	private       int                            score;
	public static UnderMoonApplication           mApplication;
	private static ArrayList<Activity> mActivityList = new ArrayList<>();
	public static final int CAMERA = 1;
	public static final int GALLERY = 2;
	public static final int CROP = 3;
	public static final String SERVER_IP = "http://192.168.107.99:8089/";

	//TODO 这里的所有数据需要在init方法中进行网络更新，以便后台添加游戏
	public String[] online_game = {"LOL","王者荣耀","绝地求生"};
	public String[] online_happy = {"音乐","占星","乐器指导"};
	public String[] online_voice = {"虚拟恋人","叫醒","声音鉴定","连麦哄睡","情感咨询"};
	public String[] offline_game = {"线下LOL指导","线下王者荣耀指导","线下绝地求生指导"};
	public String[] offline_happy = {"线下娱乐"};

	public Map<Integer, List<ChatEntity>> getChatMessagesMap() {
		return mChatMessagesMap;
	}

	public void setChatMessagesMap(
			Map<Integer, List<ChatEntity>> mChatMessagesMap) {
		this.mChatMessagesMap = mChatMessagesMap;
	}

	public static ApplicationData getInstance() {
		if (mInitData == null) {
			mInitData = new ApplicationData();
		}
		return mInitData;
	}

	private ApplicationData() {

	}

	public void start() {
		while (!(mIsReceived))
			;
	}

	public void loginMessageArrived(Object tranObject) {

		mReceivedMessage = (TranObject) tranObject;
		Result loginResult = mReceivedMessage.getResult();
		if (loginResult == Result.LOGIN_SUCCESS) {
			mUser = (User) mReceivedMessage.getObject();
			((UnderMoonApplication)mContext.getApplicationContext()).setUser(mUser);
			mFriendList = mUser.getFriendList();// 根据从服务器得到的信息，设置好友是否在线
			mUserPhoto = PhotoUtils.getBitmap(mUser.getPhoto());
			List<User> friendListLocal = ImDB.getInstance(mContext)
					.getAllFriend();
			mFriendPhotoMap = new HashMap<Integer, Bitmap>();
			for (int i = 0; i < friendListLocal.size(); i++) {
				User friend = friendListLocal.get(i);
				Bitmap photo = PhotoUtils.getBitmap(friend.getPhoto());
				mFriendPhotoMap.put(friend.getId(), photo);
			}
			mMessageEntities = ImDB.getInstance(mContext).getAllMessage();
			sp = SpUtil.getSharePreference(mContext);
			score = mUser.getScore();
			SpUtil.setIntSharedPreference(sp,"score",score);
		} else {
			Log.e("tag","login message arrived");
			mUser = null;
			mFriendList = null;
		}
		mChatMessagesMap = new HashMap<Integer, List<ChatEntity>>();
		mIsReceived = true;
	}

	public Map<Integer, Bitmap> getFriendPhotoMap() {
		return mFriendPhotoMap;
	}

	public void setFriendPhotoList(Map<Integer, Bitmap> mFriendPhotoMap) {
		this.mFriendPhotoMap = mFriendPhotoMap;
	}

	public User getUserInfo() {
		return mUser;
	}

	public List<User> getFriendList() {
		return mFriendList;
	}

	public void initData(Context comtext) {
		mApplication = (UnderMoonApplication) comtext.getApplicationContext();
		mContext = comtext;
		mIsReceived = false;
		mFriendList = null;
		mUser = null;
		mReceivedMessage = null;
	}

	public TranObject getReceivedMessage() {
		return mReceivedMessage;
	}

	public void setReceivedMessage(TranObject mReceivedMessage) {
		this.mReceivedMessage = mReceivedMessage;
	}

	public List<User> getFriendSearched() {
		return mFriendSearched;
	}

	public void setFriendSearched(List<User> mFriendSearched) {
		this.mFriendSearched = mFriendSearched;
	}

	public void friendRequestArrived(TranObject mReceivedRequest) {
		MessageTabEntity messageEntity = new MessageTabEntity();
		if (mReceivedRequest.getResult() == Result.MAKE_FRIEND_REQUEST) {
			messageEntity.setMessageType(MessageTabEntity.MAKE_FRIEND_REQUEST);
			messageEntity.setContent("希望加你为好友");
		} else if (mReceivedRequest.getResult() == Result.FRIEND_REQUEST_RESPONSE_ACCEPT) {
			messageEntity
					.setMessageType(MessageTabEntity.MAKE_FRIEND_RESPONSE_ACCEPT);
			messageEntity.setContent("接受了你的好友请求");
			User newFriend = (User) mReceivedRequest.getObject();
			if (!mFriendList.contains(newFriend)) {

				mFriendList.add(newFriend);
			}
			
			mFriendPhotoMap.put(newFriend.getId(),
					PhotoUtils.getBitmap(newFriend.getPhoto()));
			if (friendListHandler != null) {
				Message message = new Message();
				message.what = 1;
				friendListHandler.sendMessage(message);
			}
			ImDB.getInstance(mContext).saveFriend(newFriend);
		} else {
			messageEntity
					.setMessageType(MessageTabEntity.MAKE_FRIEND_RESPONSE_REJECT);
			messageEntity.setContent("拒绝了你的好友请求");
		}
		messageEntity.setName(mReceivedRequest.getSendName());
		messageEntity.setSendTime(mReceivedRequest.getSendTime());
		messageEntity.setSenderId(mReceivedRequest.getSendId());
		messageEntity.setUnReadCount(1);
		ImDB.getInstance(mContext).saveMessage(messageEntity);
		mMessageEntities.add(messageEntity);
		if (messageHandler != null) {
			Message message = new Message();
			message.what = 1;
			messageHandler.sendMessage(message);
		}
	}

	public void messageArrived(TranObject tran) {
		ChatEntity chat = (ChatEntity) tran.getObject();
		int senderId = chat.getSenderId();
		boolean hasMessageTab = false;
		for (int i = 0; i < mMessageEntities.size(); i++) {
			MessageTabEntity messageTab = mMessageEntities.get(i);
			if (messageTab.getSenderId() == senderId
					&& messageTab.getMessageType() == MessageTabEntity.FRIEND_MESSAGE) {
				messageTab.setUnReadCount(messageTab.getUnReadCount() + 1);
				messageTab.setContent(chat.getContent());
				messageTab.setSendTime(chat.getSendTime());
				ImDB.getInstance(mContext).updateMessages(messageTab);
				hasMessageTab = true;
			}
		}
		if (!hasMessageTab) {
			MessageTabEntity messageTab = new MessageTabEntity();
			messageTab.setContent(chat.getContent());
			messageTab.setMessageType(MessageTabEntity.FRIEND_MESSAGE);
			messageTab.setName(tran.getSendName());
			messageTab.setSenderId(senderId);
			messageTab.setSendTime(chat.getSendTime());
			messageTab.setUnReadCount(1);
			mMessageEntities.add(messageTab);
			ImDB.getInstance(mContext).saveMessage(messageTab);
		}
		chat.setMessageType(ChatEntity.RECEIVE);
		List<ChatEntity> chatList = mChatMessagesMap.get(chat.getSenderId());
		if (chatList == null) {
			chatList = ImDB.getInstance(mContext).getChatMessage(
					chat.getSenderId());
			getChatMessagesMap().put(chat.getSenderId(), chatList);
		}
		chatList.add(chat);
		ImDB.getInstance(mContext).saveChatMessage(chat);
		if (messageHandler != null) {
			Message message = new Message();
			message.what = 1;
			messageHandler.sendMessage(message);
		}
		if (chatMessageHandler != null) {
			Message message = new Message();
			message.what = 1;
			chatMessageHandler.sendMessage(message);
		}
	}

	public Bitmap getUserPhoto() {
		return mUserPhoto;
	}

	public void setUserPhoto(Bitmap mUserPhoto) {
		this.mUserPhoto = mUserPhoto;
	}

	public List<MessageTabEntity> getMessageEntities() {
		return mMessageEntities;
	}

	public void setMessageEntities(List<MessageTabEntity> mMessageEntities) {
		this.mMessageEntities = mMessageEntities;
	}

	public void setMessageHandler(Handler handler) {
		this.messageHandler = handler;
	}

	public void setChatHandler(Handler handler) {
		this.chatMessageHandler = handler;
	}

	public void setfriendListHandler(Handler handler) {
		this.friendListHandler = handler;
	}

	public static<T extends BaseActivity> void addActivity(T t){
		mActivityList.add(t);
	}

	public static <T extends BaseActivity> void removeActivity(T t){
		mActivityList.remove(t);
	}

	public static void finishAll(){
		for (Activity activity : mActivityList){
			if (!activity.isFinishing() && !(activity instanceof LoginActivity))
				activity.finish();
		}
		Intent intent = new Intent(mApplication,LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mApplication.startActivity(intent);
	}
}
