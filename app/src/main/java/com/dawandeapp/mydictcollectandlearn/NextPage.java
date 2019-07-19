package com.dawandeapp.mydictcollectandlearn;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class NextPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_page);
    }

    public void Notif(View view) {
        Intent intentToReceiver = new Intent(getApplicationContext(), AlarmReceiver.class);
        final AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        final PendingIntent alarmPIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intentToReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis()+5*1000, alarmPIntent);

    }
}
