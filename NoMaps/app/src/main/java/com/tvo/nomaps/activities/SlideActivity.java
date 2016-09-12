package com.tvo.nomaps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;

/**
 * Created by huy on 8/31/2016.
 */
public class SlideActivity extends BaseActivity {
    @Override
    protected int layoutById() {
        return R.layout.activity_slide;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String TAG() {
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
//
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    gotoTop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private void gotoTop() {
        startActivity(new Intent(this, TopActivity.class));
    }
}
