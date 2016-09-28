package jp.co.crypton.spinach.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Pattern;

import jp.co.crypton.spinach.constants.Constants;
import jp.co.crypton.spinach.services.AlarmReceiver;

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

    public static String getTodayStr() {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }

    public static String getTomorowStr() {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(cal.getTime());
    }

    public static boolean deleteLocalFile(Context con, String fileName) {
        return con.deleteFile(fileName);
    }

    public static void deleteLocalFileAll(Context con) {
        File localFiles = con.getFilesDir();
        String[] files = localFiles.list();
        if (files != null && files.length != 0) {
            for (int i=0; i<files.length; i++) {
                con.deleteFile(files[i]);
            }
        }
    }

    public static Bitmap getLocalFile(Context con, String fileName) {
        InputStream input = null;
        try {
            input = con.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }

    /**
     * ローカルファイル存在確認
     * @param con
     * @param fileName
     * @return
     */
    public static boolean isLocalFileExist(Context con, String fileName) {
        boolean ret = true;
        try {
            con.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            ret = false;
        }
        return ret;
    }

    public static boolean saveLocalFile(Context con, String fileName, Bitmap file) {
        boolean ret = false;
        FileOutputStream out = null;
        try {
            out = con.openFileOutput(fileName, Context.MODE_PRIVATE);
            file.compress(Bitmap.CompressFormat.PNG, 100, out);
            ret = true;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                    out = null;
                    file = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }
        }
        return ret;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isServiceRunning(Context context,Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void setAlarmService(Context context) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);
//        calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DAY_OF_MONTH, 17);

        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }
    public static void writeListToFile(String json, Context context, String name) {
        File myfile = context.getFileStreamPath(name);
        try {
            if (myfile.exists() || myfile.createNewFile()) {
                FileOutputStream fos = context.openFileOutput(name, context.MODE_PRIVATE);
//                Log.i(TAG, "abc writeListToFile: ListInfo COme here");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
