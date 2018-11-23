package com.test.jwj.underMoon.global;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.ChatEntity;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.network.IMessageArrived;
import com.test.jwj.underMoon.network.NetService;
import com.test.jwj.underMoon.utils.OkhttpUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class UserAction {
	private static NetService mNetService = NetService.getInstance();
	private static Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private static IMessageArrived miDataListener;
	public static void setMiDataListener(IMessageArrived listener) {
		miDataListener = listener;
	}

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
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss",Locale.CHINA);
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
			Log.e("tag","send request contributes success" + userid);
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
			Log.e("tag","send request contributes success" + id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getInvitationDetail(int meetingId) {
		HashMap<String,String> params = new HashMap<>();
		params.put("meetingid",String.valueOf(meetingId));
		OkhttpUtil.get("invitationdetail",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				ApplicationData.mApplication.mBinder.AlertToast("服务器未知错误，请重试，请联系大佐");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()) {
					MeetingDetail meetingDetail = mGson.fromJson(response.body().string(), MeetingDetail.class);
					if (miDataListener != null)
						miDataListener.OnDataArrived(meetingDetail);
				}
			}
		});
	}

	public static void addContribute(MeetingDetail meetingDetail, java.sql.Date date, ArrayList<String> picList){
		String meetingDetailJson = new Gson().toJson(meetingDetail);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		HashMap<String,String> params = new HashMap<>();
		params.put("meetingDetail",meetingDetailJson);
		params.put("meetingDate",sdf.format(date));
		OkhttpUtil.post("createmeeting",params,picList).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				ApplicationData.mApplication.mBinder.AlertToast("服务器未知错误，请重试，请联系大佐");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()) {
					String string = response.body().string();
					if (string.equals("-1"))
						ApplicationData.mApplication.mBinder.AlertToast("上传邀约失败，请重试");
					else
						ApplicationData.mApplication.mBinder.AlertToast("上传邀约成功");
				}
			}
		});
	}

	public static void enlist(int meetingId,int userId,String enlisterName){
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.ENLIST);
		t.setObject(meetingId);
		t.setSendId(userId);
		t.setSendName(enlisterName);
		try {
			mNetService.send(t);
			Log.e("tag","send request enlist success");
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
			Log.e("tag","send request save success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getEnlist() {
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.GET_ENLIST);
		try {
			mNetService.send(t);
			Log.e("tag","send get enlist success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getEnlistName(int meetingId) {
		TranObject t = new TranObject();
		t.setTranType(TranObjectType.GET_ENLIST_NAME);
		t.setObject(meetingId);
		try {
			mNetService.send(t);
			Log.e("tag","send get enlist success");
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
			Log.e("tag","send get user info success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getPhotos(int userId) {
		TranObject t = new TranObject();
		t.setObject(userId);
		t.setTranType(TranObjectType.GET_USER_PHOTOS);
		try {
			mNetService.send(t);
			Log.e("tag","send get user photos success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void uploadNewPic(int type,int userid, String path) {
		mNetService.uploadFile(type,userid,path);
	}

	public static void updateScore(int id, int spScore) {
		TranObject t = new TranObject();
		t.setObject(spScore);
		t.setSendId(id);
		t.setTranType(TranObjectType.UPDATE_SCORE);
		try {
			mNetService.send(t);
			Log.e("tag","send update score success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
