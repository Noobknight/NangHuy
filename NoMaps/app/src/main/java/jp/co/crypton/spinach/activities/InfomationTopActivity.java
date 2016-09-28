package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.refactor.lib.colordialog.PromptDialog;
import cz.msebera.android.httpclient.Header;
import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.adapters.InfoTopAdapter;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.model.InfoModel;
import jp.co.crypton.spinach.model.Information;

/**
 * Created by huy on 9/20/2016.
 */
public class InfomationTopActivity extends BaseActivity implements InfoTopAdapter.OnShowMoreClickListener {

    private final String TAG = InfomationTopActivity.class.getSimpleName();
    private ListView lvMyPage;
    private InfoTopAdapter mAdapter;
    private CheckURLVali checkURLVali = new CheckURLVali();
    AsyncHttpClient client = new AsyncHttpClient();
    List<Information> result = new ArrayList<>();
    String tapcm, tm;
//    View lastTouchedView;

    @Override
    protected int layoutById() {
        return R.layout.activity_info_top;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        lvMyPage = (ListView) findViewById(R.id.lv_info_top);
    }

    @Override
    protected void setEvents() {
        lvMyPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isJp = false;
                if (Locale.getDefault().equals(Locale.JAPAN) || Locale.getDefault().equals(Locale.JAPANESE)) {
                    isJp = true;
                }
                String url = isJp ? result.get(position).getUrlJa() : result.get(position).getUrlEn();
                int showAs = result.get(position).getShowAs();
                switch (showAs) {
                    case 0:
                        // 何もしない
                        break;
                    case 1:
                        showWebview(url, Constants.WEBVIEW_TYPE_NORMAL);
                        break;
                    case 2:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case 3:
                        // ポップアップ
//                        new AlertDialog.Builder(InfomationTopActivity.this)
//                                .setTitle(R.string.title_information)
//                                .setMessage(isJp ? result.get(position).getTitleJa() : result.get(position).getTitleEn())
//                                .setNegativeButton(R.string.btn_close, null)
//                                .show();
                        showAlertColor(getResources().getString(R.string.title_information),isJp ? result.get(position).getTitleJa() : result.get(position).getTitleEn());
                        break;

                }
            }
        });

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            if (extra.containsKey(Constants.INTENT_KEY_FOOTPRINT)) {
                prepareNfc(null);
            }
        } else {
            prepareNfc(new NfcEventListener() {
                @Override
                public void foundUri(String uri) {
                    showWebview(uri, Constants.WEBVIEW_TYPE_SP);
                }
            });
        }
    }

    void showAlertColor(String title, String message) {
        PromptDialog dialog = new PromptDialog(this);
        dialog.setDialogType(PromptDialog.DIALOG_TYPE_DEFAULT);
        dialog.setAnimationEnable(true);
        dialog.setTitleText(title).setContentText(message);
        dialog.setPositiveListener(R.string.btn_close, new PromptDialog.OnPositiveListener() {
            @Override
            public void onClick(PromptDialog dialog) {
                dialog.dismiss();
            }
        }).show();
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
        if (read() != null) {
            result = read();
        }
        mAdapter = new InfoTopAdapter(this, result, this);
        lvMyPage.setAdapter(mAdapter);
//        getDataIsConnectInternet();
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    void getDataIsConnectInternet() {

        client.get(Constants.URL_TOP_INFO, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "AsyncHttpClient onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String data = new String(response);
                Log.d(TAG, data);

                InfoModel model = new Gson().fromJson(data, InfoModel.class);

//                    writeListToFile(data);
                result.clear();
                for (Information item : model.getInformation()) {
                    result.add(item);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
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


    List<Information> read() {
        List<Information> list = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = openFileInput(Constants.FILE_NAME_JSON_TOP);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String data = (String) ois.readObject();

            Type collectionType = new TypeToken<List<Information>>() {
            }.getType();
            list = new Gson().fromJson(data, collectionType);
//            for (Information item : list) {
//                Log.i(TAG, "read: "+item.getTitleEn());
//                list.add(item);
//            }
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

    boolean isTextViewClicked = false;

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(InfomationTopActivity.this, "Row Click At Positon " + position, Toast.LENGTH_SHORT).show();

        //This will shrink textview to 2 lines if it is expanded.
        if (view instanceof TextView) {
            if (isTextViewClicked) {
                ((TextView) view).setMaxLines(2);
                isTextViewClicked = false;
            } else {
                //This will expand the textview if it is of 2 lines
                ((TextView) view).setMaxLines(Integer.MAX_VALUE);
                isTextViewClicked = true;
            }
        }
    }
}

//    private void writeListToFile(String json) {
//        File myfile = getFileStreamPath(Constants.FILE_NAME_JSON_TOP);
//        try {
//            if (myfile.exists() || myfile.createNewFile()) {
//                FileOutputStream fos = openFileOutput(Constants.FILE_NAME_JSON_TOP, MODE_PRIVATE);
//                ObjectOutputStream oos = new ObjectOutputStream(fos);
//                oos.writeObject(json);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

