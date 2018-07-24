package com.test.jwj.underMoon.network;

import android.util.Log;

import com.test.jwj.underMoon.bean.TranObject;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;


public class ClientSendThread {
	private Socket             mSocket     = null;
	private ObjectOutputStream oos         = null;
	private OutputStream       fileOOS     = null;
	private Socket             mFileSocket = null;
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
			fileOOS = getFileSocket();
			if (fileOOS == null)
				return;
			fileOOS.write(userid);
			File file = new File(path);
			fileOutStream = new RandomAccessFile(file,"r");
			byte[] buffer = new byte[1024];
			int len;
			while((len = fileOutStream.read(buffer)) != -1){
				fileOOS.write(buffer,0,len);
			}
			fileOutStream.close();
			fileOOS.flush();
			fileOOS.close();
			mFileSocket.close();
		} catch (Exception e){
			Log.e("tag","heh " + e.getMessage());
		} finally {
			try {
				if (fileOutStream != null)
					fileOutStream.close();
				if (fileOOS != null)
					fileOOS.close();
				if (mFileSocket != null && mFileSocket.isConnected())
					mFileSocket.close();
			} catch (IOException e) {
				Log.e("tag",e.getMessage());
			}
		}

	}

	private OutputStream getFileSocket() throws IOException {
		FileNetConnect fileConnect = new FileNetConnect();
		fileConnect.startConnect();
		if (fileConnect.getIsConnected()){
			mFileSocket = fileConnect.getSocket();
			return mFileSocket.getOutputStream();
		}
		return null;
	}
}
