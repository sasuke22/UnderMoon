package com.test.jwj.underMoon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.jwj.underMoon.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder>{
    private List<Bitmap> mPhotoList;
    private Context      mContext;
    public RecyclerViewAdapter(Context context, List list){
        this.mContext = context;
        this.mPhotoList = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.iv_photo.setImageBitmap(mPhotoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView iv_photo;
        public MyHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_personal_photo);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO 子条目点击，放大图片
        }

        @Override
        public boolean onLongClick(View v) {
            //TODO 子条目长点击，弹dialog确认删除图片
            return false;
        }
    }

    interface MyItemClickListener extends View.OnClickListener{
        void onItemClick(View view,int position);
    }

    interface MyItemLongClickListener extends View.OnLongClickListener{
        void onItemLongClick(View view,int position);
    }
}
