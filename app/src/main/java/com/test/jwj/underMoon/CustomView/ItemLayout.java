package com.test.jwj.underMoon.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.jwj.underMoon.R;

/**
 * Created by jiangweijin on 2018/8/13.
 */

public class ItemLayout extends RelativeLayout{
    private ImageView img;
    private TextView name;
    private TextView addition;
    private ImageView backImg;
    private LayoutClickListener mClickListener;
    private Context mContext;

    public ItemLayout(Context context) {
        this(context,null);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews(attrs);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initViews(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_item,this);
        img = (ImageView) view.findViewById(R.id.item_img);
        name = (TextView) view.findViewById(R.id.item_name);
        addition = (TextView) view.findViewById(R.id.item_addition);
        backImg = (ImageView) view.findViewById(R.id.item_back);
        if (attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.skill_item);
            Drawable itemImage = a.getDrawable(R.styleable.skill_item_itemImage);
            if (itemImage != null)
                img.setImageDrawable(itemImage);
            else img.setVisibility(GONE);
            String itemName = a.getString(R.styleable.skill_item_itemName);
            if (itemName != null)
                name.setText(itemName);
            else name.setVisibility(GONE);
            String itemAddition = a.getString(R.styleable.skill_item_itemAddition);
            if (itemAddition != null)
                addition.setText(itemAddition);
            else addition.setVisibility(GONE);
            Drawable itemBackImg = a.getDrawable(R.styleable.skill_item_itemBackImg);
            if (itemBackImg != null)
                backImg.setImageDrawable(itemBackImg);
            else backImg.setVisibility(GONE);
        }
    }

    public TextView getAddition() {
        return addition;
    }

    public void setLayoutClickListener(LayoutClickListener listener){
        mClickListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if (mClickListener != null)
                    mClickListener.onLayoutClick(this);
                break;
        }
        return true;
    }

    public void setBackImg(int res){
        backImg.setImageDrawable(ContextCompat.getDrawable(mContext,res));
    }

    public interface LayoutClickListener {
        void onLayoutClick(View view);
    }
}
