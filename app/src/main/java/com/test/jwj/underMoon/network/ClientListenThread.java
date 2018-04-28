package com.test.jwj.underMoon.network;

import android.content.Context;
import android.util.Log;

import com.test.jwj.underMoon.activity.SearchFriendActivity;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.MeetingDetail;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.fragments.BaseFragment;
import com.test.jwj.underMoon.global.Result;
import com.test.jwj.underMoon.global.UnderMoonApplication;
import com.test.jwj.underMoon.regist.StepAccount;
import com.test.jwj.underMoon.regist.StepPhoto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.ArrayList;


public class ClientListenThread extends Thread {
	private Socket mSocket = null;
	private Context mContext = null;
	private ObjectInputStream mOis;
	private static IMessageArrived miDataListener;
	private boolean isStart = true;
	private UnderMoonApplication mApplication;

	public static void setMiDataListener(IMessageArrived listener) {
		miDataListener = listener;
	}

	public ClientListenThread(Context context, Socket socket) {
		this.mContext = context;
		this.mSocket = socket;
		mApplication = (UnderMoonApplication)context.getApplicationContext();
		try {
			mOis = new ObjectInputStream(mSocket.getInputStream());
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSocket(Socket socket) {
		this.mSocket = socket;
	}

	@Override
	public void run() {
		try {
			isStart = true;
			while (isStart) {
				TranObject mReceived = null;
				//System.out.println("开始接受服务器");
				mReceived = (TranObject) mOis.readObject();
				Log.e("tag","接受成功 " + mReceived.getTranType());
				switch (mReceived.getTranType()) {
				case REGISTER_ACCOUNT:
					StepAccount.setRegisterInfo(mReceived, true);
					 System.out.println("注册账号成功");
					break;
				case REGISTER:
					StepPhoto.setRegisterInfo(mReceived, true);
					break;
				case LOGIN:
					ApplicationData.getInstance().loginMessageArrived(mReceived);
					break;
				case SEARCH_FRIEND:
					System.out.println("收到朋友查找结果");
					SearchFriendActivity.messageArrived(mReceived);
					break;
				case FRIEND_REQUEST:
					ApplicationData.getInstance().friendRequestArrived(mReceived);
					break;
				case MESSAGE:
					ApplicationData.getInstance().messageArrived(mReceived);
					break;
				case ALL_CONTRIBUTES:
					BaseFragment.setMeetingList((ArrayList) mReceived.getObject());
					break;
				case TODAY_CONTRIBUTES:
					BaseFragment.setMeetingList((ArrayList) mReceived.getObject());
					break;
				case INVITATION_DETAIL:
					if (miDataListener != null)
						miDataListener.OnDataArrived((MeetingDetail)mReceived.getObject());//如果不行还是直接将数据传过去吧
					break;
				case ADD_CONTRIBUTE:
					int res = (int)mReceived.getObject();
					switch (res){
						case 1:
							mApplication.mBinder.AlertToast("邀约上传成功");
							break;
						case -1:
							mApplication.mBinder.AlertToast("邀约上传失败");
							break;
					}
					break;
				case ENLIST:
					Result registRes = (Result)mReceived.getObject();
					switch (registRes){
						case ENLIST_SUCCESS:
							mApplication.mBinder.AlertToast("报名成功");
							break;
						case ENLIST_FAILED:
							mApplication.mBinder.AlertToast("报名失败");
							break;
						case ENLIST_EXIST:
							mApplication.mBinder.AlertToast("已报过名");
							break;
					}
					break;
				case MY_CONTRIBUTES:
					BaseFragment.setMeetingList((ArrayList) mReceived.getObject());
					break;
				case SAVE_USER_INFO:
					res = (int)mReceived.getObject();
					switch (res){
						case 1:
							// TODO 通知修改成功，可能还要改，因为图片
							mApplication.mBinder.AlertToast("信息修改成功");
							break;
						case 0:
							mApplication.mBinder.AlertToast("信息修改失败");
							break;
					}
					break;
				case GET_ENLIST:
					//TODO 在查看自己发布的邀约，查看报名列表
					break;
				default:
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void close() {
		isStart = false;
	}
}
