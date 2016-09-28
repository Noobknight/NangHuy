package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.adapters.MyPageAdapter;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.Footprint;
import jp.co.crypton.spinach.model.ListData;
import jp.co.crypton.spinach.model.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.utils.Utils;

/**
 * Created by huy on 8/31/2016.
 */
public class FootPrintActivity extends BaseActivity {
    private final String TAG = FootPrintActivity.class.getSimpleName();
    private ListView lvMyPage;
    private MyPageAdapter mAdapter;
    private CheckURLVali checkURLVali = new CheckURLVali();
    AsyncHttpClient client = new AsyncHttpClient();
    List<ListData> result = new ArrayList<>();
    String tapcm, tm;

    @Override
    protected int layoutById() {
        return R.layout.activity_my_page;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        lvMyPage = (ListView) findViewById(R.id.lv_myPage);
    }

    @Override
    protected void setEvents() {
        lvMyPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = result.get(position).getUrl();
                if (url.startsWith(Constants.NOMAPS_URL_PREFIX)) {
                    showWebview(url, Constants.WEBVIEW_TYPE_SP);
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            if (extra.containsKey(Constants.INTENT_KEY_FOOTPRINT)) {
                prepareNfc(null);
            }
        } else {
            prepareNfc(new NfcEventListener() {
                @Override
                public void foundUri(String uri) {
                    showWebview(uri, Constants.WEBVIEW_TYPE_NORMAL);
                }
            });
        }
    }

    private void showWebview(String url, int type) {
        Intent newActivity = new Intent(this, WebViewActivity.class);
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, checkURLVali.slipURI(url));
        newActivity.putExtra(Constants.INTENT_KEY_WEBVIEW_TYPE, type);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newActivity);
    }

    @Override
    protected void initData() {
//        if (read() != null) {
//            result = read();
//        }
        mAdapter = new MyPageAdapter(this, result);
        lvMyPage.setAdapter(mAdapter);
        String tapcm = CommonPrefs.getCookieTapcm();
        getDataIsConnectInternet(Constants.API_DOMAIN, tapcm);
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    void getDataIsConnectInternet(String URL, String TAPCM) {
//        TAPCM = "0d2089cae805ee7742d3cf2250fd1aa8ea9eacb7";
//        tm = "1077";
        String url = URL + "?tk=" + TAPCM + "&tm=" + Constants.NOMAPS_PROJECT_ID;
//        String url = URL + "?tk=" + TAPCM + "&tm=" + tm;
        Log.d("TAPCM", url);
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "AsyncHttpClient onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String data = new String(response);
                Log.d(TAG, data);

                Footprint model = new Gson().fromJson(data, Footprint.class);
                Status status = model.getStatus();
                if (status.getCode() == 0) {
//                    writeListToFile(data);
                    Utils.writeListToFile(data,FootPrintActivity.this,Constants.FILE_NAME_JSON );
                    result.clear();
                    for (ListData item : model.getList()) {
                        result.add(item);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Found");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (read() != null) {
                    for (ListData item : read()) {
                        result.add(item);
                    }
                    mAdapter.notifyDataSetChanged();
                }                Log.d(TAG, "AsyncHttpClient onFailure");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "onRetry: ");
            }
        });
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

    List<ListData> read() {
        List<ListData> list = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = openFileInput(Constants.FILE_NAME_JSON);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String data = (String) ois.readObject();
            Footprint model = new Gson().fromJson(data, Footprint.class);
            for (ListData item : model.getList()) {
                list.add(item);
            }
            ois.close();
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

//    private void writeListToFile(String json) {
//        File myfile = getFileStreamPath(Constants.FILE_NAME_JSON);
//        try {
//            if (myfile.exists() || myfile.createNewFile()) {
//                FileOutputStream fos = openFileOutput(Constants.FILE_NAME_JSON, MODE_PRIVATE);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                oos.writeObject(json);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
