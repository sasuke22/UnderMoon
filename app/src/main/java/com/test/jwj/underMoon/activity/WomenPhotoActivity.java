package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.test.jwj.underMoon.CustomView.DividerGridItemDecoration;
import com.test.jwj.underMoon.adapter.RecyclerViewAdapter;
import com.test.jwj.underMoon.utils.Bimp;
import com.test.jwj.underMoon.utils.FileUtils;
import com.test.jwj.underMoon.utils.ImageItem;
import com.test.jwj.underMoon.utils.PublicWay;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class WomenPhotoActivity extends Activity {
    private static final int TAKE_PICTURE = 0x000001;
    private List<Bitmap> mPhotoList;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private RecyclerViewAdapter adapter;
    private View parentView;
    public static Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.icon_addpic_unfocused);
        parentView = getLayoutInflater().inflate(R.layout.activity_women_photo,null);
        setContentView(parentView);

        //TODO 网络获取图片流转换成bitmap设置给mPhotoList

        initViews();

    }

    private void initViews() {
        initPopupWindow();

        RecyclerView gv_women_photo = (RecyclerView) findViewById(R.id.gv_women_photo);
        adapter = new RecyclerViewAdapter(this,mPhotoList);
        adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == Bimp.tempSelectBitmap.size()){
                    //TODO 添加图片
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(WomenPhotoActivity.this,R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
                    //TODO 将图片放大，做法可以是弹一个对话框显示图片
                    Intent intent = new Intent(WomenPhotoActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
//                    intent.putExtra("ID", arg2);
                    startActivity(intent);
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
        gv_women_photo.setLayoutManager(new GridLayoutManager(this,3));
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
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        photoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WomenPhotoActivity.this,
                        AlbumActivity.class);
                startActivity(intent);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
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
}
