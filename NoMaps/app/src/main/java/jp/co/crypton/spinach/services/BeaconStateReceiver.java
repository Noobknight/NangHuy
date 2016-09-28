package jp.co.crypton.spinach.services;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;

import jp.co.crypton.spinach.MainActivity;
import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.activities.DialogAcivity;
import jp.co.crypton.spinach.activities.QRCodeScanActivity;
import jp.co.crypton.spinach.activities.TopActivity;
import jp.co.crypton.spinach.constants.Actions;
import jp.co.crypton.spinach.utils.Utils;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by huy on 9/16/2016.
 */
public class BeaconStateReceiver extends BroadcastReceiver {
    private static final String TAG = "BeaconStateReceiver";
    private static final int ZXING_CAMERA_PERMISSION = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(Actions.BEACON_ACTION)) {
                HashMap info = (HashMap) intent.getSerializableExtra("info");
                if (isAppIsInBackground(context)) {
                    generateNotification(context, info);
                } else {
                    showAlert(context, info);
                }
                Log.i(TAG, "onReceive: Notification " + info);
            }
        }
    }

    public void generateNotification(Context context, HashMap message) {
        Intent intent1 = new Intent(context.getApplicationContext(), TopActivity.class);
        intent1.putExtra("NotificationMessageBeaconBG", message);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.icon).setContentTitle("NoMapsGO!").setContentText(message.get("displayTitle").toString());
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
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


    void showAlert(Context context, HashMap info) {
        Intent dialogIntent = new Intent(context, DialogAcivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("NotificationMessageBeacon", info);
        context.startActivity(dialogIntent);
    }
}
