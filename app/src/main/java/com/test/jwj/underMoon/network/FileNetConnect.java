package com.test.jwj.underMoon.network;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by jiangweijin on 2018/7/23.
 */

public class FileNetConnect {
    private Socket mClientSocket = null;
    private static final String SERVER_IP = "192.168.107.41";
    private static final int FILE_SERVER_PORT = 8400;
    private boolean mIsConnected = false;

    public FileNetConnect() {
    }

    public void startConnect() {
        try {
            mClientSocket = new Socket();
            mClientSocket.connect(
                    new InetSocketAddress(SERVER_IP, FILE_SERVER_PORT), 3000);
            Log.e("tag", "服务器连接成功");
            if (mClientSocket.isConnected()) {
                mIsConnected = true;
            } else {
                mIsConnected = false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("tag", "服务器地址无法解析");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("tag", "Socket io异常");
        }
    }

    public boolean getIsConnected() {
        return mIsConnected;
    }

    public Socket getSocket() {
        return mClientSocket;
    }
}
