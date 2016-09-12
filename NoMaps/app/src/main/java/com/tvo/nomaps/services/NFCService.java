package com.tvo.nomaps.services;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
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
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tvo.nomaps.activities.NfcEventListener;
import com.tvo.nomaps.activities.WebViewActivity;

import java.io.UnsupportedEncodingException;

/**
 * Created by huy on 9/5/2016.
 */
public class NFCService  {

//    /**
//     * NFC関連アイテム
//     */
//    public NfcAdapter mNfcAdapter;
//    /**
//     * NFC関連アイテム
//     */
//    public PendingIntent mNfcPendingIntent;
//
//    public NfcEventListener nfcListener;
//
//    public Activity activity;
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new NFCBinder();
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        prepareNfc(nfcListener);
//
//        setNfcIntentFilter(activity, mNfcAdapter, mNfcPendingIntent);
//
//        return START_STICKY;
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mNfcAdapter != null) {
//            mNfcAdapter.disableForegroundDispatch(activity);
//        }
//    }
//
//
//    public void prepareNfc(NfcEventListener nfcListener) {
//        this.nfcListener = nfcListener;
//        mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
//        mNfcPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
//                new Intent(getApplicationContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//    }
//
////    @Override
////    protected void onResume() {
//////        Log.e(TAG, "onResume");
////        super.onResume();
////        if (mNfcAdapter != null) {
////            setNfcIntentFilter(this, mNfcAdapter, mNfcPendingIntent);
////        }
////
////    }
////
////    @Override
////    protected void onPause() {
////        super.onPause();
////        if (mNfcAdapter != null) {
////            mNfcAdapter.disableForegroundDispatch(this);
////        }
////    }
//
//
//
//
//    /**
//     * フォアグラウンドディスパッチシステムで、アプリ起動時には優先的にNFCのインテントを取得するように設定する
//     */
//    private void setNfcIntentFilter(Activity activity, NfcAdapter nfcAdapter, PendingIntent seder) {
//        // NDEF type指定
//        IntentFilter typeNdef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        try {
//            typeNdef.addDataType("*/*");
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }
//        // NDEF スキーマ(http)指定
//        IntentFilter httpNdef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        httpNdef.addDataScheme("http");
//        IntentFilter[] filters = new IntentFilter[]{
//                typeNdef, httpNdef
//        };
//        // TECH指定
//        String[][] techLists = new String[][]{
//                new String[]{IsoDep.class.getName()},
//                new String[]{NfcA.class.getName()},
//                new String[]{NfcB.class.getName()},
//                new String[]{NfcF.class.getName()},
//                new String[]{NfcV.class.getName()},
//                new String[]{Ndef.class.getName()},
//                new String[]{NdefFormatable.class.getName()},
//                new String[]{MifareClassic.class.getName()},
//                new String[]{MifareUltralight.class.getName()}
//        };
//
//        nfcAdapter.enableForegroundDispatch(activity, seder, filters, techLists);
//    }
//
//
//    public class NFCBinder extends Binder {
//        public NFCService getService(Activity currentActivity) {
//            activity = currentActivity;
//            return NFCService.this;
//        }
//    }



}
