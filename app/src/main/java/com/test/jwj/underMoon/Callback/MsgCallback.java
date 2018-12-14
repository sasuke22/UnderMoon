package com.test.jwj.underMoon.Callback;

import com.test.jwj.underMoon.bean.ChatEntity;

import java.io.Serializable;

public interface MsgCallback extends Serializable{
    void onMsgCallback(ChatEntity chatMsg);
}
