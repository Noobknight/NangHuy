package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.adapters.DownloadMusicAcdapter;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.Footprint;
import jp.co.crypton.spinach.model.ListData;
import jp.co.crypton.spinach.utils.Utils;

/**
 * Created by huy on 9/15/2016.
 */
public class DonloadMusicActivity extends BaseActivity {
    private final String TAG = DownLoadActivity.class.getSimpleName();
    private GridView lvMyPage;
    private DownloadMusicAcdapter mDownloadAdapter;
    private CheckURLVali checkURLVali = new CheckURLVali();
    AsyncHttpClient client = new AsyncHttpClient();
    List<ListData> result = new ArrayList<>();

    @Override
    protected int layoutById() {
        return R.layout.activiti_download_music;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        lvMyPage = (GridView) findViewById(R.id.activity_download_music_gridview);

    }

    @Override
    protected void setEvents() {
        lvMyPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = result.get(position).getUrl();
                if (!url.isEmpty() && url != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        prepareNfc(null);
    }

    @Override
    protected void initData() {
//        if (read() != null) {
//            result = read();
//        }
        mDownloadAdapter = new DownloadMusicAcdapter(this, result);
        lvMyPage.setAdapter(mDownloadAdapter);
        String tapcm = CommonPrefs.getCookieTapcm();
        getDataIsConnectInternet(Constants.API_DOMAIN_JSON_DOWNLOAD, tapcm);
    }

    @Override
    protected String TAG() {
        return null;
    }

    void getDataIsConnectInternet(String URL, String tapcm) {
        String url = URL + "?tk=" + tapcm + "&tm=" + Constants.NOMAPS_PROJECT_ID + "&lb=music";
        Log.d(TAG, "getDataIsConnectInternet: " + url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d(TAG, "AsyncHttpClient onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String data = new String(response);
                Log.d(TAG, data);
                Footprint model = new Gson().fromJson(data, Footprint.class);
//                writeListToFile(data);
                Utils.writeListToFile(data,DonloadMusicActivity.this,Constants.FILE_NAME_JSON_MUSIC);
                result.clear();
                for (ListData item : model.getList()) {
                    result.add(item);
                }
                mDownloadAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                if (read() != null) {
                    for (ListData item : read()) {
                        result.add(item);
                    }
                    mDownloadAdapter.notifyDataSetChanged();

                }
                Log.d(TAG, "AsyncHttpClient onFailure");
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
            fis = openFileInput(Constants.FILE_NAME_JSON_MUSIC);
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
//        File myfile = getFileStreamPath(Constants.FILE_NAME_JSON_MUSIC);
//        try {
//            if (myfile.exists() || myfile.createNewFile()) {
//                FileOutputStream fos = openFileOutput(Constants.FILE_NAME_JSON_MUSIC, MODE_PRIVATE);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                oos.writeObject(json);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
