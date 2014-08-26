package pl.surecase.co.ibeaconapp.ibeaconapp.patternTools.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import pl.surecase.co.ibeaconapp.ibeaconapp.R;

public class NotificationTools {

    public static void postOngoingNotification(String title, String msg, int notificationId, Context context,
                                               NotificationManager manager, NotificationCompat.Builder builder) {
        long[] vibratePattern = {1, 600, 1000};
        Intent notifyIntent = new Intent(context, Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                context,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setVibrate(vibratePattern)
                .setContentIntent(pendingIntent);
        manager.notify(notificationId, builder.build());
    }

    public static void postNotification(String title, String msg, int notificationId, Context context,
                                  NotificationManager manager, NotificationCompat.Builder builder) {
        Intent notifyIntent = new Intent(context, Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                context,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        manager.notify(notificationId, builder.build());
    }

    public static void updateNotification(String title, String msg, int notificationId, Context context,
                                    NotificationManager manager, NotificationCompat.Builder builder) {
        Intent notifyIntent = new Intent(context, Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                context,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        manager.notify(notificationId, builder.build());
    }

}
