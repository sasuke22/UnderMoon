package com.test.jwj.underMoon.network;

import android.util.Log;

import com.test.jwj.underMoon.bean.TranObject;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
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
		oos.writeInt(-1);
		oos.writeObject(t);
		oos.flush();
		Log.e("tag","send msg " + t.getSendName());
	}

	public void uploadFile(int userid,String path){
		RandomAccessFile fileOutStream = null;
		try {
//			String head = "userid=" + userid + "\r\n";
			oos.writeInt(userid);
//			oos.write(head.getBytes());
			File file = new File(path);
			fileOutStream = new RandomAccessFile(file,"r");
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = fileOutStream.read(buffer)) != -1){
				oos.write(buffer,0,len);
			}
			fileOutStream.close();
			oos.flush();
		} catch (Exception e){
			Log.e("tag",e.getMessage().toString());
		} finally {
			try {
				if (fileOutStream != null)
					fileOutStream.close();
			} catch (IOException e) {
				Log.e("tag",e.getMessage().toString());
			}
		}

	}
}
