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
    MyItemClickListener itemClickListener;
    MyItemLongClickListener itemLongClickListener;
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
        holder.itemView.setTag(position);//将position赋值给子View，让在外面调用的能够知道点击的position,如果说每个recyclerView使用的地方自条目点击事件一样就不用这么麻烦
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
            if (itemClickListener != null)
                itemClickListener.onItemClick(v,(int)v.getTag());
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null)
                itemLongClickListener.onItemLongClick(v,(int)v.getTag());
            return true;
        }
    }

    public interface MyItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface MyItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    public void setItemClickListener(MyItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(MyItemLongClickListener itemLongClickListener){
        this.itemLongClickListener = itemLongClickListener;
    }
}
