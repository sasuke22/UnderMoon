package com.test.jwj.underMoon.regist;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.test.jwj.underMoon.CustomView.HandyTextView;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.utils.VerifyUtils;

import java.io.IOException;


public class StepAccount extends RegisterStep implements TextWatcher {

	private EditText mEtAccount;
	private HandyTextView mHtvNotice;

	private static String mAccount;
	private static boolean mIsChange = true;

	private static String  userExist   = null;
	private static boolean mIsReceived = false;
	private Context mContext;

	public StepAccount(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
		mContext = activity;
	}

	public String getAccount() {
		return mAccount;
	}

	@Override
	public void initViews() {
		mEtAccount = (EditText) findViewById(R.id.reg_account_et_account);
		mHtvNotice = (HandyTextView) findViewById(R.id.reg_account_htv_notice);
	}

	@Override
	public void initEvents() {
		mEtAccount.addTextChangedListener(this);
	}

	@Override
	public void doNext() {
		new AccountTask().execute();
	}

	@Override
	public boolean validate() {
		mAccount = null;
		if (VerifyUtils.isNull(mEtAccount)) {
			showCustomToast("请填写账号");
			mEtAccount.requestFocus();
			return false;
		}
		String account = mEtAccount.getText().toString().trim();
		if (VerifyUtils.matchAccount(account)) {
			mAccount = account;
			return true;
		}
		showCustomToast("账号格式不正确");
		mEtAccount.requestFocus();
		return false;
	}

	@Override
	public boolean isChange() {
		return mIsChange;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mIsChange = true;
		if (s.toString().length() > 0) {
			mHtvNotice.setVisibility(View.VISIBLE);
			char[] chars = s.toString().toCharArray();
			StringBuilder buffer = new StringBuilder();
			for (char aChar : chars) {
				buffer.append(aChar);
			}
			mHtvNotice.setText(buffer.toString());
		} else {
			mHtvNotice.setVisibility(View.GONE);
		}
	}
	public static void setRegisterInfo(String result) {
		userExist = result;
		mIsReceived = true;
	}

	private class AccountTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在验证账号,请稍后...");
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				UserAction.accountVerify(mAccount);
				while (!mIsReceived) {
				}// 如果没收到的话就会一直阻塞;
				if (userExist.equals("true"))
					return 1;// 代表用户名已存在
				else if(userExist.equals("false"))
					return 2;// 代表用户名可用
			} catch (IOException e) {
				Log.d("register", "注册账号异常");
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
					showCustomToast("该账号已被注册");
				} else if (result == 2) {
					mIsChange = false;
					showCustomToast("该账号可用");
					mOnNextActionListener.next();
				}
			}
			userExist = null;
			mIsReceived = false;
		}
	}
}
