package com.tvo.nomaps.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;

/**
 * Created by huy on 9/6/2016.
 */
public class EndServiceActivity extends BaseActivity implements View.OnClickListener {

    private Button btnFootprint, btnDownload;

    @Override
    protected int layoutById() {
        return R.layout.end_service;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnFootprint = (Button) findViewById(R.id.end_activity_btnFootprint);
        btnDownload = (Button) findViewById(R.id.btnDocDownload);
    }

    @Override
    protected void setEvents() {
        btnFootprint.setOnClickListener(this);
        btnDownload.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String TAG() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
