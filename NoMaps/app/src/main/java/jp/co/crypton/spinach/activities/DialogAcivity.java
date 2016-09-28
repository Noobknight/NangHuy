package jp.co.crypton.spinach.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.services.BeaconService;
import jp.co.crypton.spinach.utils.Utils;

/**
 * Created by huy on 9/16/2016.
 */
public class DialogAcivity extends Activity implements View.OnClickListener {
    Button ok_btn, cancel_btn;
    TextView textView;
    String msg, url, eventID;
    private static final String TAG = "DialogAcivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ok_btn = (Button) findViewById(R.id.ok_btn_id);
        cancel_btn = (Button) findViewById(R.id.cancel_btn_id);
        textView = (TextView) findViewById(R.id.textView1);
        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        onNewIntent(getIntent());

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > 17) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                if (!Utils.isServiceRunning(this, TopActivity.class)) {
                    stopService(new Intent(DialogAcivity.this, BeaconService.class));
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn_id:
                finish();
                if (eventID.equals("1"))
                {
                    gotoWebView();
//                    gotoWebBroser();
                }
                else if (eventID.equals("2"))
                {
                    gotoWebView();
                }
                break;
            case R.id.cancel_btn_id:
                this.finish();
                break;
        }
    }

    private void gotoWebBroser() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT > 17) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                if (!Utils.isServiceRunning(this, TopActivity.class)) {
                    startService(new Intent(DialogAcivity.this, BeaconService.class));
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void gotoWebView() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constants.INTENT_KEY_WEBVIEW_URL, url);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            HashMap info = (HashMap) intent.getSerializableExtra("NotificationMessageBeacon");
            Log.d(TAG, "onNewIntent: " + msg);
            textView.setText(info.get("displayTitle").toString());
//            textView.setText(info.get("displayText").toString() + "\n" + info.get("url").toString());
//            this.setTitle(info.get("displayTitle").toString());
            url = info.get("url").toString();
            if (info.get("eventId")!=null){
                eventID = info.get("eventId").toString();
                if (eventID.equals("0"))
                {
                    ok_btn.setVisibility(View.GONE);
                }
            }
            else {
                ok_btn.setVisibility(View.GONE);
            }

        }
    }
}
