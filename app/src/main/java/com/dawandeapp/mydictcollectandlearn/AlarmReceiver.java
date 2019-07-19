package com.dawandeapp.mydictcollectandlearn;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager mNM = (NotificationManager)
                context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent contentIntent = new Intent(context.getApplicationContext(), OpenAct.class);
        contentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context.getApplicationContext(), "Daifu")
                        //обязательные параметры
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Время вповторить")
                        .setContentText("Пришло время " + "Daifu")
                        // НЕобязательные параметры
                        .setTicker("Время учиться!")
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(contentPendingIntent)
                        .setShowWhen(true);
        Notification notification = builder.build();
        NotificationChannel channel = new NotificationChannel("Daifu", "Daifu" + "_channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(0 , notification);


    }
}
