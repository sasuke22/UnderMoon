package com.test.jwj.underMoon.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.AlbumAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.bean.Material;
import com.test.jwj.underMoon.utils.PhotoUtils;

import java.io.File;
import java.util.List;

/**
 * Created by jiangweijin on 2018/8/29.
 */

public class SelectAlbumActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    GridView       gridView;
    List<Material> albumList;
    String         photoUri;
    AlbumAdapter   mAlbumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_album);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        albumList = PhotoUtils.getAllLocalPhotos(this);
        gridView = (GridView) findViewById(R.id.myGrid);
        mAlbumAdapter = new AlbumAdapter(this,albumList);
        gridView.setAdapter(mAlbumAdapter);
    }

    @Override
    protected void initEvents() {
        gridView.setOnItemClickListener(this);
        findViewById(R.id.select_album_cancel).setOnClickListener(this);
        findViewById(R.id.select_album_gallery).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
                } else {
                    photoUri = PhotoUtils.takePicture(this);
                }
            }else
                photoUri = PhotoUtils.takePicture(this);
        }else{
            //TODO 裁剪界面，上传,权限问题
            File file = new File(albumList.get(i - 1).getFilePath());
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                PhotoUtils.CropCamera(this, FileProvider.getUriForFile(this,getApplicationContext().getPackageName() +
                        ".provider",file));
            else
                PhotoUtils.CropCamera(this, Uri.fromFile(file));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_album_cancel:
                finish();
                break;
            case R.id.select_album_gallery:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
                if (resultCode != Activity.RESULT_CANCELED) {
                    File file = new File(photoUri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        PhotoUtils.CropCamera(this, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() +
                                ".provider", file));
                    else
                        PhotoUtils.CropCamera(this, Uri.fromFile(file));
                }
                break;
            case ApplicationData.CROP:
                if (resultCode != Activity.RESULT_CANCELED) {
                    //TODO 裁剪后的图片上传服务器
                    File cropFile = new File(PhotoUtils.tempCrop);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2){
            photoUri = PhotoUtils.takePicture(this);
        }
    }
}
