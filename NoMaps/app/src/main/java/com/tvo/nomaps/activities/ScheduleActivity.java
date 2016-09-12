package com.tvo.nomaps.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;

/**
 * Created by huy on 9/12/2016.
 */
public class ScheduleActivity extends BaseActivity {
    private static final String TAG = "ScheduleActivity";
    private Button btnHome;
    private ImageView imgToday, imgTomorrow;
    @Override
    protected int layoutById() {
        return R.layout.activity_schedule;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnHome = (Button) findViewById(R.id.activity_schedule_btnHome);
        imgToday = (ImageView) findViewById(R.id.image_today_schedule);
        imgTomorrow = (ImageView) findViewById(R.id.image_tomorrow_schedule);
    }

    @Override
    protected void setEvents() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected String TAG() {
        return null;
    }
}
