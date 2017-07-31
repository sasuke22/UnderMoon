package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
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

        RecyclerView gv_women_photo = (RecyclerView) findViewById(R.id.gv_women_photo);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,mPhotoList);
        adapter.setItemClickListener(new RecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO 将图片放大
            }
        });
        adapter.setItemLongClickListener(new RecyclerViewAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                //TODO 弹对话框删除
            }
        });
        gv_women_photo.setAdapter(adapter);
        gv_women_photo.setLayoutManager(new GridLayoutManager(this,3));
        gv_women_photo.addItemDecoration(new DividerGridItemDecoration(this));

    }

}
