package com.test.jwj.underMoon.regist;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.test.jwj.underMoon.CustomView.BaseDialog;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.utils.SpUtil;
import com.test.jwj.underMoon.utils.VerifyUtils;


public class StepBaseInfo extends RegisterStep implements TextWatcher,
		OnCheckedChangeListener {

	private EditText mEtName;
	private RadioGroup mRgGender;
	private RadioButton mRbMale;
	private RadioButton mRbFemale;
	private String mName;
	private String mCity;
	private String mFigure;
	private String mJob;
	private String mHeight;
	private String mMarry;
	private int mGender = 0;//0代表女 1代表男
	private boolean mIsChange = true;
	private boolean mIsGenderAlert;
	private BaseDialog mBaseDialog;
	private EditText mEtCity;
	private EditText mEtFigure;
	private EditText mEtJob;
	private EditText mEtHeight;
	private EditText mEtMarry;

	public StepBaseInfo(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initViews() {
		mEtName = (EditText) findViewById(R.id.reg_baseinfo_et_name);
		mRgGender = (RadioGroup) findViewById(R.id.reg_baseinfo_rg_gender);
		mRbMale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_male);
		mRbFemale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_female);
		mEtCity = (EditText) findViewById(R.id.reg_baseinfo_et_city);
		mEtFigure = (EditText) findViewById(R.id.reg_baseinfo_et_figure);
		mEtJob = (EditText) findViewById(R.id.reg_baseinfo_et_job);
		mEtHeight = (EditText) findViewById(R.id.reg_baseinfo_et_height);
		mEtMarry = (EditText) findViewById(R.id.reg_baseinfo_et_marry);
	}

	@Override
	public void initEvents() {
		mEtName.addTextChangedListener(this);
		mRgGender.setOnCheckedChangeListener(this);
	}

	@Override
	public void doNext() {
		mOnNextActionListener.next();
	}

	@Override
	public boolean validate() {
		mName = mEtName.getText().toString().trim();
		mCity = mEtCity.getText().toString().trim();
		mFigure = mEtFigure.getText().toString().trim();
		mJob = mEtJob.getText().toString().trim();
		mHeight = mEtHeight.getText().toString().trim();
		mMarry = mEtMarry.getText().toString().trim();
		if (VerifyUtils.isNull(mEtName)) {
			showCustomToast("请输入用户名");
			mEtName.requestFocus();
			return false;
		}
		
		if (mRgGender.getCheckedRadioButtonId() < 0) {
			showCustomToast("请选择性别");
			return false;
		}

		if (VerifyUtils.isNull(mEtCity)) {
			showCustomToast("请输入所在城市");
			mEtCity.requestFocus();
			return false;
		}

		if (VerifyUtils.isNull(mEtFigure)) {
			showCustomToast("请输入身材");
			mEtFigure.requestFocus();
			return false;
		}

		if (VerifyUtils.isNull(mEtJob)) {
			showCustomToast("请输入工作");
			mEtJob.requestFocus();
			return false;
		}

		if (VerifyUtils.isNull(mEtHeight)) {
			showCustomToast("请输入身高");
			mEtHeight.requestFocus();
			return false;
		}

		if (VerifyUtils.isNull(mEtMarry)) {
			showCustomToast("请输入情感状况");
			mEtMarry.requestFocus();
			return false;
		}

		if (!mMarry.equals("单身") && !mMarry.equals("已恋") && !mMarry.equals("已婚") && !mMarry.equals("不详")){
			showCustomToast("请输入正确的情感状况");
			mEtMarry.requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public boolean isChange() {
		return mIsChange;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mIsChange = true;
		if (!mIsGenderAlert) {
			mIsGenderAlert = true;
			mBaseDialog = BaseDialog.getDialog(mContext, "提示", "注册成功后性别将不可更改",
					"确认", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			mBaseDialog.show();
		}
		switch (checkedId) {
		case R.id.reg_baseinfo_rb_male:
			mRbMale.setChecked(true);
			mGender = 1;
			SpUtil.setBooleanSharedPreference(SpUtil.getSharePreference(mContext),"ismale",true);
			break;

		case R.id.reg_baseinfo_rb_female:
			mRbFemale.setChecked(true);
			mGender = 0;
			SpUtil.setBooleanSharedPreference(SpUtil.getSharePreference(mContext),"ismale",false);
			break;
		}
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
	}

	public String getName() {
		return mName;
	}
	public int getGender(){
		return mGender;
	}
	public String getCity() {
		return mCity;
	}
	public String getFigure(){
		return mFigure;
	}
	public String getJob() {
		return mJob;
	}
	public int getHeight(){
		return Integer.parseInt(mHeight);
	}
	public int getMarry(){
		if (mMarry.equals("单身"))
			return 0;
		else if (mMarry.equals("已恋"))
			return 1;
		else if (mMarry.equals("已婚"))
			return 2;
		else return 3;
	}
}
