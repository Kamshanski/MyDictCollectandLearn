package com.dawandeapp.mydictcollectandlearn.Learning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Dict;
import com.dawandeapp.mydictcollectandlearn.MainActivity;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.LearningAdapter;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.LearningView;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.nio.file.Paths;

public class LearningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        Intent intent = getIntent();
        String dictName = intent.getStringExtra("toLearningActivity_dictName");
        M.d(dictName);
        Dict dict = Dict.open(Paths.get(M.appPath().concat(Dict.json(dictName))));
        int[] modes = intent.getIntArrayExtra("toLearningActivity_checkedModesPositions");
        M.d(modes.toString());

        Button btn_continue = findViewById(R.id.btn_Learning_continue);
        TextView tx_counter = findViewById(R.id.tx_Learning_counter);

        btn_continue.setEnabled(false);
        tx_counter.setText(String.format(getResources().getString(R.string.tx_Learning_counter),
                0,
                dict.size()));

        LearningView LearnV = findViewById(R.id.LearnV);
        LearnV.setAdapter(new LearningAdapter(dict, modes, btn_continue, tx_counter, LearnV));
        LearnV.start();
    }

    @Override
    public void onBackPressed() {
        Intent intentToMainActivity = new Intent(this, MainActivity.class);
        intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToMainActivity);
        //TODO: dialog to ensure user's will to go back;
    }
}
