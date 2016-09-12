package com.tvo.nomaps.activities;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tvo.nomaps.abstracts.BaseActivity;

/**
 * Created by huy on 9/1/2016.
 */
public class NfcEventListener extends BaseActivity{
    private final String TAG = NfcEventListener.class.toString();
    public static final String KEY_EXTRA_NFC = "url_nfc_code";

    public void foundUri(String foundUrl) {
        Log.d(TAG, "foundUri: ");
        if (foundUrl != null){
            Intent newActivity1 = new Intent(this, WebViewActivity.class);
            newActivity1.putExtra(KEY_EXTRA_NFC, foundUrl);
            startActivity(newActivity1);
        }
//        Log.e(TAG,foundUrl);
//        return foundUrl;
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
