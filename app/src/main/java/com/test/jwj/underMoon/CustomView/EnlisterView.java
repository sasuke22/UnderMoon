package com.test.jwj.underMoon.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.test.jwj.underMoon.activity.EnlistInfoActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangweijin on 2018/5/30.
 */

@SuppressLint("AppCompatCustomView")
public class EnlisterView extends TextView {
    public HashMap<String,String> users;
    private String mMiddleStr = ",";
    private Context mContext;

    public EnlisterView(Context context) {
        super(context);
        mContext = context;
    }

    public EnlisterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public EnlisterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setData(HashMap enlisters){
        this.users = enlisters;
        this.setHighlightColor(Color.GRAY);
        this.setMovementMethod (LinkMovementMethod.getInstance ());
        this.setText(getEnlistersString());
    }

    private SpannableStringBuilder getEnlistersString() {
        SpannableStringBuilder mBuilder = new SpannableStringBuilder ("❤");
        for(final Map.Entry<String,String> entry : users.entrySet()){
            if (!TextUtils.isEmpty(entry.getKey())){
                mBuilder.append(entry.getValue() + mMiddleStr);
                mBuilder.setSpan(new ClickableSpan() {
                                     @Override
                                     public void onClick(View widget) {
                                         Intent intent = new Intent(mContext,EnlistInfoActivity.class);
                                         intent.putExtra("enlistId",entry.getKey());
                                         mContext.startActivity(intent);
                                     }
                                 },mBuilder.length() - entry.getValue().length() - mMiddleStr.length(),
                        mBuilder.length() - mMiddleStr.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return mBuilder;
    }
}
