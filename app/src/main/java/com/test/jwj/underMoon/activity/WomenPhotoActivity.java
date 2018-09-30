package com.test.jwj.underMoon.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.test.jwj.underMoon.CustomView.DividerGridItemDecoration;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.RecyclerViewAdapter;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.global.UserAction;
import com.test.jwj.underMoon.network.ClientListenThread;
import com.test.jwj.underMoon.network.IMessageArrived;
import com.test.jwj.underMoon.utils.Bimp;
import com.test.jwj.underMoon.utils.FileUtils;
import com.test.jwj.underMoon.utils.ImageItem;
import com.test.jwj.underMoon.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2017/7/28.
 */

public class WomenPhotoActivity extends Activity implements IMessageArrived<String>, View.OnClickListener {
    private static final int TAKE_PICTURE = 0x000001;
    private List<String> mPhotoList;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private RecyclerViewAdapter adapter;
    private View parentView;
    public static Bitmap mBitmap;
    private final Object key = new Object();
    private int     userId;
    private Handler mHandler;
    RecyclerView gv_women_photo;
    private final int OPEN_CANMER = 111;
    private final int OPEN_ALBUM = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getIntExtra("id",-1);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0)
                    initPhotos();
                super.handleMessage(msg);
            }
        };
        mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.icon_addpic_unfocused);//添加图片的按钮，需要将它放在第一个
        parentView = getLayoutInflater().inflate(R.layout.activity_women_photo,null);
        ClientListenThread.setMiDataListener(this);
        setContentView(parentView);

    }

    @Override
    protected void onStart() {
        initViews();
        super.onStart();
    }

    private void initViews() {
        initPopupWindow();
        gv_women_photo = (RecyclerView) findViewById(R.id.gv_women_photo);
        ((TextView) findViewById(R.id.header_title)).setText("个人相册");
        findViewById(R.id.header_back).setOnClickListener(this);
        findViewById(R.id.header_option).setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {//网络获取图片流转换成bitmap设置给mPhotoList
                UserAction.getPhotos(userId);
                synchronized (key){
                    try {
                        key.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(0);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //隐藏加载bar？
                    }
                });

            }
        }).start();
    }

    private void initPhotos(){
        adapter = new RecyclerViewAdapter(this,mPhotoList,userId);
        adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0){
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(WomenPhotoActivity.this,R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    LayoutInflater inflater = LayoutInflater.from(WomenPhotoActivity.this);
                    View bigPhoto = inflater.inflate(R.layout.dialog_big_photo,null);
                    final AlertDialog dialog = new AlertDialog.Builder(WomenPhotoActivity.this).create();

                    WindowManager wm = (WindowManager) WomenPhotoActivity.this
                            .getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int width =display.getWidth();
                    int height=display.getHeight();
                    Glide.with(WomenPhotoActivity.this).load(ApplicationData.SERVER_IP + userId + "/" + mPhotoList.get(position-1) + ".jpg")
                            .placeholder(R.mipmap.ic_launcher).crossFade().override(width,height).into((ImageView) bigPhoto.findViewById(R.id.large_photo));
                    dialog.setView(bigPhoto);
                    dialog.show();
                    bigPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            }
        });
        adapter.setItemLongClickListener(new RecyclerViewAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                if (position != 0){
                    new AlertDialog.Builder(WomenPhotoActivity.this).setTitle("系统提示")
                            .setMessage("删除该照片？")
                            .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO 加上删除的网络动作
                                    mPhotoList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }
        });
        gv_women_photo.setAdapter(adapter);
        gv_women_photo.setLayoutManager(new GridLayoutManager(this,4));
        gv_women_photo.addItemDecoration(new DividerGridItemDecoration(this));
    }

    private void initPopupWindow() {
        pop = new PopupWindow(WomenPhotoActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(RecyclerView.LayoutParams.MATCH_PARENT);
        pop.setHeight(RecyclerView.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        Button cameraBtn = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button photoBtn = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button cancelBtn = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(WomenPhotoActivity.this, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WomenPhotoActivity.this, new String[]{Manifest.permission.CAMERA}, OPEN_CANMER);
                        return;
                    }else
                        photo();
                }else
                    photo();
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        photoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(WomenPhotoActivity.this, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WomenPhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, OPEN_ALBUM);
                        return;
                    }else
                        PhotoUtils.selectPhoto(WomenPhotoActivity.this);
                }else
                    PhotoUtils.selectPhoto(WomenPhotoActivity.this);
//                PhotoUtils.selectPhoto(WomenPhotoActivity.this);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /*
     * 从摄像头返回时拿到拍摄的图片
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
            case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
                if (data == null) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (data.getData() == null) {
                        return;
                    }
                    Uri uri = data.getData();//直接用这个uri给glide赋值？
                    String[] proj = { MediaStore.MediaColumns.DATA };
                    Cursor cursor = managedQuery(uri, proj, null, null, null);
                    if (cursor != null) {
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                            String path = cursor.getString(column_index);
                            uploadPic(path);
                            mPhotoList.add(uri.toString());
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
                break;
        }
    }

    private void uploadPic(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserAction.uploadNewPic(ApplicationData.getInstance().getUserInfo().getId(),path);
            }
        }).start();
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            for(int i = 0; i< PublicWay.activityList.size(); i++){
//                if (null != PublicWay.activityList.get(i)) {
//                    PublicWay.activityList.get(i).finish();
//                }
//            }
//            System.exit(0);
//        }
//        return true;
//    }

    protected void onRestart() {
        adapter.loading();
        super.onRestart();
    }

    @Override
    public void OnDataArrived(String urlStr) {
        String[] urlArray = urlStr.split("\\|");
        mPhotoList = new ArrayList<>(Arrays.asList(urlArray));
        if (mPhotoList.get(0) == "")
            mPhotoList.remove(0);
        Log.e("tag","data arrived " + mPhotoList);
        synchronized (key){
            key.notify();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case OPEN_CANMER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photo();
                } else {
                    Toast.makeText(WomenPhotoActivity.this, "相机权限禁用了。请务必开启相机权", Toast.LENGTH_SHORT).show();
                }
                break;
            case OPEN_ALBUM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.selectPhoto(WomenPhotoActivity.this);
                } else {
                    Toast.makeText(WomenPhotoActivity.this, "相机权限禁用了。请务必开启相机权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
        }
    }
}
