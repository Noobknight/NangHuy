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
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

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
import jp.co.crypton.spinach.adapters.DownloadAdapter;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.DocDownload;
import jp.co.crypton.spinach.model.Footprint;
import jp.co.crypton.spinach.model.ListData;
import jp.co.crypton.spinach.model.ModelDownload;
import jp.co.crypton.spinach.utils.Utils;

/**
 * Created by huy on 9/15/2016.
 */
public class DownLoadActivity extends BaseActivity {
    private final String TAG = DownLoadActivity.class.getSimpleName();
    private GridView lvMyPage;
    private DownloadAdapter mDownloadAdapter;
    private CheckURLVali checkURLVali = new CheckURLVali();
    AsyncHttpClient client = new AsyncHttpClient();
    List<ListData> result = new ArrayList<>();
    private Button btnSend;
    @Override
    protected int layoutById() {
        return R.layout.activity_download;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        lvMyPage = (GridView) findViewById(R.id.activity_download_gridview);
        btnSend = (Button) findViewById(R.id.activity_download_btn_send_email);
    }

    @Override
    protected void setEvents() {
        lvMyPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = result.get(position).getUrl();
                if (!url.isEmpty() && url!=null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
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

        mDownloadAdapter = new DownloadAdapter(this, result);
        lvMyPage.setAdapter(mDownloadAdapter);
        String tapcm = CommonPrefs.getCookieTapcm();
        getDataIsConnectInternet(Constants.API_DOMAIN_JSON_DOWNLOAD, tapcm);
    }

    @Override
    protected String TAG() {
        return null;
    }

    void getDataIsConnectInternet(String URL, String tapcm) {
        String url = URL + "?tk=" + tapcm + "&tm="+Constants.NOMAPS_PROJECT_ID+"&lb=tradeshow";
//        String url = "http://smartplate.kokope.li/access_history.php?tk=cd0c4f3b3d8062f18008e8e291bec0a8fe659408&tm=2841";

        Log.d(TAG, "getDataIsConnectInternet: "+url);
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
//                writeListToFile(data);
                Utils.writeListToFile(data,DownLoadActivity.this,Constants.FILE_NAME_JSON_DOWNLOAD);
                result.clear();
                for (ListData item : model.getList()) {
                    result.add(item);
                }
                if (result.size()>0)
                {
                    btnSend.setVisibility(View.VISIBLE);
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
                    if (result.size()>0)
                    {
                        btnSend.setVisibility(View.VISIBLE);
                    }
                }
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
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
            fis = openFileInput(Constants.FILE_NAME_JSON_DOWNLOAD);
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
//        File myfile = getFileStreamPath(Constants.FILE_NAME_JSON_DOWNLOAD);
//        try {
//            if (myfile.exists() || myfile.createNewFile()) {
//                FileOutputStream fos = openFileOutput(Constants.FILE_NAME_JSON_DOWNLOAD, MODE_PRIVATE);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                oos.writeObject(json);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    void sendEmail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        String body = "";
        for(ListData item : result)
        {
            body += item.getUserName()+"\n"+item.getUrl()+"\n";
        }
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "NoMaps資料ダウンロード");
        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DownLoadActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
