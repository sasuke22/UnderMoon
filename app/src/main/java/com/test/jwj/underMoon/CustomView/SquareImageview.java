package com.test.jwj.underMoon.CustomView;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by jiangweijin on 2018/9/5.
 */

public class SquareImageview extends android.support.v7.widget.AppCompatImageView {
    private Context mContext;
    public SquareImageview(Context context) {
        super(context);
        this.mContext = context;
    }


    public SquareImageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Drawable d = getDrawable();
//
//
//        if (d != null) {
//            // ceil not round - avoid thin vertical gaps along the left/right edges
//            int width = MeasureSpec.getSize(widthMeasureSpec);
//            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
//            Log.e("tag","height " + height + ",screen " + SystemMethod.getScreenWidthPixels((Activity) mContext));
//            setMeasuredDimension(width, height);
//        } else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = this.getMeasuredWidth();
        setMeasuredDimension(w, w);
    }
}
