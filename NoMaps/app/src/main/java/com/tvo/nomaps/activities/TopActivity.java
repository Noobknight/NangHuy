package com.tvo.nomaps.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.tvo.nomaps.Helpers.CheckURLVali;
import com.tvo.nomaps.Helpers.GooglePlayServiceLocationManager;
import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import io.tangerine.beaconreceiver.android.sdk.application.ActionType;
import io.tangerine.beaconreceiver.android.sdk.application.BeaconReceiver;
import io.tangerine.beaconreceiver.android.sdk.application.BeaconReceiverListener;
import io.tangerine.beaconreceiver.android.sdk.application.ErrorType;
import io.tangerine.beaconreceiver.android.sdk.application.LocationManager;

/**
 * Created by Tavv
 * on 29/08/2016.
 */
public class TopActivity extends BaseActivity implements View.OnClickListener, BeaconReceiverListener {
    private final String TAG = "TopActivity";
    public static final String KEY_EXTRA_BEACON = "url_beacon_code";
    private CheckURLVali checkURLVali = new CheckURLVali();
    private Button btnStartScanQR, btnCurrentPosition, btnSettings,
            btnFootpring, btnTimeTable, btnStampRally, btnOfficialWebsite, btnFB, btnTwitter;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private BeaconReceiver mBeaconReceiver;
    private LocationManager mLocationManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected int layoutById() {
        return R.layout.activity_top;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnStartScanQR = (Button) findViewById(R.id.btnStartQrCode);
        btnCurrentPosition = (Button) findViewById(R.id.btnCurrentPosition);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnFootpring = (Button) findViewById(R.id.btnFootprint);
        btnStampRally = (Button) findViewById(R.id.btnStampRally);
        btnTimeTable = (Button) findViewById(R.id.btnTimeTable);
//        btnOfficialWebsite = (Button) findViewById(R.id.btnOfficialWebsite);
        btnFB = (Button) findViewById(R.id.top_btn_facebook);
        btnTwitter = (Button) findViewById(R.id.top_btn_twitter);
    }

    @Override
    protected void setEvents() {
        btnStartScanQR.setOnClickListener(this);
        btnCurrentPosition.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnFootpring.setOnClickListener(this);
        btnTimeTable.setOnClickListener(this);
        btnStampRally.setOnClickListener(this);
//        btnOfficialWebsite.setOnClickListener(this);
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkTwitter();
            }
        });
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkFacebook();
            }
        });
    }

    @Override
    protected void initData() {
        initBeaconReceiver();
        if (mBeaconReceiver != null) {
            mBeaconReceiver.startScanning();
        }
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                Intent newActivity = new Intent(this, WebViewActivity.class);
                newActivity.putExtra(KEY_EXTRA_NFC, checkURLVali.slipURI(data.toString()));
                startActivity(newActivity);
            }
        }
        checkTimeService();
        prepareNfc(new NfcEventListener() {
            @Override
            public void foundUri(String uri) {
//                return null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            Intent newActivity = new Intent(this, WebViewActivity.class);
            newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            newActivity.putExtra(KEY_EXTRA_NFC, checkURLVali.slipURI(data.toString()));
            startActivity(newActivity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartQrCode:
                checkGotoQRCode();
                break;
            case R.id.btnCurrentPosition:
                showAlertPosition();
                break;
            case R.id.btnSettings:
                Intent i = new Intent(this, SettingActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.btnFootprint:
                i = new Intent(this, MyPageActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.btnTimeTable:
                i = new Intent(this, ScheduleActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            case R.id.btnStampRally:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
        }
    }


    void showAlertPosition() {
        String url = "https://no-maps.jp/access";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    void linkFacebook() {
        String url = "https://www.facebook.com/NoMaps.jp/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    void linkTwitter() {
        String url = "https://twitter.com/no_maps";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    void showOfficialWebsite() {
        String url = "https://no-maps.jp/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //check time service 10/10 - 16/10
    void checkTimeService() {
        String valid_until_start = "16/10/2016";
        String valid_until_end = "16/10/2016";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDateStart = null;
        Date strDateEnd = null;
        try {
            strDateStart = sdf.parse(valid_until_start);
            strDateEnd = sdf.parse(valid_until_end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!((new Date().before(strDateEnd)) && (new Date().after(strDateStart)))) {
//            Intent i = new Intent(this, EndServiceActivity.class);
//            startActivity(i);
        }
    }

    //show alert advice NFC
    void showAlertAdviceNFC() {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(this);
        }
        builder.setPositiveButton(R.string.btn_skip, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Utils.launchActivity(TopActivity.this, QRCodeScanActivity.class, ZXING_CAMERA_PERMISSION);
            }
        });
        builder.setMessage(getResources().getString(R.string.txt_message_recommen_nfc));
        builder.show();
    }

    void checkGotoQRCode() {
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            showAlertAdviceNFC();
        } else {
            Utils.launchActivity(TopActivity.this, QRCodeScanActivity.class, ZXING_CAMERA_PERMISSION);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEnterAction(String eventId, HashMap params, int rssi) {
        String url = params.get("uuid").toString();
        if (url != null && !url.equals("")) {
            Intent newActivity = new Intent(this, WebViewActivity.class);
            newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            newActivity.putExtra(KEY_EXTRA_NFC, url);
            startActivity(newActivity);
        }

    }

    @Override
    public void onEnterAction(String s, String s1, Bitmap bitmap, Integer integer, int i) {

    }

    @Override
    public void onExitAction(String s, String s1, Bitmap bitmap, Integer integer, int i) {

    }

    @Override
    public void onExitAction(String s, HashMap hashMap, int i) {

    }

    @Override
    public void onError(ErrorType errorType, String s) {

    }


    private void initBeaconReceiver() {
        SharedPreferences sharedPreferences = getSharedPreferences("preference_beacon_receiver", Context.MODE_PRIVATE);
//		String preference_application_id = sharedPreferences.getString("preference_application_id", "");
//		String preference_developer_id = sharedPreferences.getString("preference_developer_id", "");
        String preference_application_id = "82";
        String preference_developer_id = "61";
        if (isNumber(preference_application_id) && isNumber(preference_developer_id)) {
            int applicationId = Integer.valueOf(preference_application_id);
            int developerId = Integer.valueOf(preference_developer_id);

            if (mBeaconReceiver == null) {
                mBeaconReceiver = BeaconReceiver.getInstance(this, this);
                mBeaconReceiver.setDebugEnable(false);
                mBeaconReceiver.setDoActionPeriod(2);
                mLocationManager = new GooglePlayServiceLocationManager(this);
                mBeaconReceiver.setLocationManager(mLocationManager);
                mBeaconReceiver.initialize(applicationId, developerId, ActionType.All.getId());
            }
        } else {
            //application ID and developer ID error
        }
    }

    private boolean isNumber(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException nfex) {
            return false;
        }
    }

}
