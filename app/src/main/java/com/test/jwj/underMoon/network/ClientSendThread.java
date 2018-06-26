package com.test.jwj.underMoon.network;

import android.util.Log;

import com.test.jwj.underMoon.bean.TranObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientSendThread {
	private Socket mSocket = null;
	private ObjectOutputStream oos = null;
	public ClientSendThread(Socket socket) {
		this.mSocket = socket;
		try {
			oos = new ObjectOutputStream(mSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void sendMessage(TranObject t) throws IOException{
		oos.writeObject(t);
		oos.flush();
		Log.e("tag","send msg " + t.getSendName());
	}
}
