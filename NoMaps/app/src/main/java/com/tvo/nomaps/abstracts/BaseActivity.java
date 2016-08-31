package com.tvo.nomaps.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Tavv
 * on 27/08/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutById());
        TAG();
        initView(savedInstanceState);
        setEvents();
        initData();
    }
    /*Function using for set id layout */
    protected abstract int layoutById();
    /*Function using for init views*/
    protected abstract void initView(@Nullable Bundle savedInstanceState);
    /*init event for views*/
    protected abstract void setEvents();
    /*Function using init data for views */
    protected abstract void initData();
    /*Function using log class Tag */
    protected abstract String TAG();

}