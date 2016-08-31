package com.tvo.nomaps.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

/**
 * Created by Tavv
 * on 29/08/2016.
 */
public class TopActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "TopActivity";

    private Button btnStartScanQR, btnCurrentPosition, btnSettings,
            btnMyPage, btnInfoUtilities;

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    @Override
    protected int layoutById() {
        return R.layout.activity_top;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnStartScanQR = (Button) findViewById(R.id.btnStartQrCode);
        btnCurrentPosition = (Button) findViewById(R.id.btnCurrentPosition);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnMyPage = (Button) findViewById(R.id.btnMyPage);
        btnInfoUtilities = (Button) findViewById(R.id.btnInfoUtilities);
    }

    @Override
    protected void setEvents() {
        btnStartScanQR.setOnClickListener(this);
        btnCurrentPosition.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnMyPage.setOnClickListener(this);
        btnInfoUtilities.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //Do something
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartQrCode:
                Utils.launchActivity(this, QRCodeScanActivity.class, ZXING_CAMERA_PERMISSION);
                break;
            case R.id.btnCurrentPosition:
                break;
            case R.id.btnSettings:
                break;
            case R.id.btnMyPage:
                break;
            case R.id.btnInfoUtilities:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
