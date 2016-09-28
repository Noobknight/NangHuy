package jp.co.crypton.spinach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import jp.co.crypton.spinach.abstracts.BaseActivity;
import jp.co.crypton.spinach.constants.CommonPrefs;

import io.tangerine.beaconreceiver.android.sdk.application.BeaconReceiver;

/**
 * Created by huy on 9/6/2016.
 */
public class FromExternalActivity extends BaseActivity {
    private static final String TAG = "FromExternalActivity";
    private BeaconReceiver mBeaconReceiver;

    @Override
    protected int layoutById() {
        // return R.layout.activity_launch;
        return 0;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void setEvents() {
        prepareNfc(null);

        boolean agree = CommonPrefs.isAgree();
        if (agree) {
            Intent i = new Intent(this, TopActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("NFC_URL", "http://yahoo.co.jp");
            startActivity(i);
        } else {
            startActivity(new Intent(
                    this, LicensedActivity.class).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        finish();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            
        }
    }

    @Override
    protected String TAG() {
        return null;
    }
}
