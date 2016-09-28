package jp.co.crypton.spinach.services;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.tangerine.beaconreceiver.android.sdk.application.ActionType;
import io.tangerine.beaconreceiver.android.sdk.application.BeaconReceiver;
import io.tangerine.beaconreceiver.android.sdk.application.BeaconReceiverListener;
import io.tangerine.beaconreceiver.android.sdk.application.ErrorType;
import io.tangerine.beaconreceiver.android.sdk.application.LocationManager;
import jp.co.crypton.spinach.Helpers.GooglePlayServiceLocationManager;
import jp.co.crypton.spinach.constants.Actions;

/**
 * Created by huy on 9/16/2016.
 */
@TargetApi(18)
public class BeaconService extends Service implements BeaconReceiverListener {
    private static final String TAG = "BeaconService";
    private BeaconReceiver mBeaconReceiver;
    private LocalBroadcastManager mBroadcastManager;
    private BeaconStateReceiver mStateReceiver;

    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 3000;
    int count = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mStateReceiver = new BeaconStateReceiver();
        IntentFilter intentFilter = new IntentFilter(Actions.BEACON_ACTION);
        mBroadcastManager.registerReceiver(mStateReceiver, intentFilter);
        initBeaconService();
        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            btAdapter = btManager.getAdapter();
        }

    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            if (btAdapter != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    btAdapter.startLeScan(leScanCallback);
                }
            }
            Log.d(TAG, "run: " + count);
            SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
            String tapcm = pre.getString("beaconID", "");
            Log.d(TAG, "runaa: " + tapcm);
            if (count > 3) {
                tapcm = pre.getString("beaconID", "");
//                Log.d(TAG, "runbb: " + tapcm);
                Log.d(TAG, "runaaaa: " + tapcm);
                setUrlBeacon();
            }
            count++;
            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5) {
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound) {
                count = 0;
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                Log.i(TAG, "UUID: " + uuid + "\\nmajor: " + major + "\\nminor" + minor);

            }
        }
    };
    /**
     * bytesToHex method
     */
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String valid_until_start = "10/10/2016";
        String valid_until_end = "17/10/2016 08:00";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
//        Date strDateStart = null;
        Date strDateEnd = null;
        try {
//            strDateStart = sdf.parse(valid_until_start);
            strDateEnd = sdf.parse(valid_until_end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (((new Date().before(strDateEnd)))) {
            mBeaconReceiver.startScanning();
            scanHandler.post(scanRunnable);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        scanHandler.removeCallbacks(scanRunnable);
        mBroadcastManager.unregisterReceiver(mStateReceiver);
        mBeaconReceiver.release();
    }


    @Override
    public void onEnterAction(String title, String description, Bitmap image, Integer actionId, int rssi) {
        Log.d(TAG, "onEnterAction actionID: " + actionId);
    }

    @Override
    public void onExitAction(String title, String description, Bitmap image, Integer actionId, int rssi) {
        String displayText = "title=" + title + ", description= " + description + ", actionId=" + actionId.toString() + ", rssi=" + rssi;
        Log.i(TAG, "[onExitAction" + new Date() + "]" + displayText);
        setUrlBeacon();
    }

    @Override
    public void onEnterAction(String eventId, HashMap params, int rssi) {
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        String tapcm = pre.getString("beaconID", "");
//        String title = pre.getString("displayTitle", "");
//        String mess = pre.getString("displayText", "");

        Log.i(TAG, "[onEnterAction" + params + "rssi: " + rssi+" eventId: "+eventId);

        if (tapcm.equals("") || (!tapcm.equals(params.get("actionID").toString()))) {
            SharedPreferences.Editor edit = pre.edit();
            edit.putString("beaconID", params.get("actionID").toString());
//            edit.putString("displayTitle", params.get("displayTitle").toString());
//            edit.putString("displayText", params.get("displayText").toString());
            edit.commit();
            Intent intent = new Intent(Actions.BEACON_ACTION);
            intent.putExtra("info", params);
            mBroadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    public void onExitAction(String eventId, HashMap params, int rssi) {
        String displayText = "eventId=" + eventId + "params=" + params.toString() + ", rssi=" + rssi;
        Log.i(TAG, "[onExitAction" + new Date() + "]" + displayText);
        setUrlBeacon();
    }

    @Override
    public void onError(ErrorType beaconReceiverErrorType, String message) {
        Log.i(TAG, "onError" + new Date() + "]" + "[" + beaconReceiverErrorType.getName() + "] " + message);
        setUrlBeacon();
    }

    private void initBeaconService() {
        String preference_application_id = "82";
        String preference_developer_id = "61";

        if (isNumber(preference_application_id) && isNumber(preference_developer_id)) {
            int applicationId = Integer.valueOf(preference_application_id);
            int developerId = Integer.valueOf(preference_developer_id);

            if (mBeaconReceiver == null) {
                Log.i(TAG, "initBeaconService: [ drvice:" + Build.MODEL + "]");
                // Use this check to determine whether BLE is supported on the device
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    Log.i(TAG, "initBeaconService: [Initialize SDK]");
                    mBeaconReceiver = BeaconReceiver.getInstance(this, this);
                    mBeaconReceiver.setDebugEnable(true);
                    mBeaconReceiver.setDoActionPeriod(60);
                    LocationManager mLocationManager = new GooglePlayServiceLocationManager(this);
                    mBeaconReceiver.setLocationManager(mLocationManager);
                    mBeaconReceiver.initialize(applicationId, developerId, ActionType.All.getId());
                } else {
                    Log.i(TAG, "BLE is not supported on this device.");
                }
            }
        } else {
            Log.i(TAG, "メニュー→設定→applicationID、developerIDを入力してください。");
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

    void setUrlBeacon() {
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        SharedPreferences.Editor edit = pre.edit();
        edit.putString("beaconID", "");
        edit.commit();
    }

}
