package com.test.jwj.underMoon.global;

import android.util.Log;

import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.ChatEntity;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.network.NetService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class UserAction {
	private static NetService mNetService = NetService.getInstance();

	public static void accountVerify(String account) throws IOException {

		TranObject t = new TranObject(account, TranObjectType.REGISTER_ACCOUNT);
		mNetService.send(t);

	}

	public static void register(User user) throws IOException {

		TranObject t = new TranObject(user, TranObjectType.REGISTER);
		mNetService.send(t);

	}

	public static void loginVerify(User user) throws IOException {
		TranObject t = new TranObject(user, TranObjectType.LOGIN);
		mNetService.send(t);
	}

	public static void searchFriend(String message) throws IOException {
		TranObject t = new TranObject(message, TranObjectType.SEARCH_FRIEND);
		mNetService.send(t);

	}

	public static void sendFriendRequest(Result result, Integer id) {
		TranObject t = new TranObject();
		t.setReceiveId(id);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
		String sendTime = sdf.format(date);
		t.setSendTime(sendTime);
		User user = ApplicationData.getInstance().getUserInfo();
		t.setResult(result);
		t.setSendId(user.getId());
		t.setTranType(TranObjectType.FRIEND_REQUEST);
		t.setSendName(user.getUserName());
		try {
			mNetService.send(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendMessage(ChatEntity message) {

		TranObject t = new TranObject();
		t.setTranType(TranObjectType.MESSAGE);
		t.setReceiveId(message.getReceiverId());
		t.setSendName(ApplicationData.getInstance().getUserInfo().getUserName());
		t.setObject(message);
		try {
			mNetService.send(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getAllContributes(int userid) {
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.ALL_CONTRIBUTES);
		t.setObject(userid);
		try {
			mNetService.send(t);
			Log.d("tag","send request contributes success" + userid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getMyContributes(int userid){
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.MY_CONTRIBUTES);
		t.setSendId(userid);
		try {
			mNetService.send(t);
			Log.e("tag","send request my contributes success" + userid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getTodayContributes(int id, java.sql.Date curDate) {
		HashMap<Integer, java.sql.Date> map = new HashMap<>();
		map.put(id,curDate);
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.TODAY_CONTRIBUTES);
		t.setObject(map);
		try {
			mNetService.send(t);
			Log.d("tag","send request contributes success" + id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getInvitationDetail(int meetingId) {
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.INVITATION_DETAIL);
		t.setObject(meetingId);
		try {
			mNetService.send(t);
			Log.e("tag","send request contributes success" + meetingId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addContribute(MeetingDetail meetingDetail){
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.ADD_CONTRIBUTE);
		t.setObject(meetingDetail);
		try {
			mNetService.send(t);
			Log.d("tag","send add contributes success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void enlist(int meetingId,int userId){
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.ENLIST);
		t.setObject(meetingId);
		t.setSendId(userId);
		try {
			mNetService.send(t);
			Log.d("tag","send request enlist success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveUserInfo(User user) {
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.SAVE_USER_INFO);
		t.setObject(user);
		try {
			mNetService.send(t);
			Log.d("tag","send request save success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getEnlist() {
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.GET_ENLIST);
		try {
			mNetService.send(t);
			Log.d("tag","send get enlist success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getEnlistName(int meetingId) {
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.GET_ENLIST_NAME);
		try {
			mNetService.send(t);
			Log.d("tag","send get enlist success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getUserInfo(int userId) {
		TranObject t = new TranObject();
		t.setObject(userId);
		t.setTranType(TranObjectType.GET_USER_INFO);
		try {
			mNetService.send(t);
			Log.d("tag","send get user info success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
