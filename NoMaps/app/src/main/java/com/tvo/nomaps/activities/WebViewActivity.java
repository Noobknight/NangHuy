package com.tvo.nomaps.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.tvo.nomaps.Helpers.CheckURLVali;
import com.tvo.nomaps.Helpers.GetValueCookies;
import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tavv
 * on 29/08/2016.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "WebViewActivity";
    private WebView mWebView;
    private Button btnClose, btnUpdate;
    private ProgressDialog progressBar;
    private GetValueCookies getValueCookies = new GetValueCookies();
    @Override
    protected int layoutById() {
        return R.layout.screen_webview;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mWebView = (WebView) findViewById(R.id.webView);
        btnClose = (Button) findViewById(R.id.screen_webview_btnClose);
        btnUpdate = (Button) findViewById(R.id.screen_webview_btnUpdate);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
    }

    void gotoHome() {
        Intent i = new Intent(this, TopActivity.class);
        startActivity(i);
    }

    @Override
    protected void setEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.reload();
            }
        });
    }

    @Override
    protected void initData() {
        checkAccept();
        CheckURLVali checkURLVali = new CheckURLVali();
        String url = checkURLVali.addHttp(getURL());
        Log.d(TAG, "initData"+url);

        if (checkURLVali.checkURL(url)) {
            if (url != null) {
                progressBar = ProgressDialog.show(WebViewActivity.this, "", "Loading...");
                String userAgentCustom = new WebView(this).getSettings().getUserAgentString() + "NoMaps";
                mWebView.getSettings().setUserAgentString(userAgentCustom);
                mWebView.setWebViewClient(new WebViewClient() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        final Uri uri = request.getUrl();
                        return Utils.checkURL(uri.toString());
                    }

                    public void onPageFinished(WebView view, String url) {
                        String valueTAMCM = getValueCookies.getCookie(url);
                        Log.d(TAG, "All the cookies in a string:" + valueTAMCM);
                        String cookieString = "TAPCM="+valueTAMCM+"; path=/";
                        CookieManager.getInstance().setCookie(url, cookieString);
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }

                });
//            Map<String, String> extraHeaders = new HashMap<String, String>();
//            extraHeaders.put("uuid",UUID);
//            mWebView.loadUrl(url,extraHeaders);
                mWebView.loadUrl(url);

            } else {
                Toast.makeText(WebViewActivity.this, "URL is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            gotoHome();
        }

    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.screen_webview_btnClose:
                break;
            case R.id.screen_webview_btnUpdate:
                break;
        }
    }
    private String getURL() {
        Intent intent = getIntent();
        if (intent.getStringExtra(QRCodeScanActivity.KEY_EXTRA) != null) {
            return intent.getStringExtra(QRCodeScanActivity.KEY_EXTRA);
        }
        if (intent.getStringExtra(NfcEventListener.KEY_EXTRA_NFC) != null) {
            return intent.getStringExtra(NfcEventListener.KEY_EXTRA_NFC);
        }
        return null;
    }

    private void checkAccept(){
        SharedPreferences pre = getSharedPreferences ("my_data",MODE_PRIVATE);
        String user = pre.getString("check", "");
        if (user.equals(""))
        {
            Log.d(TAG, "checkAccept: "+user);
            Intent i = new Intent(this, LaunchScreen.class);
            startActivity(i);
        }
    }

}
