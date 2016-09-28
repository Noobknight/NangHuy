package jp.co.crypton.spinach.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Locale;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.msebera.android.httpclient.Header;
import io.tangerine.beaconreceiver.android.sdk.service.BeaconAction;
import io.tangerine.beaconreceiver.android.sdk.service.BeaconAreaEvent;
import io.tangerine.beaconreceiver.android.sdk.service.BeaconReceiverActionList;
import io.tangerine.beaconreceiver.android.sdk.service.BeaconReceiverPacket;
import io.tangerine.beaconreceiver.android.sdk.service.BeaconReceiverServiceApi;
import io.tangerine.beaconreceiver.android.sdk.service.BeaconReceiverServiceListener;
import io.tangerine.beaconreceiver.android.sdk.service.IBeaconPacket;
import io.tangerine.beaconreceiver.android.sdk.service.ServiceTask;
import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;

import io.tangerine.beaconreceiver.android.sdk.application.BeaconReceiver;
import io.tangerine.beaconreceiver.android.sdk.service.BeaconReceiverService;

import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.model.Boot;
import jp.co.crypton.spinach.model.BootModel;
import jp.co.crypton.spinach.model.Footprint;
import jp.co.crypton.spinach.model.ListData;
import jp.co.crypton.spinach.model.Status;

/**
 * Created by huy on 9/6/2016.
 */
public class LaunchScreen extends BaseActivity {
    private static final String TAG = "LaunchScreen";
    private BeaconReceiver mBeaconReceiver;
    private String nfcUrl;

    @Override
    protected int layoutById() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void setEvents() {
        prepareNfc(null);
    }

    @Override
    protected void initData() {
        getDataIsConnectInternet();
    }

    void gotoTop() {
        boolean agree = CommonPrefs.isAgree();
        if (agree) {
            Intent i = new Intent(this, TopActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Constants.INTENT_KEY_NFC_URL, nfcUrl);
            startActivity(i);
        } else {
            Intent i = new Intent(this, LicensedActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Constants.INTENT_KEY_NFC_URL, nfcUrl);
            startActivity(i);
        }
        finish();

    }

    @Override
    protected String TAG() {
        return null;
    }

    void getDataIsConnectInternet() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.URL_BOOT, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "AsyncHttpClient onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String data = new String(response);
                Log.d(TAG, data);

                BootModel model = new Gson().fromJson(data, BootModel.class);
                for (Boot item : model.getBoot()) {
                    boolean isJp = false;
                    if (Locale.getDefault().equals(Locale.JAPAN) || Locale.getDefault().equals(Locale.JAPANESE)) {
                        isJp = true;
                    }
                    String url = isJp ? item.getMsgJa() : item.getMsgEn();
                    if (item.getState()== 0)
                    {
                        Log.d(TAG, "onSuccess: "+item.getState());
                        gotoNextPage();
                    }
                    else {
//                        new AlertDialog.Builder(LaunchScreen.this)
//                                .setMessage(url)
//                                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        finish();
//                                    }
//                                })
//                                .show();
                        showAlertColorLicense(url);
//                        finish();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                gotoNextPage();
                Log.d(TAG, "AsyncHttpClient onFailure");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "onRetry: ");
            }
        });
    }
    void showAlertColorLicense(String message) {
        PromptDialog dialog = new PromptDialog(this);
        dialog.setDialogType(PromptDialog.DIALOG_TYPE_INFO);
//        dialog.setAnimationEnable(true);
        dialog.setContentText(message);
        dialog.setPositiveListener(R.string.btn_close, new PromptDialog.OnPositiveListener() {
            @Override
            public void onClick(PromptDialog dialog) {
                dialog.dismiss();
                finish();
            }
        }).show();
    }
    void gotoNextPage(){
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                CheckURLVali checkURLVali = new CheckURLVali();
                Log.d(TAG, "initData: " + checkURLVali.slipURI(data.toString()));
            }
        }
        nfcUrl = getIntent().getDataString();
//        Log.d(TAG, "gotoNextPage: ");
        if (nfcUrl != null && nfcUrl.startsWith(Constants.NOMAPS_URL_PREFIX)) {// Toast.makeText(this, foundUrl, Toast.LENGTH_SHORT).show();
            gotoTop();
            return;
        }

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
}
