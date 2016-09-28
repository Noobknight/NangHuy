package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;

import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.model.Footprint;
import jp.co.crypton.spinach.model.ListData;


/**
 * Created by huy on 8/31/2016.
 */
public class LicensedActivity extends BaseActivity {
    private final String TAG = LicensedActivity.class.getSimpleName();

    Button btnAccept, btnNoAccept;
    ScrollView scrollLicense;
    TextView textView;

    @Override
    protected int layoutById() {
        return R.layout.screen_licensed;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnAccept = (Button) findViewById(R.id.screen_lisensed_btnAccept);
        btnNoAccept = (Button) findViewById(R.id.screen_lisensed_btnNoAccept);
        scrollLicense = (ScrollView) findViewById(R.id.scrollLicense);
        textView = (TextView) findViewById(R.id.screen_lisensed_txtContent);
//        btnAccept.setEnabled(false);

    }

    @Override
    protected void setEvents() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterActivity();
            }
        });
        btnNoAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        scrollLicense.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int totalHeight = scrollLicense.getChildAt(0).getHeight();
//                int diff = (totalHeight - (scrollLicense.getBottom() + scrollLicense.getScrollY()));
////                Log.d(TAG, totalHeight + " " + diff);
//                if (diff <= 0) {
//                    btnAccept.setTextColor(Color.parseColor("#89000000"));
//                    btnAccept.setEnabled(true);
//                }
//            }
//
//        });

//        scrollLicense.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
//                int totalHeight = scrollLicense.getChildAt(0).getHeight();
////                Log.d(TAG, "bbbb" + totalHeight + "Textview " + frameLayout.getHeight());
//                scrollLicense.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                if (totalHeight <= frameLayout.getHeight()) {
//                    btnAccept.setTextColor(Color.parseColor("#89000000"));
//                    btnAccept.setEnabled(true);
//                } else {
//                    btnAccept.setTextColor(Color.parseColor("#EEEEEE"));
//
//                    btnAccept.setEnabled(false);
//                }
//
//            }
//        });
    }
//
//    private boolean canScroll(ScrollView scrollView) {
//        View child = (View) scrollView.getChildAt(0);
//        if (child != null) {
//            int childHeight = (child).getHeight();
//            return scrollView.getHeight() < childHeight + scrollView.getPaddingTop() + scrollView.getPaddingBottom();
//        }
//        return false;
//    }

    private void gotoRegisterActivity() {
        CommonPrefs.setAgree(true);

        String nfcUrl = null;
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nfcUrl = extra.getString(Constants.INTENT_KEY_NFC_URL);
        }

        Intent i = new Intent(this, RegisterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.INTENT_KEY_NFC_URL, nfcUrl);
        startActivity(i);
        finish();
    }

    @Override
    protected void initData() {
        getDataIsConnectInternet();
    }

    @Override
    protected String TAG() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fromHistoryCheck()) {
            return;
        }
    }
    void getDataIsConnectInternet() {
//        TAPCM = "0d2089cae805ee7742d3cf2250fd1aa8ea9eacb7";
//        tm = "1077";
//        String url = URL + "?tk=" + TAPCM + "&tm=" + tm;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.URL_LICENSE, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(TAG, "AsyncHttpClient onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String data = new String(response);
                textView.setText(data);
//                Log.d(TAG, data);
                writeListToFile(data);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(TAG, "AsyncHttpClient onFailure");
                if (!read().equals(""))
                {
                    textView.setText(read());
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d(TAG, "onRetry: ");
            }
        });
    }

    private void writeListToFile(String json) {
        File myfile = getFileStreamPath(Constants.FILE_NAME_JSON_LICENCE);
        try {
            if (myfile.exists() || myfile.createNewFile()) {
                FileOutputStream fos = openFileOutput(Constants.FILE_NAME_JSON_LICENCE, MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String read() {
        String list = "";
        FileInputStream fis;
        try {
            fis = openFileInput(Constants.FILE_NAME_JSON_LICENCE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            String data = (String) ois.readObject();
            ois.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
