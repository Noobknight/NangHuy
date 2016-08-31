package com.tvo.nomaps.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.tvo.nomaps.R;
import com.tvo.nomaps.abstracts.BaseActivity;
import com.tvo.nomaps.utils.Utils;

/**
 * Created by Tavv
 * on 29/08/2016.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "WebViewActivity";
    private WebView mWebView;
    private Button btnClose, btnUpdate;
    private ProgressDialog progressBar;


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

    @Override
    protected void setEvents() {
        btnClose.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        if (getURL() != null) {
            progressBar = ProgressDialog.show(WebViewActivity.this, "", "Loading...");
            mWebView.setWebViewClient(new WebViewClient() {

                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    final Uri uri = request.getUrl();
                    return Utils.checkURL(uri.toString());
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

            });

            mWebView.loadUrl(getURL());
        } else {
            Toast.makeText(WebViewActivity.this, "URL is null", Toast.LENGTH_SHORT).show();
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
        return intent.getStringExtra(QRCodeScanActivity.KEY_EXTRA);
    }

}
