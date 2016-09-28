package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.List;
import java.util.Locale;

import jp.co.crypton.spinach.Helpers.Api;
import jp.co.crypton.spinach.Helpers.ApiCallback;
import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.TopInfo;
import jp.co.crypton.spinach.services.BeaconService;


/**
 * Created by huy on 9/6/2016.
 */
public class EndServiceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "EndServiceActivity";
    private Button btnFootprint, btnDownload, btnFB, btnTwitter, btnOfficialSite, btnArtist;
    private ImageButton btnSetting;
    private LinearLayout infoLay, infoListLay;
    private CheckURLVali checkURLVali = new CheckURLVali();
    private Api api;
    private TextView textViewThank;
    private LinearLayout welcomeLay;

    @Override
    protected int layoutById() {
        return R.layout.end_service_design;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnFootprint = (Button) findViewById(R.id.end_activity_btnFootprint);
        btnDownload = (Button) findViewById(R.id.btnDocDownload);
        infoLay = (LinearLayout) findViewById(R.id.info_lay);
        infoListLay = (LinearLayout) findViewById(R.id.info_list_lay);
        textViewThank = (TextView) findViewById(R.id.txt_thank);
        btnFB = (Button) findViewById(R.id.end_btn_facebook);
        btnTwitter = (Button) findViewById(R.id.end_btn_twitter);
        btnOfficialSite = (Button) findViewById(R.id.end_activity_btn_official_site);
        btnArtist = (Button) findViewById(R.id.end_activity_btn_artist_content);
        btnSetting = (ImageButton) findViewById(R.id.end_activity_btn_setting);

    }

    @Override
    protected void setEvents() {
        btnFootprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFootPrint();
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDownload();
            }
        });
        btnOfficialSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOfficialWebsite();
            }
        });
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
        btnArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDownloadMusic();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetting();
            }
        });

        prepareNfc(null);
    }

    private void gotoDownloadMusic() {
        Intent i = new Intent(this, DonloadMusicActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void gotoDownload() {
        Intent i = new Intent(this, DownLoadActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void gotoSetting() {
        Intent i = new Intent(this, SettingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    protected void initData() {
        Log.d(TAG, "initData: ");
        stopService(new Intent(EndServiceActivity.this, BeaconService.class));
        createWelcomeLay();
//        List<TopInfo.Information> list;
//        list = read();
//        if (list != null) {
//            showInfo(read());
//        }
        setTextThank();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        getTopInfo();
    }

    @Override
    protected String TAG() {
        return null;
    }

    @Override
    public void onClick(View v) {

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
                            List<TopInfo.Information> list;
                            list = read();
                            if (list != null) {
                                showInfo(read());
                            }
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
            writeListToFile(convertGsonToJson(infoList));
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


    private void showWebview(String url, int type) {
        Intent newActivity = new Intent(this, WebViewActivity.class);
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, checkURLVali.slipURI(url));
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_TYPE, type);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newActivity);
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

    private void goFootPrint() {
        Intent i = new Intent(this, FootPrintActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.INTENT_KEY_FOOTPRINT, "Huy");
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
        //textWelcom.setText(getResources().getString(R.string.txtWelCom)+" "+dateExist);

        infoTv.setText(R.string.txtWelCom);
        timeTv.setText(dateExist);
        findViewById(R.id.top_info_bt).setVisibility(View.GONE);
        findViewById(R.id.arrow_bottom_iv).setVisibility(View.GONE);
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

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.common, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_home:
//                finish();
//                break;
//        }
//        return true;
//    }
    void setTextThank() {
//        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
//        String dateStart = pre.getString("dateStart", "");
        String valid_until_start = "10/10/2016";
        String valid_until_end = "17/10/2016";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDateStart = null;
        Date strDateEnd = null;
        try {
            strDateStart = sdf.parse(valid_until_start);
            strDateEnd = sdf.parse(valid_until_end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((new Date().before(strDateStart))) {
            textViewThank.setText(getResources().getString(R.string.txtWelCom));
        } else if (new Date().after(strDateEnd)) {
            textViewThank.setText(getResources().getString(R.string.txt_thank));
        }
    }
}
