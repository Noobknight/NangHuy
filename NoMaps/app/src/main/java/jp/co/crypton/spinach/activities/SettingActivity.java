package jp.co.crypton.spinach.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.refactor.lib.colordialog.PromptDialog;
import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.Constants;


/**
 * Created by huy on 8/31/2016.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "SettingActivity";
    private Button btnViewLicensed, activity_settings_btnRegister;
    private TextView txtStateBlu, txtStateNFC, testCookie;
    public static final String INTENT_SETTING_REGISTER = "SETTING_REGISTER";
    private CheckURLVali checkURLVali = new CheckURLVali();

    protected int layoutById() {
        return R.layout.activity_settings;
    }

    protected void initView(@Nullable Bundle savedInstanceState) {
        btnViewLicensed = (Button) findViewById(R.id.activity_settings_btnViewLicensed);
        txtStateBlu = (TextView) findViewById(R.id.activity_settings_txtStateBlu);
        txtStateNFC = (TextView) findViewById(R.id.activity_settings_txtStateNFC);
//        testCookie = (TextView) findViewById(R.id.testCookies);
        activity_settings_btnRegister = (Button) findViewById(R.id.activity_settings_btnRegister);
//        testCookie.setText("amsa"+CommonPrefs.getCookieTapcm()+"okoto");
    }

    @Override
    protected void setEvents() {
        btnViewLicensed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAlertLicense();
                showAlertColorLicense();
            }
        });
        activity_settings_btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterActivity();
            }
        });
        txtStateBlu.setOnClickListener(this);
        txtStateNFC.setOnClickListener(this);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prepareNfc(new NfcEventListener() {
            @Override
            public void foundUri(String uri) {
                showWebview(uri, Constants.WEBVIEW_TYPE_SP);
            }
        });
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
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter != null) {
                    startActivityForResult(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0);
                    checkStatusBlueTooth();
                }
                break;
            case R.id.activity_settings_txtStateNFC:
                if (checkSpNfc()) {
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
//            Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_bluetooth_OFF));
//            wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), wordtoSpan.length() - 3, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            txtStateBlu.setText(wordtoSpan);
            txtStateBlu.setText(getResources().getString(R.string.state_bluetooth_OFF));
            txtStateBlu.setTextColor(Color.parseColor("#e7e6e6"));
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
        } else {
//            Spannable wordtoSpan = new SpannableString(getResources().getString(R.string.state_nfc_OFF));
//            wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), wordtoSpan.length() - 3, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            txtStateNFC.setText(wordtoSpan);
            txtStateNFC.setText(getResources().getString(R.string.state_nfc_OFF));
            txtStateNFC.setTextColor(Color.parseColor("#e7e6e6"));
        }
    }

    Boolean checkSpNfc() {
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null) {
            return true;
        } else {
            return false;
        }
    }

    private void gotoRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_SETTING_REGISTER, "Key");
        startActivity(intent);
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

    void showAlertColorLicense() {
        PromptDialog dialog = new PromptDialog(this);
        dialog.setDialogType(PromptDialog.DIALOG_TYPE_INFO);
        dialog.setAnimationEnable(true);
        dialog.setTitleText(getResources().getString(R.string.open_source_licensed))
                .setContentText(getResources().getString(R.string.licensed_content));
        dialog.setPositiveListener(R.string.btn_skip, new PromptDialog.OnPositiveListener() {
            @Override
            public void onClick(PromptDialog dialog) {
                dialog.dismiss();
            }
        }).show();
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

    private void showWebview(String url, int type) {
        Intent newActivity = new Intent(this, WebViewActivity.class);
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, checkURLVali.slipURI(url));
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_TYPE, type);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newActivity);
    }
}
