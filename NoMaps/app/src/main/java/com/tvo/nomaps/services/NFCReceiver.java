package com.tvo.nomaps.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tvo.nomaps.constants.Actions;

/**
 * Created by huy on 9/5/2016.
 */
public class NFCReceiver extends BroadcastReceiver {

    public NFCReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.i("NFCReceiver", "onReceive: " + intent);
            String result = intent.getStringExtra("data");
            Log.i("NFCReceiver", "onReceive: " + result);
        }
    }
}
