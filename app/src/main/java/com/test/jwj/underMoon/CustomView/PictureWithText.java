package com.test.jwj.underMoon.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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

public class PictureWithText extends RelativeLayout{
    private ImageView img;
    private TextView text;
    private LayoutClickListener mClickListener;
    private Context mContext;

    public PictureWithText(Context context) {
        this(context,null);
    }

    public PictureWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initViews(attrs);
    }

    public PictureWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initViews(AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_picture_text,this);
        img = (ImageView) view.findViewById(R.id.picture_text_pic);
        text = (TextView) view.findViewById(R.id.picture_text_text);
        if (attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.PictureWithText);
            Drawable itemImage = a.getDrawable(R.styleable.PictureWithText_pic_txt_img);
            if (itemImage != null)
                img.setImageDrawable(itemImage);
            else img.setVisibility(GONE);
            String itemName = a.getString(R.styleable.PictureWithText_pic_txt_text);
            if (itemName != null)
                text.setText(itemName);
            else text.setVisibility(GONE);
            a.recycle();
        }
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

    public interface LayoutClickListener {
        void onLayoutClick(View view);
    }
}
