package com.tvo.nomaps.Helpers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tvo.nomaps.abstracts.BaseActivity;

import java.util.UUID;

/**
 * Created by huy on 9/1/2016.
 */
public class PostAPI extends BaseActivity {
    void postUUID() {

    }

    void postRegister() {

    }

    public String getUUIDRandom() {
        String id = UUID.randomUUID().toString();
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        String user = pre.getString("uuid", id);
        return id;
    }

    @Override
    protected int layoutById() {
        return 0;
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
}
