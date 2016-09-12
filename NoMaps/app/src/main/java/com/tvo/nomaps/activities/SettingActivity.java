package com.tvo.nomaps.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by huy on 8/31/2016.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "SettingActivity";
    private Button btnViewLicensed, activity_settings_btnRegister, btnHome;
    private TextView txtStateBlu, txtStateNFC;

    protected int layoutById() {
        return R.layout.activity_settings;
    }

    protected void initView(@Nullable Bundle savedInstanceState) {
        btnViewLicensed = (Button) findViewById(R.id.activity_settings_btnViewLicensed);
        txtStateBlu = (TextView) findViewById(R.id.activity_settings_txtStateBlu);
        txtStateNFC = (TextView) findViewById(R.id.activity_settings_txtStateNFC);
        activity_settings_btnRegister = (Button) findViewById(R.id.activity_settings_btnRegister);
        btnHome = (Button) findViewById(R.id.activity_setting_btnHome);

    }

    @Override
    protected void setEvents() {
        btnViewLicensed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertLicense();
            }
        });
        activity_settings_btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterActivity();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtStateBlu.setOnClickListener(this);
        txtStateNFC.setOnClickListener(this);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected String TAG() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_settings_txtStateBlu:
                startActivityForResult(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0);
                checkStatusBlueTooth();
                break;
            case R.id.activity_settings_txtStateNFC:
                if (checkSpNfc()){
                    startActivityForResult(new Intent(Settings.ACTION_NFC_SETTINGS), 0);
                    checkStatusNFC();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatusBlueTooth();
        checkStatusNFC();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void checkStatusBlueTooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_bluetooth_OFF));
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), wordtoSpan.length() - 3, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtStateBlu.setText(wordtoSpan);
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_bluetooth_ON));
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), wordtoSpan.length() - 2, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtStateBlu.setText(wordtoSpan);
            } else {
                Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_bluetooth_OFF));
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), wordtoSpan.length() - 3, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtStateBlu.setText(wordtoSpan);
            }
        }
    }

    void checkStatusNFC() {
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null) {
            // adapter exists
            if (adapter.isEnabled()) {
                Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_nfc_ON));
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), wordtoSpan.length() - 2, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtStateNFC.setText(wordtoSpan);
            } else {
                Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_nfc_OFF));
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), wordtoSpan.length() - 3, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtStateNFC.setText(wordtoSpan);
            }
        }
        else {
            Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_nfc_OFF));
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), wordtoSpan.length() - 3, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtStateNFC.setText(wordtoSpan);
        }
    }

    Boolean checkSpNfc(){
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null) {
           return true;
        }
        else {
            return false;
        }
    }

    private void gotoRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    void showAlertLicense() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setPositiveButton(R.string.btn_skip, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                });
        builder.setTitle(getResources().getString(R.string.open_source_licensed));
        builder.setMessage(getResources().getString(R.string.licensed_content));
        builder.show();
    }
}
