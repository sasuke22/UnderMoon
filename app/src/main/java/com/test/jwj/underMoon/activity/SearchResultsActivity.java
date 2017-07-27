package com.test.jwj.underMoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.jwj.underMoon.R;
import com.test.jwj.underMoon.adapter.FriendsAdapter;
import com.test.jwj.underMoon.javabean.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */
public class SearchResultsActivity extends Activity{
    private List<Friends> mFriendsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ListView lv_search_list = (ListView) findViewById(R.id.lv_search_list);
        //TODO 通过网络获取数据
        lv_search_list.setAdapter(new FriendsAdapter(this,mFriendsList));
        lv_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friends friendsItem = mFriendsList.get(position);
                Intent intent = new Intent(SearchResultsActivity.this,FriendDetailActivity.class);
                intent.putExtra("friendId",friendsItem.id);
                startActivity(intent);
            }
        });
    }
}
