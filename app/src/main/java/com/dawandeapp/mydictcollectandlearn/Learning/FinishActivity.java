package com.dawandeapp.mydictcollectandlearn.Learning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.AlarmReceiver;
import com.dawandeapp.mydictcollectandlearn.MainActivity;
import com.dawandeapp.mydictcollectandlearn.R;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Intent intent = getIntent();
        String dictName = intent.getStringExtra("toFinish_dictName");
        int dictSize = intent.getIntExtra("toFinish_dictSize", 0);
        int correctAnswers = intent.getIntExtra("toFinish_correctAnswers", 0);
        TextView tx_Finish_results = findViewById(R.id.tx_Finish_results);
        tx_Finish_results.setText(String.format(getResources().getString(R.string.tx_Finish_results),
                dictName,
                correctAnswers,
                dictSize));
    }

    @Override
    public void onBackPressed() {
        Intent intentToMainActivity = new Intent(this, MainActivity.class);
        intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToMainActivity);
        //TODO: dialog to ensure user's will to go back;
    }

    public void Notif(View view) {
        Intent intentToReceiver = new Intent(getApplicationContext(), AlarmReceiver.class);
        final AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        final PendingIntent alarmPIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intentToReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis()+5*1000, alarmPIntent);

    }
}
