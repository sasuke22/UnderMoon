package com.test.jwj.underMoon.global;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.test.jwj.underMoon.Callback.UploadCallback;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.ChatEntity;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.TranObjectType;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.network.IMessageArrived;
import com.test.jwj.underMoon.network.NetService;
import com.test.jwj.underMoon.regist.StepAccount;
import com.test.jwj.underMoon.regist.StepPhoto;
import com.test.jwj.underMoon.utils.OkhttpUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	private static HashMap<String,String> params = new HashMap<>();

	public static void accountVerify(String account) throws IOException {
		params.clear();
		params.put("account",account);
		OkhttpUtil.get("user/imuser/registaccount",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful())
					StepAccount.setRegisterInfo(response.body().string());
			}
		});
	}

	public static void register(User user,String headPath) throws IOException {
		params.clear();
		params.put("user",mGson.toJson(user));
		ArrayList<String> list = new ArrayList<>();
		list.add(headPath);
		OkhttpUtil.post("user/imuser/regist",params,list).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					StepPhoto.setRegisterInfo(response.body().string(), true);
				}
			}
		});
	}

	public static void loginVerify(User user) throws IOException {
		TranObject t = new TranObject(user, TranObjectType.LOGIN);
		mNetService.send(t);
	}

	public static void searchFriend(String message) throws IOException {
		params.clear();
		params.put("filter",message);
		OkhttpUtil.get("searchfriend",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					ArrayList<User> list = mGson.fromJson(response.body().string(),new TypeToken<List<User>>(){}.getType());
					if (miDataListener != null)
						miDataListener.OnDataArrived(list);
				}
			}
		});
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
		params.clear();
		params.put("userId",String.valueOf(userid));
		OkhttpUtil.get("allcontributes",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					ArrayList<MeetingDetail> list = mGson.fromJson(response.body().string(),new TypeToken<List<MeetingDetail>>(){}.getType());
					if (miDataListener != null)
						miDataListener.OnDataArrived(list);
				}else
					ApplicationData.mApplication.mBinder.AlertToast("服务器未知错误，请重试，请联系大佐");
			}
		});
	}

	public static void getMyContributes(int userid){
		params.clear();
		params.put("userId",String.valueOf(userid));
		OkhttpUtil.get("mycontributes",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					ArrayList<MeetingDetail> list = mGson.fromJson(response.body().string(),new TypeToken<List<MeetingDetail>>(){}.getType());
					if (miDataListener != null)
						miDataListener.OnDataArrived(list);
				}else
					ApplicationData.mApplication.mBinder.AlertToast("服务器未知错误，请重试，请联系大佐");
			}
		});
	}

	public static void getTodayContributes(int userid) {
		params.clear();
		params.put("userId",String.valueOf(userid));
		OkhttpUtil.get("todaycontributes",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					ArrayList<MeetingDetail> list = mGson.fromJson(response.body().string(),new TypeToken<List<MeetingDetail>>(){}.getType());
					if (miDataListener != null)
						miDataListener.OnDataArrived(list);
				}
			}
		});
	}

	public static void getInvitationDetail(int meetingId) {
		params.clear();
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
		params.clear();
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
		params.clear();
		params.put("userId",String.valueOf(userId));
		params.put("meetingId",String.valueOf(meetingId));
		params.put("userName",enlisterName);
		OkhttpUtil.get("enlist",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					String result = response.body().string();
					switch (result){
						case "1":
							ApplicationData.mApplication.mBinder.AlertToast("报名成功");
							break;
						case "0":
							ApplicationData.mApplication.mBinder.AlertToast("已经报过名");
							break;
						default:
							ApplicationData.mApplication.mBinder.AlertToast("报名失败，请重试");
							break;
					}
				}
			}
		});
	}

	public static void saveUserInfo(User user) {
		params.clear();
		params.put("userinfo",mGson.toJson(user));
		OkhttpUtil.post("saveuserinfo",params,null).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					String result = response.body().string();
					ApplicationData.mApplication.mBinder.AlertToast(result.equals("1") ? "保存成功" : "保存失败，请重试");
				}
			}
		});
	}

	public static void getEnlist(int userid) {
		params.clear();
		params.put("userId",String.valueOf(userid));
		OkhttpUtil.get("getenlist",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					if (response.isSuccessful()) {
						ArrayList<MeetingDetail> list = mGson.fromJson(response.body().string(),new TypeToken<List<MeetingDetail>>(){}.getType());
						if (miDataListener != null)
							miDataListener.OnDataArrived(list);
					}
				}else
					ApplicationData.mApplication.mBinder.AlertToast("获取列表失败，请重试");
			}
		});
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
		params.clear();
		params.put("userId",String.valueOf(userId));
		OkhttpUtil.get("getuserinfo",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					User enlister = mGson.fromJson(response.body().string(),User.class);
					if (miDataListener != null)
						miDataListener.OnDataArrived(enlister);
				}
			}
		});
	}

	public static void getPhotos(int userId) {
		params.clear();
		params.put("userId",String.valueOf(userId));
		OkhttpUtil.get("getuserpic",params).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					if (miDataListener != null)
						miDataListener.OnDataArrived(mGson.fromJson(response.body().string(),String.class));
				}
			}
		});
	}

	public static void uploadNewPic(int userid, String path, final UploadCallback callback) {
		params.clear();
		params.put("userId",String.valueOf(userid));
		ArrayList<String> fileList = new ArrayList<>();
		fileList.add(path);
		OkhttpUtil.post("imgupload",params,fileList).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()) {
					String result = response.body().string();
					ApplicationData.mApplication.mBinder.AlertToast(result.equals("1") ? "上传成功" : "上传失败，请重试");
					if (callback != null)
						callback.uploadResult();
				}
			}
		});
	}

	public static void updateScore(int id, int spScore) {
		params.clear();
		params.put("userId",String.valueOf(id));
		params.put("score",String.valueOf(spScore));
		OkhttpUtil.post("updateuserscore",params,null).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.isSuccessful()){
					String result = response.body().string();
					if (result.equals("1"))
						Log.e("tag","更新积分成功");
					else
						Log.e("tag","更新积分失败");
				}
			}
		});
	}
}
