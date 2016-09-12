package com.tvo.nomaps.Helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.tvo.nomaps.R;

/**
 * Created by huy on 9/7/2016.
 */
public class CheckDevideNFC extends Activity{
    public Boolean checkDevideSupportNFC(){
        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null) {
            return true;
        }
        return false;
    }
}
