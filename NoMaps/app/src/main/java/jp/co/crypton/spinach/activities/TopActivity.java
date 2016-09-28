package jp.co.crypton.spinach.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.refactor.lib.colordialog.PromptDialog;
import jp.co.crypton.spinach.Helpers.Api;
import jp.co.crypton.spinach.Helpers.ApiCallback;
import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.TopInfo;
import jp.co.crypton.spinach.services.AlarmReceiver;
import jp.co.crypton.spinach.services.BeaconService;
import jp.co.crypton.spinach.utils.Utils;

/**
 * Created by Tavv
 * on 29/08/2016.
 */
public class TopActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "TopActivity";
    private CheckURLVali checkURLVali = new CheckURLVali();
    private Button btnStartScanQR, btnCurrentPosition, btnSettings,
            btnFootpring, btnTimeTable, btnOfficialWebsite, btnFB, btnTwitter;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private LinearLayout infoLay, infoListLay;
    private Api api;
    private LinearLayout welcomeLay;
    String url;
    private TextView textWelcom;

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

        btnTimeTable = (Button) findViewById(R.id.btnTimeTable);
        btnOfficialWebsite = (Button) findViewById(R.id.btnOfficialWebsite);
        btnFB = (Button) findViewById(R.id.top_btn_facebook);
        btnTwitter = (Button) findViewById(R.id.top_btn_twitter);

//        textWelcom = (TextView) findViewById(R.id.welcom_top);
        infoLay = (LinearLayout) findViewById(R.id.info_lay);
        infoListLay = (LinearLayout) findViewById(R.id.info_list_lay);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Alarm");
        if (name != null) {
            intent = new Intent(this, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(sender);
        }

        boolean notify = CommonPrefs.getKeyNotify();
        if (!notify)
            Utils.setAlarmService(this);
    }

    @Override
    protected void setEvents() {
        btnStartScanQR.setOnClickListener(this);
        btnCurrentPosition.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnFootpring.setOnClickListener(this);
        btnTimeTable.setOnClickListener(this);
        btnOfficialWebsite.setOnClickListener(this);
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
        Log.d(TAG, "initData: ");
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String nfcUrl = extra.getString(Constants.INTENT_KEY_NFC_URL);
            if (nfcUrl != null && !"".equals(nfcUrl)) {
                showWebview(nfcUrl, Constants.WEBVIEW_TYPE_SP);
            }
        }
        checkTimeService();
        prepareNfc(new NfcEventListener() {
            @Override
            public void foundUri(String uri) {
                showWebview(uri, Constants.WEBVIEW_TYPE_SP);
            }
        });
        receiveBeacon();
    }

    private void showWebview(String url, int type) {
        Intent newActivity = new Intent(this, WebViewActivity.class);
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, checkURLVali.slipURI(url));
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_TYPE, type);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate: ï¿½");
        createWelcomeLay();
//        List<TopInfo.Information> list;
//        list = read();
//        if (list != null) {
//            showInfo(read());
//        }
    }

    private void createWelcomeLay() {

        welcomeLay = (LinearLayout) findViewById(R.id.welcom_lay);
        TextView infoTv = (TextView) findViewById(R.id.info_tv);
        TextView timeTv = (TextView) findViewById(R.id.time_tv);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd hh:mm");
        String today = sdf.format(date);
        Log.d(TAG, "onCreate: " + today);
        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        String dateStart = pre.getString("dateStart", "");
        if (dateStart.equals("")) {
            SharedPreferences.Editor edit = pre.edit();
            edit.putString("dateStart", today);
            edit.commit();
        }
        String dateExist = pre.getString("dateStart", "");
//        textWelcom.setText(getResources().getString(R.string.txtWelCom) + " " + dateExist);

        infoTv.setText(R.string.txtWelCom);
        timeTv.setText(dateExist);
        findViewById(R.id.top_info_bt).setVisibility(View.GONE);
        findViewById(R.id.arrow_bottom_iv).setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //check BLE
        if (Build.VERSION.SDK_INT > 17) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                startService(new Intent(TopActivity.this, BeaconService.class));
                Log.d(TAG, "initView: check build version");
            }
        }
        getTopInfo();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "[onStop] MainActivity stop event.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
//        SharedPreferences.Editor edit = pre.edit();
//        edit.putString("beaconID", "");
//        edit.commit();
        Log.d(TAG, "onDestroy: ");
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
                goSettings();
                break;
            case R.id.btnFootprint:
                goFootPrint();
                break;
            case R.id.btnTimeTable:
                goTimetable();
                break;
            case R.id.btnOfficialWebsite:
                showOfficialWebsite();
                break;
        }
    }

    private void goSettings() {
        Intent i = new Intent(this, SettingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goFootPrint() {
        Intent i = new Intent(this, FootPrintActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goTimetable() {
        Intent i = new Intent(this, ScheduleActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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
//        String valid_until_start = "10/09/2016";
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
        if (!(new Date().before(strDateEnd))) {
            Intent i = new Intent(this, EndServiceActivity.class);
            startActivity(i);
            finish();
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
void showAlertColorNFC(){
    PromptDialog dialog = new PromptDialog(this);
    dialog.setDialogType(PromptDialog.DIALOG_TYPE_DEFAULT);
//    dialog.setAnimationEnable(true);
    dialog.setContentText(getResources().getString(R.string.txt_message_recommen_nfc));
    dialog.setPositiveListener(R.string.btn_skip, new PromptDialog.OnPositiveListener() {
        @Override
        public void onClick(PromptDialog dialog) {
            dialog.dismiss();
            Utils.launchActivity(TopActivity.this, QRCodeScanActivity.class, ZXING_CAMERA_PERMISSION);
        }
    }).show();
}
    void checkGotoQRCode() {
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
//        showAlertColorNFC();
        if (adapter != null && !adapter.isEnabled()) {
//            showAlertAdviceNFC();
            showAlertColorNFC();
        } else {
            Utils.launchActivity(TopActivity.this, QRCodeScanActivity.class, ZXING_CAMERA_PERMISSION);
        }
    }

    private void getTopInfo() {
        api = new Api(this, Constants.API_GET_TOP_INFO, new ApiCallback() {
            @Override
            public void callback(int responseCode, int requestCode, Object obj) {
                api = null;
                if (requestCode == Constants.API_GET_TOP_INFO) {
                    if (obj instanceof TopInfo) {
                        TopInfo tInfo = (TopInfo) obj;
                        if (tInfo.information != null && tInfo.information.size() > 0) {
                            showInfo(tInfo.information);
                        } else {
                            infoLay.setVisibility(View.GONE);
                            welcomeLay.setVisibility(View.VISIBLE);

                        }
                    }
                    else {
                        List<TopInfo.Information> list;
                        list = read();
                        if (list != null) {
                            showInfo(read());
                        }
                    }
                }
            }
        }, Constants.API_GET_TOP_INFO, new Api.ApiParams());
        api.execute();
    }


    private String convertGsonToJson(List<TopInfo.Information> infoList) {
        Gson gson = new GsonBuilder().create();
        JsonArray result = gson.toJsonTree(infoList).getAsJsonArray();
        return result.toString();
    }

    private void showInfo(List<TopInfo.Information> infoList) {
        infoListLay.removeAllViews();
        // Inflater取得.
        LayoutInflater inflater = getLayoutInflater();
        boolean isJp = false;
        if (Locale.getDefault().equals(Locale.JAPAN) || Locale.getDefault().equals(Locale.JAPANESE)) {
            isJp = true;
        }
        if (infoList.size() > 0) {
//            textWelcom.setVisibility(View.GONE);
            Log.d(TAG, "showInfo: " + infoList.size());
//            writeListToFile(convertGsonToJson(infoList));
            Utils.writeListToFile(convertGsonToJson(infoList),this, Constants.FILE_NAME_JSON_TOP);
            for (int i = 0; i < 1; i++) {
                // page_layoutからページを作成.(1ページ目)
                FrameLayout ll = (FrameLayout) inflater.inflate(R.layout.info_row, null);
                TextView a = (TextView) ll.findViewById(R.id.info_tv);
                TextView b = (TextView) ll.findViewById(R.id.time_tv);
                a.setMaxLines(2);
                b.setText(infoList.get(i).date);
                a.setText(isJp ? infoList.get(i).title_ja : infoList.get(i).title_en);
                Button infoBt = (Button) ll.findViewById(R.id.top_info_bt);
                infoBt.setTag(infoList.get(i));
                infoBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TopInfo.Information info = (TopInfo.Information) v.getTag();
//                    goInfo(info);
                        goInformationTop();
                    }
                });
                infoListLay.addView(ll);
                welcomeLay.setVisibility(View.GONE);
            }
            infoLay.setVisibility(View.VISIBLE);
        }

    }

    private void goInformationTop() {
        Intent i = new Intent(this, InfomationTopActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

//    private void goInfo(TopInfo.Information informaion) {
//        String locale = Locale.getDefault().toString();
//        boolean isJp = locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE);
//        String url = isJp ? informaion.url_ja : informaion.url_en;
//        if (informaion.show_as == 1) {
//            showWebview(url, Constants.WEBVIEW_TYPE_NORMAL);
//        } else if (informaion.show_as == 2) {
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        } else if (informaion.show_as == 0) {
//            // 何もしない
//        } else if (informaion.show_as == 3) {
//            // ポップアップ
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.title_information)
//                    .setMessage(isJp ? informaion.title_ja : informaion.title_en)
//                    .setNegativeButton(R.string.btn_close, null)
//                    .show();
//        }
//    }


    private void receiveBeacon() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            HashMap info = (HashMap) intent.getSerializableExtra("NotificationMessageBeaconBG");
            if (info != null) {
//                String message = info.get("displayText").toString() + "\n" + info.get("url").toString();
                String message = info.get("displayText").toString();
                String title = info.get("displayTitle").toString();
                String eventID;
                if (info.get("eventId") != null) {
                    eventID = info.get("eventId").toString();
                } else {
                    eventID = "0";
                }
                url = info.get("url").toString();
                showAlertBeacon(title, message, eventID);
            }

        }
    }

    void showAlertBeacon(String title, String message, final String eventId) {
        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(this);
        }
        builder.setPositiveButton(getResources().getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        if (!eventId.equals("0")) {
            builder.setNegativeButton(getResources().getString(R.string.dialog_btn_display), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (eventId.equals("1")) {
//                        showBrowser();
                        gotoWebView();
                    } else if (eventId.equals("2")) {
                        gotoWebView();
                    }
                }
            });
        }
        //builder.setTitle(title);
        builder.setMessage(title);
        builder.show();
    }


//    private void showBrowser() {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        startActivity(i);
//    }

    private void gotoWebView() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, url);
        startActivity(intent);
    }

    private void writeListToFile(String json) {
        File myfile = getFileStreamPath(Constants.FILE_NAME_JSON_TOP);
        try {
            if (myfile.exists() || myfile.createNewFile()) {
                FileOutputStream fos = openFileOutput(Constants.FILE_NAME_JSON_TOP, MODE_PRIVATE);
                Log.i(TAG, "abc writeListToFile: ListInfo COme here");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<TopInfo.Information> read() {
        FileInputStream fis;
        try {
            fis = openFileInput(Constants.FILE_NAME_JSON_TOP);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String data = (String) ois.readObject();
            Type collectionType = new TypeToken<List<TopInfo.Information>>() {
            }.getType();
            List<TopInfo.Information> listInfo = new Gson().fromJson(data, collectionType);
            if (listInfo != null) {
                Log.i(TAG, "abc read: " + listInfo.size());
                for (TopInfo.Information item : listInfo) {
                    Log.i(TAG, "Information read: " + item.getTitle_en());
                    //    list.add(item);
                }
//                textWelcom.setVisibility(View.GONE);
                ois.close();
            }
            return listInfo;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
