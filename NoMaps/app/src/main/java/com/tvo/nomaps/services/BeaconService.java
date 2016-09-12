package com.tvo.nomaps.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by huy on 9/5/2016.
 */
public class BeaconService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        intent.getAction()

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
