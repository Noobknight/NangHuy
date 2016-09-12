package com.tvo.nomaps.activities;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.mode.APIMode;
import com.tvo.nomaps.utils.Utils;

import cz.msebera.android.httpclient.Header;

/**
 * Created by huy on 8/31/2016.
 */
public class LicensedActivity extends BaseActivity {
    private final String TAG = LicensedActivity.class.getSimpleName();

    Button btnAccept, btnNoAccept;
    ScrollView scrollLicense;

    @Override
    protected int layoutById() {
        return R.layout.screen_licensed;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnAccept = (Button) findViewById(R.id.screen_lisensed_btnAccept);
        btnNoAccept = (Button) findViewById(R.id.screen_lisensed_btnNoAccept);
        scrollLicense = (ScrollView) findViewById(R.id.scrollLicense);
        btnAccept.setEnabled(false);
    }

    @Override
    protected void setEvents() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
                SharedPreferences.Editor edit = pre.edit();
                edit.putString("check", "true");
                edit.commit();
                gotoRegisterActivity();
            }
        });
        btnNoAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });
        scrollLicense.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int totalHeight = scrollLicense.getChildAt(0).getHeight();
                int diff = (totalHeight-(scrollLicense.getBottom()+scrollLicense.getScrollY()));
                if (diff < 0 )
                {
                    btnAccept.setEnabled(true);
                }
            }
        });
    }

    private void gotoRegisterActivity() {
//        Intent intent =  new Intent(this, RegisterActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        Utils.gotoActivity(this,RegisterActivity.class);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected String TAG() {
        return null;
    }


}
