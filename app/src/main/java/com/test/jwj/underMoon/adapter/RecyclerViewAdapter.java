package com.test.jwj.underMoon.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.utils.Bimp;

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
//        mPhotoList.add(0,addBitmap);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    RecyclerViewAdapter.this.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_photo,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (position == 0){
            Bitmap addBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.icon_addpic_unfocused);
            holder.iv_photo.setImageBitmap(addBitmap);
            holder.itemView.setTag(0);
        }else{
            Glide.with(mContext).load(mPhotoList.get(position - 1)).//减1是为了去掉一开始的添加图片按钮
                    centerCrop().placeholder(R.mipmap.ic_launcher).crossFade().into(holder.iv_photo);
            holder.itemView.setTag(position);//将position赋值给子View，让在外面调用的能够知道点击的position,如果说每个recyclerView使用的地方自条目点击事件一样就不用这么麻烦
        }
    }

    @Override
    public int getItemCount() {
        return (Bimp.tempSelectBitmap.size() + 1);
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

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        Bimp.max += 1;
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();
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
