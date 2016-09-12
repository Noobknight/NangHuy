package com.tvo.nomaps.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

import io.tangerine.beaconreceiver.android.sdk.application.BeaconReceiver;
import io.tangerine.beaconreceiver.android.sdk.application.HandsetId;

/**
 * Created by huy on 9/6/2016.
 */
public class LaunchScreen extends BaseActivity {
    private static final String TAG = "LaunchScreen";

    @Override
    protected int layoutById() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void initData() {
        int version = android.os.Build.VERSION.SDK_INT;
        String release = Build.VERSION.RELEASE;
        Log.d(TAG, "Version: "+ version + "Verson release: " + release);
    }
    void gotoTop(){
        SharedPreferences pre=getSharedPreferences ("my_data",MODE_PRIVATE);
        String user = pre.getString("uuid", "");
        Log.d(TAG,user);
        if (user.equals("")){
//            Intent intent =  new Intent(this, LicensedActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            Utils.gotoActivity(this,LicensedActivity.class);
        }
        else{
//            Intent intent =  new Intent(this, TopActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            Utils.gotoActivity(this,TopActivity.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected String TAG() {
        return null;
    }

}
