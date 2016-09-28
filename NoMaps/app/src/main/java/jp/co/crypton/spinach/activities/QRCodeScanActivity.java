package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.Constants;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import jp.co.crypton.spinach.R;

/**
 * Created by Tavv
 * on 27/08/2016.
 */
public class QRCodeScanActivity extends BaseActivity implements QRCodeView.Delegate {
    private final String TAG = "QRCodeScanActivity";
    private static final String FLASH_STATE = "FLASH_STATE";
    public static final String KEY_EXTRA = "url_qr_code";
    private QRCodeView mQrCodeView;
    private Button btnBack;

    @Override
    protected int layoutById() {
        return R.layout.activity_qrcode_scan;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mQrCodeView = (ZBarView) findViewById(R.id.zbarview);
        mQrCodeView.setDelegate(this);
        mQrCodeView.startSpot();
        btnBack = (Button) findViewById(R.id.activity_qrcode_btnBack);
    }

    @Override
    protected void setEvents() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        prepareNfc(null);
    }

    @Override
    protected void initData() {
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, "http://plate.id/NM10012Q1122166");
//        startActivity(intent);
//        finish();
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
            if (result.startsWith(Constants.QR_CODE_URL_PREFIX)) {
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, result);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(QRCodeScanActivity.this, getResources().getString(R.string.txt_cannot_qr), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(QRCodeScanActivity.this, "Can Not NoMaps QR code", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                finish();
                break;
        }
        return true;
    }
}
