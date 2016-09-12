package com.tvo.nomaps.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.adapters.MyPageAdapter;

import cz.msebera.android.httpclient.Header;

/**
 * Created by huy on 8/31/2016.
 */
public class MyPageActivity extends BaseActivity {
    private final String TAG = MyPageActivity.class.getSimpleName();
    private ListView lvMyPage;
    private MyPageAdapter mAdapter;
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};
    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected int layoutById() {
        return R.layout.activity_my_page;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        lvMyPage = (ListView) findViewById(R.id.lv_myPage);
    }

    @Override
    protected void setEvents() {
        lvMyPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyPageActivity.this,"Item click at position : "+position, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void initData() {
        mAdapter = new MyPageAdapter(this, mobileArray);
        lvMyPage.setAdapter(mAdapter);
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    void getDataIsConnectInternet(String URL){
        RequestParams params = new RequestParams();
        params.put("uuid", "uuid");
        client.post(this, URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "Success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "Failure");
            }
        });
    }
}
