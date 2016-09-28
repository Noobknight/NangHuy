package jp.co.crypton.spinach.services;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.activities.EndServiceActivity;
import jp.co.crypton.spinach.activities.TopActivity;
import jp.co.crypton.spinach.constants.CommonPrefs;
import jp.co.crypton.spinach.utils.Utils;


/**
 * Created by huy on 9/12/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AoAlarmReceiver";
    private Context mContext;
    private Handler mHandler = new Handler();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        this.mContext = context;
        String action = intent.getAction();
        if (action != null) {
            if (action.equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)
                    || action.equalsIgnoreCase(Intent.ACTION_TIME_CHANGED)
                    || action.equalsIgnoreCase(Intent.ACTION_DATE_CHANGED)) {
                Utils.setAlarmService(context);
            }
        }

        generateNotification(context);
    }

    public void generateNotification(Context context) {
        if (isAppIsInBackground(context)) {
        } else {
            Intent dialogIntent = new Intent(context, EndServiceActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialogIntent);
        }
        Intent intent1 = new Intent(context.getApplicationContext(), TopActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("Alarm", "hello");
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context).setSmallIcon(R.mipmap.icon)
                .setContentTitle("NoMapsGO!")
                .setContentText(context.getResources().getString(R.string.txt_message_notification));
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(R.string.txt_message_notification)));
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
        CommonPrefs.setKeyNotify(true);
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }


}
