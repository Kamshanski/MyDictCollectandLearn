package com.dawandeapp.mydictcollectandlearn;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dawandeapp.mydictcollectandlearn.Create.CreateDictActivity;
import com.dawandeapp.mydictcollectandlearn.EditAndView.ViewDictsActivity;
import com.dawandeapp.mydictcollectandlearn.Learning.ChooseDictActivity;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.TestActivity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        try {
            Files.createDirectory(Paths.get(M.appPath()));
            M.d(Paths.get(M.appPath()).toString());
        } catch (IOException ex) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            M.d("Folder wasnt created");
        }


    }
    public void toNextPage(View view) {
        Intent intent = new Intent(this, NextPage.class);
        startActivity(intent);

        Intent intentToReceiver = new Intent(getApplicationContext(), AlarmReceiver.class);

        final AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        final PendingIntent alarmPIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intentToReceiver, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 2);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis()+10000, alarmPIntent);

    }

    public void toCreateDictActivity(View view) {
        startActivity(new Intent(this, CreateDictActivity.class));
    }
    public void toViewDictsActivity(View view) {
        startActivity(new Intent(this, ViewDictsActivity.class));
    }

    public void toChooseDictActivity(View view) {
        startActivity(new Intent(this, ChooseDictActivity.class));
    }

    public void toTestActivity(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }
}
