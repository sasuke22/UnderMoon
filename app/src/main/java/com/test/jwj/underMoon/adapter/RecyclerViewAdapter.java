package com.test.jwj.underMoon.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.test.jwj.underMoon.CustomView.SquareImageview;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.activity.InvitationDetailActivity;
import com.test.jwj.underMoon.activity.WomenPhotoActivity;
import com.test.jwj.underMoon.bean.ApplicationData;
import com.test.jwj.underMoon.utils.Bimp;
import com.test.jwj.underMoon.utils.SystemMethod;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/28.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder>{
    private ArrayList<String> mPhotoList;
    private Context      mContext;
    private int          mUserId;
    private MyItemClickListener itemClickListener;
    private MyItemLongClickListener itemLongClickListener;
    public RecyclerViewAdapter(Context context, ArrayList list, int userId){
        this.mUserId = userId;
        this.mContext = context;
        this.mPhotoList = list;
    }

    public RecyclerViewAdapter(Context context, ArrayList list){
        this.mContext = context;
        this.mPhotoList = list;
    }

    public void setPhotoList(ArrayList<String> list){
        this.mPhotoList = list;
        notifyDataSetChanged();
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
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher);
        DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions().crossFade();
        if (mContext instanceof WomenPhotoActivity) {
            if (position == 0) {
                Bitmap addBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_addpic_unfocused);
                holder.iv_photo.setImageBitmap(addBitmap);
            } else {
                Glide.with(mContext).load(ApplicationData.SERVER_IP + mUserId + "/" + mPhotoList.get(position - 1) + ".jpg").//减1是为了去掉一开始的添加图片按钮
                        apply(requestOptions).transition(transitionOptions).into(holder.iv_photo);
            }
        }else if (mContext instanceof InvitationDetailActivity){
            Glide.with(mContext).load(mPhotoList.get(position))
                    .apply(requestOptions).transition(transitionOptions).into(holder.iv_photo);
        }else{
            Glide.with(mContext).load(ApplicationData.SERVER_IP + mUserId + "/" + mPhotoList.get(position) + ".jpg").//减1是为了去掉一开始的添加图片按钮
                    apply(requestOptions).transition(transitionOptions).into(holder.iv_photo);
        }
        holder.itemView.setTag(position);//将position赋值给子View，让在外面调用的能够知道点击的position,如果说每个recyclerView使用的地方子条目点击事件一样就不用这么麻烦
    }

    @Override
    public int getItemCount() {
        if (mContext instanceof WomenPhotoActivity)
            return mPhotoList == null ? 1 : mPhotoList.size() + 1;
        else
            return mPhotoList == null ? 0 : mPhotoList.size();
    }//加1为了增加一个加号的位置

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        SquareImageview iv_photo;

        private MyHolder(View itemView) {
            super(itemView);
            iv_photo = (SquareImageview) itemView.findViewById(R.id.iv_personal_photo);
            ViewGroup.LayoutParams layoutParams = iv_photo.getLayoutParams();
            int width = (SystemMethod.getScreenWidthPixels((Activity) mContext) - SystemMethod.dip2px(mContext,25)) / 4;
            layoutParams.width = width;
            layoutParams.height = width;
            iv_photo.setLayoutParams(layoutParams);
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
