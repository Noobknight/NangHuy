package jp.co.crypton.spinach.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import jp.co.crypton.spinach.Helpers.CheckURLVali;
import jp.co.crypton.spinach.Helpers.GetValueCookies;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.constants.TempInfo;


/**
 * Created by Tavv
 * on 29/08/2016.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "WebViewActivity";
    private WebView mWebView;
    private Button btnClose;
//    private ProgressDialog progressBar;
    private GetValueCookies getValueCookies = new GetValueCookies();
    private int webviewType = Constants.WEBVIEW_TYPE_SP;
    private String url;

    @Override
    protected int layoutById() {
        return R.layout.screen_webview;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mWebView = (WebView) findViewById(R.id.webView);
        btnClose = (Button) findViewById(R.id.screen_webview_btnClose);
//        btnUpdate = (Button) findViewById(R.id.screen_webview_btnUpdate);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        getIntentParams();
    }

    @Override
    protected void setEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        btnUpdate.setOnClickListener(this);

        prepareNfc(null);
    }

    @Override
    protected void initData() {
        final String tapcm = CommonPrefs.getCookieTapcm();
        if (tapcm != null && !"".equals(tapcm)) {
            CookieManager cMgr = CookieManager.getInstance();
//            String[] cookies = CookieManager.getInstance().getCookie(Constants.SMARTPLATE_PREFIX).split(";");
//            if (cookies != null || cookies.length == 0) {
//                for (String cookie : cookies) {
//                    Log.d("", cookie);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TAPCM=" + tapcm + "; ");
            String cookieString = stringBuilder.toString();
            cMgr.setCookie(Constants.SMARTPLATE_PREFIX, cookieString);
            if (Build.VERSION.SDK_INT < 21) {
                CookieSyncManager.getInstance().sync();
            } else {
                cMgr.flush();
            }
//                }
//            }
        }
        if (url != null) {
//            Log.d(TAG, "gotoNextPage: "+);
//            Log.d(TAG, "initData: "+Integer.decode(url.substring(url.length()-1)));
            Log.d(TAG, "onPageFinished: " + url);
            CheckURLVali checkURLVali = new CheckURLVali();
            checkURLVali.checkCharspecial(url);
//            progressBar = ProgressDialog.show(WebViewActivity.this, "", "Loading...");
            String userAgentCustom = new WebView(this).getSettings().getUserAgentString() + "NoMaps";
            mWebView.getSettings().setUserAgentString(userAgentCustom);
            String UUID = CommonPrefs.getUUID();
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("uuid", UUID);
            final String[] tampURL = {""};
            mWebView.setWebViewClient(new WebViewClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
//                    if (progressBar.isShowing()) {
//                        progressBar.dismiss();
//                    }
                }

                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                    if (webviewType == Constants.WEBVIEW_TYPE_NORMAL) {
                        view.loadUrl(url);
                        tampURL[0] = url;
                        return true;
                    }

                    if (url.startsWith(Constants.SMARTPLATE_PREFIX)) {
                        view.loadUrl(url);
                        tampURL[0] = url;
                    } else {
//                        url = "http://plate.id/tiles/tiles.php?tag=NM.10012.1330706&atp=Q";
                        if (tampURL[0].startsWith(Constants.SMARTPLATE_PREFIX_NEXTPAGE)) {
                            showBrowser(url);
                        } else {
                            finish();
                            showBrowser(url);
                        }
                    }
                    return true;
                }


                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    final Uri uri = request.getUrl();
                    String url = uri.toString();

                    if (webviewType == Constants.WEBVIEW_TYPE_NORMAL) {
                        view.loadUrl(url);
                        tampURL[0] = url;
                        return true;
                    }
                    if (url.startsWith(Constants.SMARTPLATE_PREFIX)) {
                        view.loadUrl(url);
                        tampURL[0] = url;
                    } else {
//                        url = "http://plate.id/tiles/tiles.php?tag=NM.10012.1330706&atp=Q";
                        if (tampURL[0].startsWith(Constants.SMARTPLATE_PREFIX_NEXTPAGE)) {
                            showBrowser(url);
                        } else {
                            finish();
                            showBrowser(url);
                        }
                    }
                    return true;
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    Map<String, String> headers = new HashMap<String, String>();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        headers = request.getRequestHeaders();
                    }
                    Log.d(TAG, "onReceivedHttpError: " + headers);
                    return super.shouldInterceptRequest(view, request);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.d(TAG, "onPageStarted: " + url);
                }


                @Override
                public void onPageFinished(WebView view, String url) {
                    if (url.startsWith(Constants.SMARTPLATE_PREFIX)) {
                        if (tapcm == null || "".equals(tapcm)) {
                            String valueTAMCM = getValueCookies.getCookie(url);
                            Log.d(TAG, "All the cookies in a string:" + valueTAMCM);
                            CommonPrefs.setCookieTapcm(valueTAMCM);
//                            CommonPrefs.setCookieTapcm("amsa"+valueTAMCM+"okoto");
                        }
                    }
//                    if (progressBar.isShowing()) {
//                        progressBar.dismiss();
//                    }
                }


            });
            String custom = mWebView.getSettings().getUserAgentString();
            Log.d(TAG, "initData: " + custom);
            mWebView.loadUrl(url, extraHeaders);

        } else {
            Toast.makeText(WebViewActivity.this, "URL is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void showBrowser(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.screen_webview_btnClose:
                finish();
                break;
//            case R.id.screen_webview_btnUpdate:
//                mWebView.reload();
//                break;
        }
    }


    private void getIntentParams() {
        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            finish();
            return;
        }
        url = extra.getString(Constants.INTENT_KEY_WEBVIEW_URL);
        webviewType = extra.getInt(Constants.INTENT_KEY_WEBVIEW_TYPE);
//        if (intent.getStringExtra(QRCodeScanActivity.KEY_EXTRA) != null) {
//            return intent.getStringExtra(QRCodeScanActivity.KEY_EXTRA);
//        }
//        if (intent.getStringExtra(NfcEventListener.KEY_EXTRA_NFC) != null) {
//            return intent.getStringExtra(NfcEventListener.KEY_EXTRA_NFC);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TempInfo.isWebviewOpen = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        TempInfo.isWebviewOpen = false;
    }
}
