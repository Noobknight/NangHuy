package jp.co.crypton.spinach.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.Footprint;
import jp.co.crypton.spinach.model.ListData;
import jp.co.crypton.spinach.model.Status;
import jp.co.crypton.spinach.services.BeaconService;
import jp.co.crypton.spinach.utils.Utils;


/**
 * Created by huy on 8/31/2016.
 */
public class RegisterActivity extends BaseActivity {
    private final String TAG = "RegisterActivity";
    private WebView mWebView;
    private final String registerUrl = "https://www7.webcas.net/form/pub/nomaps/nomaps01";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    Button btnSkip;

    @Override
    protected int layoutById() {
        return R.layout.screen_register;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnSkip = (Button) findViewById(R.id.screen_register_btnSkip);
        mWebView = (WebView) findViewById(R.id.register_webView);
    }

    @Override
    protected void setEvents() {
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > 17) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                if (!Utils.isServiceRunning(this, TopActivity.class)) {
                    stopService(new Intent(RegisterActivity.this, BeaconService.class));
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT > 17) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                startService(new Intent(RegisterActivity.this, BeaconService.class));

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void initData() {
        String uuid = CommonPrefs.getUUID();
        String handsetId = CommonPrefs.getHandsetId();
        String url = registerUrl + "?appliUUID=" + uuid + "&handsetID=" + handsetId;
        mWebView.loadUrl(url);
    }

    @Override
    protected String TAG() {
        return null;
    }

    private void gotoRegisterActivity() {

        Intent intent = getIntent();
        if (intent.getStringExtra(SettingActivity.INTENT_SETTING_REGISTER) != null) {
            finish();
            return;
        }
        String nfcUrl = null;
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nfcUrl = extra.getString(Constants.INTENT_KEY_NFC_URL);
        }
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Intent i = new Intent(this, TopActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Constants.INTENT_KEY_NFC_URL, nfcUrl);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
//        if (!bluetooth.isEnabled()) {
//            Toast.makeText(getApplicationContext(),
//                    "Turning ON Bluetooth", Toast.LENGTH_LONG);
//            // Intent enableBtIntent = new
//            // Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(new Intent(
//                    BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
//        }
        //Enable bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if( mBluetoothAdapter != null){
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Please grant phone state permission to use the Beacon", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}
