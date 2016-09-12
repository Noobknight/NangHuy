package com.tvo.nomaps.abstracts;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
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
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.tvo.nomaps.activities.NfcEventListener;
import com.tvo.nomaps.activities.WebViewActivity;

import java.io.UnsupportedEncodingException;
import java.net.URL;

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
        if (intent != null)
        {
            Uri url = intent.getData();
            if (url != null){
                Intent newActivity = new Intent(this, WebViewActivity.class);
                newActivity.putExtra(KEY_EXTRA_NFC, "http://"+readFromIntent(intent));
                startActivity(newActivity);
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



    @Nullable
    private String readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            return buildTagViews(msgs);
        }
        return null;
    }

    @org.jetbrains.annotations.Contract("null -> null")
    private String buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return null;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
//        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"

        try {
            // Get the Text
            text = new String(payload, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        String result =  text.substring(1,text.length()-1);
        Log.d(TAG, "buildTagViews:"+result);
        return result;
    }

}