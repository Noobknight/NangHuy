package com.tvo.nomaps.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tvo.nomaps.Helpers.PostAPI;
import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

import java.util.UUID;

import cz.msebera.android.httpclient.Header;

/**
 * Created by huy on 8/31/2016.
 */
public class RegisterActivity extends BaseActivity {
    private final String TAG = "RegisterActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;

    Button btnSkip;
    @Override
    protected int layoutById() {
        return R.layout.screen_register;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnSkip = (Button) findViewById(R.id.screen_register_btnSkip);
    }

    @Override
    protected void setEvents() {
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterActivity();
            }
        });
    }

    @Override
    protected void initData() {
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        String user = pre.getString("uuid", "");
        if (user.equals("")){
            String id = UUID.randomUUID().toString();
            SharedPreferences.Editor edit = pre.edit();
            edit.putString("uuid", id);
            edit.commit();
        }
        Log.d(TAG, user);
//        String url = "";
//        postAPIUUID(url,user);
    }

    @Override
    protected String TAG() {
        return null;
    }

    private void gotoRegisterActivity(){
        Utils.launchActivity(RegisterActivity.this, TopActivity.class, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }
    //Funtion post UUID
    void postAPIUUID(String URL, String uuid){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Please grant phone state permission to use the Beacon", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
