package com.dawandeapp.mydictcollectandlearn.zHelpClasses;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.R;

public class TestActivity extends AppCompatActivity {

    TextView textView;
    ConstraintLayout cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SeekBar seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView5);
        cardView = findViewById(R.id.CardView_rotateMe);
        textView.setOnClickListener(v -> {
            M.d("tpuch");
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float ang = ((float) (progress-50))*180/100;
                textView.setRotationY(ang);
                cardView.setRotationY(ang);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
