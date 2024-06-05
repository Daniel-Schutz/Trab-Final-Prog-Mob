package com.progmob.android.friendkeeper.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.progmob.android.friendkeeper.R;

import java.util.Calendar;

public class NotificationUtil {
    private static final String CHANNEL_ID = "FriendKeeperChannel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1001; // Adicione um código de solicitação para a permissão

    public static void createNotificationChannel(Context context) {
        CharSequence name = "FriendKeeperChannel";
        String description = "Channel for FriendKeeper";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public static void scheduleNotification(Context context, String title, String message, Calendar calendar) {
        if (AlarmUtil.canScheduleExactAlarms(context)) {
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } catch (SecurityException e) {
                // Handle exception if permission is not granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AlarmUtil.requestExactAlarmPermission(context);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmUtil.requestExactAlarmPermission(context);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
