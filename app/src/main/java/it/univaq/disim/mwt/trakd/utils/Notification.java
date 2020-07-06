package it.univaq.disim.mwt.trakd.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import it.univaq.disim.mwt.trakd.R;
import it.univaq.disim.mwt.trakd.UserCollectionActivity;

public class Notification {

    private static final int NOTIFICATION_ID = 1000;

    public static void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = context.getString(R.string.channel_id);
            String channelName = context.getString(R.string.channel_name);;
            String channelDescription = context.getString(R.string.channel_description);

            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static void backupStatusNotification(Context context, String title, String content){
        String channelId = context.getString(R.string.channel_id);

        Intent intent = new Intent(context, UserCollectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
