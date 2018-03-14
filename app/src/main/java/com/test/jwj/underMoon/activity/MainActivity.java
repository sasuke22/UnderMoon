package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.fragments.Fragment_bapa;
import com.test.jwj.underMoon.fragments.Fragment_personal_center;
import com.test.jwj.underMoon.fragments.FriendListFragment;
import com.test.jwj.underMoon.fragments.MessageFragment;
import com.test.jwj.underMoon.network.NetService;

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";
	private Context mContext;
	private ImageButton mNews,mConstact,mDeynaimic,mSetting;
	private View mPopView;
	private View currentButton;
	
	private TextView app_cancle;
	private TextView app_exit;
	private TextView app_change;
	
	private PopupWindow mPopupWindow;
	private LinearLayout buttomBarGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext=this;
		System.out.println("初始化Main");
		findView();
		init();
	}
	
	private void findView(){
		mPopView=LayoutInflater.from(mContext).inflate(R.layout.app_exit, null);//点击返回弹出的popupwindow
		buttomBarGroup=(LinearLayout) findViewById(R.id.buttom_bar_group);
		mNews=(ImageButton) findViewById(R.id.buttom_news);
		mConstact=(ImageButton) findViewById(R.id.buttom_constact);
		mDeynaimic=(ImageButton) findViewById(R.id.buttom_deynaimic);
		mSetting=(ImageButton) findViewById(R.id.buttom_setting);
		
		app_cancle=(TextView) mPopView.findViewById(R.id.app_cancle);//取消按钮
		app_change=(TextView) mPopView.findViewById(R.id.app_change_user);//注销按钮
		app_exit=(TextView) mPopView.findViewById(R.id.app_exit);//退出按钮
	}
	
	private void init(){
		mNews.setOnClickListener(newsOnClickListener);
		mConstact.setOnClickListener(constactOnClickListener);
		mDeynaimic.setOnClickListener(deynaimicOnClickListener);
		mSetting.setOnClickListener(settingOnClickListener);
		
		mConstact.performClick();
		
		mPopupWindow=new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		
		app_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		
		app_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, LoginActivity.class);
				startActivity(intent);//点击注销启动登录界面
				((Activity)mContext).overridePendingTransition(R.anim.activity_up, R.anim.fade_out);//退出动画
				NetService.getInstance().closeConnection();//网络断开连接
				finish();
			}
		});
		
		app_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NetService.getInstance().closeConnection();
				finish();
			}
		});
	}
	
	private OnClickListener newsOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			MessageFragment messageFragment=new MessageFragment();
			ft.replace(R.id.fl_content, messageFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
		}
	};
	
	private OnClickListener constactOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			FriendListFragment constactFatherFragment=new FriendListFragment();
			ft.replace(R.id.fl_content, constactFatherFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener deynaimicOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			Fragment_bapa dynamicFragment=new Fragment_bapa();
			ft.replace(R.id.fl_content, dynamicFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener settingOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			Fragment_personal_center settingFragment=new Fragment_personal_center();
			ft.replace(R.id.fl_content, settingFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private void setButton(View v){
		if(currentButton!=null&&currentButton.getId()!=v.getId()){
			currentButton.setEnabled(true);//设置上一个按钮可点击
		}
		v.setEnabled(false);//将现在的按钮设置不可点击，防止重复加载fragment
		currentButton=v;
	}

	/*
	 * 设置在主界面点击返回按钮的时候出发popupwindow，即退出界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_MENU){
			mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));
			mPopupWindow.showAtLocation(buttomBarGroup, Gravity.BOTTOM, 0, 0);
			mPopupWindow.setAnimationStyle(R.style.app_pop);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.update();
		}
		return super.onKeyDown(keyCode, event);
		
	}

}
