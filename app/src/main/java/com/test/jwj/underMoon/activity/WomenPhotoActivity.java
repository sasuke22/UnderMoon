package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

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
import com.test.jwj.underMoon.utils.PublicWay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Administrator on 2017/7/28.
 */

public class WomenPhotoActivity extends Activity implements IMessageArrived<String>{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getIntent().getIntExtra("id",-1);
        mHandler = new Handler();
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
        Log.e("tag","init");
        RecyclerView gv_women_photo = (RecyclerView) findViewById(R.id.gv_women_photo);
        adapter = new RecyclerViewAdapter(this,mPhotoList);
        adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0){
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(WomenPhotoActivity.this,R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    //将图片放大
//                    Intent intent = new Intent(WomenPhotoActivity.this,
//                            GalleryActivity.class);
//                    intent.putExtra("position", "1");
//                    intent.putExtra("ID", position - 1);
//                    startActivity(intent);
                    LayoutInflater inflater = LayoutInflater.from(WomenPhotoActivity.this);
                    View bigPhoto = inflater.inflate(R.layout.dialog_big_photo,null);
                    final AlertDialog dialog = new AlertDialog.Builder(WomenPhotoActivity.this).create();
                    Glide.with(WomenPhotoActivity.this).load(mPhotoList.get(position - 1))
                            .placeholder(R.mipmap.ic_launcher).crossFade().into((ImageView) bigPhoto.findViewById(R.id.large_photo));
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
                    //TODO 弹对话框删除
                    new AlertDialog.Builder(WomenPhotoActivity.this).setTitle("系统提示")
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
        new Thread(new Runnable() {
            @Override
            public void run() {//网络获取图片流转换成bitmap设置给mPhotoList
                UserAction.getPhotos(userId);
                synchronized (key){
                    try {
                        key.wait();
                        Log.e("tag","photos wait");
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
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        photoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(WomenPhotoActivity.this,
//                        AlbumActivity.class);
//                startActivity(intent);
                PhotoUtils.selectPhoto(WomenPhotoActivity.this);
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
//                            Bitmap bitmap = BitmapFactory.decodeFile(path);
//                            if (PhotoUtils.bitmapIsLarge(bitmap)) {
//                                PhotoUtils.cropPhoto(this, this, path);
//                            } else {
//                                //TODO 给recyclerview添加一个图片，然后在点击确定时上传
//                                setUserPhoto(PhotoUtils.compressImage(bitmap));
//                            }
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for(int i = 0; i< PublicWay.activityList.size(); i++){
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
            System.exit(0);
        }
        return true;
    }

    protected void onRestart() {
        adapter.loading();
        super.onRestart();
    }

    @Override
    public void OnDataArrived(String urlStr) {
        String[] urlArray = urlStr.split("|");
        mPhotoList = new ArrayList<>(Arrays.asList(urlArray));
        synchronized (key){
            key.notify();
        }
    }


}
