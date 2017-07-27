package com.test.jwj.underMoon.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.test.jwj.underMoon.R;

/**
 * Created by Administrator on 2017/4/12.
 */

public class SearchActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViewById(R.id.start_search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_search:
                Intent intent = new Intent(this,SearchResultsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
