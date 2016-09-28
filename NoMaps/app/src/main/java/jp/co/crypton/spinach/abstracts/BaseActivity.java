package jp.co.crypton.spinach.abstracts;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jp.co.crypton.spinach.activities.LaunchScreen;
import jp.co.crypton.spinach.activities.NfcEventListener;
import jp.co.crypton.spinach.constants.Constants;

/**
 * Created by Tavv
 * on 27/08/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final String TAG = BaseActivity.class.toString();
    public static final String KEY_EXTRA_NFC = "url_nfc_code";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutById());
        TAG();
        initView(savedInstanceState);
        initData();
        setEvents();
    }
    /*Function using for set id layout */
    protected abstract int layoutById();
    /*Function using for init views*/
    protected abstract void initView(@Nullable Bundle savedInstanceState);
    /*init event for views*/
    protected abstract void setEvents();
    /*Function using init data for views */
    protected abstract void initData();
    /*Function using log class Tag */
    protected abstract String TAG();

    /** NFC関連アイテム */
    public NfcAdapter mNfcAdapter;
    /** NFC関連アイテム */
    public PendingIntent mNfcPendingIntent;

    public NfcEventListener nfcListener;

    public void prepareNfc(NfcEventListener nfcListener) {
        this.nfcListener = nfcListener;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        //o day la khi co nfc la no start qua thang TOPactivity dug ko, la sao?
        mNfcPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
//        Log.e(TAG, "onResume");
        super.onResume();
        if (mNfcAdapter != null) {
            setNfcIntentFilter(this, mNfcAdapter, mNfcPendingIntent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        String foundUrl = intent.getDataString();
        if (nfcListener != null) {
            if (foundUrl != null && foundUrl.startsWith(Constants.NOMAPS_URL_PREFIX)) {
                nfcListener.foundUri(foundUrl);
            }
        }
    }

    /**
     * フォアグラウンドディスパッチシステムで、アプリ起動時には優先的にNFCのインテントを取得するように設定する
     */
    private void setNfcIntentFilter(Activity activity, NfcAdapter nfcAdapter, PendingIntent seder) {
        // NDEF type指定
        IntentFilter typeNdef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            typeNdef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        // NDEF スキーマ(http)指定
        IntentFilter httpNdef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        httpNdef.addDataScheme("http");
        IntentFilter[] filters = new IntentFilter[] {
                typeNdef, httpNdef
        };
        // TECH指定
        String[][] techLists = new String[][] {
                new String[] { IsoDep.class.getName() },
                new String[] { NfcA.class.getName() },
                new String[] { NfcB.class.getName() },
                new String[] { NfcF.class.getName() },
                new String[] { NfcV.class.getName() },
                new String[] { Ndef.class.getName() },
                new String[] { NdefFormatable.class.getName() },
                new String[] { MifareClassic.class.getName() },
                new String[] { MifareUltralight.class.getName() }
        };
        nfcAdapter.enableForegroundDispatch(activity, seder, filters, techLists);
    }

    public boolean fromHistoryCheck() {
        boolean ret = false;
        int flag = getIntent().getFlags();
        if ((flag & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            Intent i = new Intent(this, LaunchScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
//            overridePendingTransition(0, 0);
        }
        return ret;
    }
}