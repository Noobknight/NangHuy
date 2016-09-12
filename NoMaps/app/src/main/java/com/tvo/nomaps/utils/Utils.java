package com.tvo.nomaps.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.webkit.URLUtil;

import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by Tavv
 * on 29/08/2016.
 */
public class Utils {

    public static void launchActivity(Activity activity, Class<?> clss, final int PERMISSION) {

        switch (PERMISSION){
            case 0 :
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION);
                } else {
                    Intent intent = new Intent(activity, clss);
                    activity.startActivity(intent);
                }
                break;
            case 1 :
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA}, PERMISSION);
                } else {
                    Intent intent = new Intent(activity, clss);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }

                break;
        }

    }

    public static void gotoActivity(Activity activity, Class<?> clss){
        Intent intent = new Intent(activity, clss);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return isURL;
    }
}
