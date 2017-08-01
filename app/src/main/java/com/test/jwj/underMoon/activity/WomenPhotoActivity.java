package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.test.jwj.underMoon.CustomView.DividerGridItemDecoration;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.RecyclerViewAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class WomenPhotoActivity extends Activity {
    private List<Bitmap> mPhotoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women_photo);
        //TODO 网络获取图片流转换成bitmap设置给mPhotoList

        final RecyclerView gv_women_photo = (RecyclerView) findViewById(R.id.gv_women_photo);
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,mPhotoList);
        adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0){
                    //TODO 添加图片
                }
                //TODO 将图片放大，做法可以是弹一个对话框显示图片

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

}
