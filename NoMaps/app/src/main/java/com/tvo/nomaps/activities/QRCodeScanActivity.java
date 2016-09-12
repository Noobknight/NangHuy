package com.tvo.nomaps.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by Tavv
 * on 27/08/2016.
 */
public class QRCodeScanActivity extends BaseActivity implements QRCodeView.Delegate{
    private final String TAG = "QRCodeScanActivity";
    private static final String FLASH_STATE = "FLASH_STATE";
    public static final String KEY_EXTRA = "url_qr_code";
    private Button btnBack;
    private QRCodeView mQrCodeView;
    private ImageView textView;
    RelativeLayout naviLayout;
    Paint paint = new Paint();

    @Override
    protected int layoutById() {
        return R.layout.activity_qrcode_scan;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        naviLayout = (RelativeLayout) findViewById(R.id.navgation_bar);
        DisplayMetrics dm = new DisplayMetrics();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, - dm.heightPixels, 0, 0);
        naviLayout.setLayoutParams(params);

        mQrCodeView = (ZBarView) findViewById(R.id.zbarview);
        btnBack = (Button) findViewById(R.id.activity_qrcode_btnBack);
        mQrCodeView.setDelegate(this);
        mQrCodeView.startSpot();

    }

    @Override
    protected void setEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected String TAG() {
        return TAG;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mQrCodeView.startCamera();
        mQrCodeView.startSpotAndShowRect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mQrCodeView.stopCamera();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            vibrate();
            if (Utils.checkURL(result)) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(KEY_EXTRA, result);
                startActivity(intent);
            }else{
                Toast.makeText(QRCodeScanActivity.this, "No content", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mQrCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    protected void onDestroy() {
        mQrCodeView.onDestroy();
        super.onDestroy();
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
