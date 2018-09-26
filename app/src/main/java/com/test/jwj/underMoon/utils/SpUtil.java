package com.test.jwj.underMoon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtil {
	private static final String NAME="QQ";
	private static SpUtil instance;
	static{
		instance=new SpUtil();
	}
	
	public static SpUtil getInstance(){
		if(instance==null){
			instance=new SpUtil();
		}
		return instance;
	}
	
	public static SharedPreferences getSharePreference(Context context){
		return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}
	
	public static boolean isFirst(SharedPreferences sp){
		return sp.getBoolean("isFirst", false);
	}
	
	public static void setIntSharedPreference(SharedPreferences sp,String key,int value){
		Editor editor=sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void setBooleanSharedPreference(SharedPreferences sp,String key,boolean value){
		Editor editor=sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static int getSPScore(SharedPreferences sp){
		return sp.getInt("score",0);
	}

	public static void setInteger(Context context, String name, int value) {
		getSharePreference(context).edit().putInt(name, value).commit();
	}

	public static int getInteger(Context context, String name, int default_i) {
		return getSharePreference(context).getInt(name, default_i);
	}
}
