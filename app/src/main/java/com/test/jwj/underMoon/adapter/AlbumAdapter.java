package com.test.jwj.underMoon.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.test.jwj.underMoon.CustomView.SquareImageview;
import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.bean.Material;
import com.test.jwj.underMoon.utils.MyVideoThumbLoader;

import java.util.List;

/**
 * Created by jiangweijin on 2018/8/29.
 */

public class AlbumAdapter extends BaseAdapter {
    private List<Material>     picList;
    private Context            mContext;
    private LayoutInflater     mInflater;
    private MyVideoThumbLoader mThumbLoader;

    public AlbumAdapter(Context context, List<Material> albumList) {
        this.mContext = context;
        this.picList = albumList;
        this.mInflater = LayoutInflater.from(context);
        this.mThumbLoader = new MyVideoThumbLoader();
    }

    @Override
    public int getCount() {
        return picList == null ? 1 : picList.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return picList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.album_item,viewGroup,false);
            holder.img = (SquareImageview) view.findViewById(R.id.album_item_img);
            holder.duration = (TextView) view.findViewById(R.id.album_item_duration);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
        if (i == 0)
            holder.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_addpic_unfocused));
        else{
            Material pic = picList.get(i - 1);
            if (pic.getFileType() == 2){
                holder.duration.setText(pic.getTime());
                holder.img.setTag(pic.getFilePath());//绑定提供给loader使用
                mThumbLoader.showThumbByAsynctack(pic.getFilePath(),holder.img);
            }else
                holder.img.setImageBitmap(BitmapFactory.decodeFile(pic.getLogo()));
        }
        return view;
    }

    class ViewHolder{
        SquareImageview img;
        TextView        duration;
    }
}
