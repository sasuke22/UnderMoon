package com.test.jwj.underMoon.bean;

/**
 * 传输对象类型
 * 
 * 
 * 
 */
public enum TranObjectType {
	REGISTER, // 注册
	REGISTER_ACCOUNT,//注册的账号验证
	LOGIN, // 用户登录
	MESSAGE, // 用户发送消息
	SEARCH_FRIEND,//找朋友
	FRIEND_REQUEST,//好友申请
	ALL_CONTRIBUTES,
    INVITATION_DETAIL,
	ADD_CONTRIBUTE,
	MY_CONTRIBUTES,
	TODAY_CONTRIBUTES,
    SAVE_USER_INFO, GET_ENLIST, GET_ENLIST_NAME, GET_USER_INFO, GET_USER_PHOTOS, UPLOAD_USER_PHOTOS, UPLOAD_RESULT, ENLIST
}
