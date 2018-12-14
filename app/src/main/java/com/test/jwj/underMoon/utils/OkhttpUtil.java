package com.test.jwj.underMoon.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by jiangweijin on 2018/11/22.
 */

public class OkhttpUtil {
    private static final String SERVER_IP_ADDRESS = "http://192.168.107.60:8080/qiqiim-server/";
    private static OkHttpClient mClient = new OkHttpClient();

    public static Call post(String url,HashMap<String,String> params, ArrayList<String> fileList){
        MultipartBody.Builder builder = getMultipartBuilder(params);
        if (fileList != null && fileList.size() > 0){
            File file;
            for (String path : fileList){
                file = new File(path);
                builder.addFormDataPart("file",file.getName(), RequestBody.create(MediaType.parse("image/png"),file));
            }
        }
        Request request = new Request.Builder().url(SERVER_IP_ADDRESS + url)
                .post(builder.build()).build();
        return mClient.newCall(request);
    }

    public static Call get(String url,HashMap<String,String> params){
        StringBuilder actualUrl = new StringBuilder(url);
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String,String> set : entries){
            if (i == 0)
                actualUrl.append("?");
            else
                actualUrl.append("&");
            actualUrl.append(set.getKey()).append("=").append(set.getValue());
            i++;
        }
        Request request = new Request.Builder().url(SERVER_IP_ADDRESS + actualUrl)
                .get().build();
        return mClient.newCall(request);
    }

    private static MultipartBody.Builder getMultipartBuilder(HashMap<String, String> params) {
        Set<Map.Entry<String, String>> entries = params.entrySet();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String,String> set : entries){
            builder.addFormDataPart(set.getKey(),set.getValue());
        }
        return builder;
    }

}
