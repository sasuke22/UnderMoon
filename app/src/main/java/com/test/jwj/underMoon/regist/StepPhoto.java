package com.test.jwj.underMoon.regist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.TranObject;
import com.test.jwj.underMoon.bean.User;
import com.test.jwj.underMoon.global.Result;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.utils.PhotoUtils;

import java.util.Date;


public class StepPhoto extends RegisterStep implements OnClickListener {

	private ImageView mIvUserPhoto;
	private LinearLayout mLayoutSelectPhoto;
	private LinearLayout mLayoutTakePicture;
	private LinearLayout mLayoutAvatars;

	private View[] mMemberBlocks;
	private String mTakePicturePath;
	private Bitmap mUserPhoto;
	private String mAccount;
	private String mPassword;
	private Date mBirthday;
	private String mName;
	private int mGender;
	private String mCity;
	private String mFigure;
	private String mJob;
	private int mHeight;
	private int mMarry;
	private static TranObject mReceivedInfo = null;
	private static boolean mIsReceived = false;
	private final int OPEN_CAMERA = 111;
	private final int OPEN_ALBUM = 222;

	public StepPhoto(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	

	public void setUserPhoto(Bitmap bitmap) {
		if (bitmap != null) {
			mUserPhoto = bitmap;
			mIvUserPhoto.setImageBitmap(mUserPhoto);
			return;
		}
		showCustomToast("未获取到图片");
		mUserPhoto = null;
		mIvUserPhoto.setImageResource(R.mipmap.ic_common_def_header);
	}

	public String getTakePicturePath() {
		return mTakePicturePath;
	}

	@Override
	public void initViews() {
		mIvUserPhoto = (ImageView) findViewById(R.id.reg_photo_iv_userphoto);
		mLayoutSelectPhoto = (LinearLayout) findViewById(R.id.reg_photo_layout_selectphoto);
		mLayoutTakePicture = (LinearLayout) findViewById(R.id.reg_photo_layout_takepicture);
	}

	@Override
	public void initEvents() {
		mLayoutSelectPhoto.setOnClickListener(this);
		mLayoutTakePicture.setOnClickListener(this);
	}

	@Override
	public boolean validate() {
		if (mUserPhoto == null) {
			showCustomToast("请添加头像");
			return false;
		}
		return true;
	}

	@Override
	public void doNext() {
		putAsyncTask(new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("请稍后,正在提交...");
			}

			@Override
			protected Integer doInBackground(Void... params) {
				try {
					mIsReceived = false;
					mNetService.setupConnection();
					if (!mNetService.isConnected()) {
						return 0;
					} else {
						byte[] photoByte = PhotoUtils.getBytes(mUserPhoto);
						int age = mActivity.getAge();
						User user = new User(mAccount, mName, mPassword,mCity,mFigure,mJob,mHeight,mMarry,
								mBirthday, mGender, photoByte);
						user.setAge(age);
						user.setXingzuo(mActivity.getXingzuo());
						UserAction.register(user);
						while (!mIsReceived) {
						}// 如果没收到的话就会一直阻塞;
						mNetService.closeConnection();
						if (mReceivedInfo.getResult() == Result.REGISTER_SUCCESS) {
							return 1;
						}else
							return 2;
					}
				} catch (Exception e) {
					Log.d("regester", "注册异常");

				}
				return 0;
				
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (result == 0) {
					showCustomToast("服务器异常");

				} else {
					if (result == 1) {
						showCustomToast("注册成功");
						mActivity.finish();
					} else if (result == 2) {
						showCustomToast("注册失败");
					}
				}
			}

		});
	}

	@Override
	public boolean isChange() {
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_photo_layout_selectphoto:
			if (Build.VERSION.SDK_INT >= 23) {
				int checkCallPhonePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
				if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
					ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, OPEN_ALBUM);
					return;
				}else
					PhotoUtils.selectPhoto(mActivity);
			}else
				PhotoUtils.selectPhoto(mActivity);
			break;

		case R.id.reg_photo_layout_takepicture:
			if (Build.VERSION.SDK_INT >= 23) {
				int checkCallPhonePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
				if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
					ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.CAMERA},OPEN_CAMERA);
					return;
				}else{
					mTakePicturePath = PhotoUtils.takePicture(mActivity);
				}
			} else {
				mTakePicturePath = PhotoUtils.takePicture(mActivity);
			}
			break;
		}
	}

	public Bitmap getPhoto() {
		return mUserPhoto;
	}

	public void setAccount(String account) {
		this.mAccount = account;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public void setGender(int gender) {
		this.mGender = gender;
	}

	public void setBirthday(Date birthday) {
		this.mBirthday = birthday;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	public void setCity(String city) {
		this.mCity = city;
	}

	public void setFigure(String Figure) {
		mFigure = Figure;
	}

	public void setJob(String Job) {
		mJob = Job;
	}

	public void setHeight(int Height) {
		mHeight = Height;
	}

	public void setMarry(int Marry) {
		mMarry = Marry;
	}

	public static void setRegisterInfo(TranObject object, boolean isReceived) {

		mReceivedInfo = object;
		mIsReceived = true;
	}


}
