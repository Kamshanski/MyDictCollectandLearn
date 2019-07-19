package com.dawandeapp.mydictcollectandlearn.Learning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dawandeapp.mydictcollectandlearn.Learning.Modes.Modes;
import com.dawandeapp.mydictcollectandlearn.Learning.Modes.TaskChooseTranslation;
import com.dawandeapp.mydictcollectandlearn.Learning.Modes.TaskTrustCard;
import com.dawandeapp.mydictcollectandlearn.Learning.Modes.TaskTypeTranslation;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.util.Arrays;

public class ChooseModesActivity extends AppCompatActivity {

    ListView LV_ChooseModes;
    String dictName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_modes);

        initLearningConstants();

        dictName = getIntent().getStringExtra("toChooseModes_dictName");
        //ListView, который предоставляет выбор режимов изучения
        LV_ChooseModes = findViewById(R.id.LV_ChooseModes);
        //Адаптер для ListView
        if (this != null) {
            M.d("this no null");
        }
        if (true) {
            M.d(String.valueOf(android.R.layout.simple_list_item_multiple_choice));
        }
        if (Modes.getMODES() != null) {
            M.d(Modes.getMODES().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, Modes.getMODES());
        //Ставим адаптер в ListView
        LV_ChooseModes.setAdapter(adapter);
    }

    public void onStratLearning(View view) {
        SparseBooleanArray SPBoolArray = LV_ChooseModes.getCheckedItemPositions();
        int checkedItemCount = 0;
        for (int i = 0; i< Modes.getMODES().length; i++) {
            if (SPBoolArray.get(i)) {
                checkedItemCount++;
            }
        }
        int[] checkedModesPositions = new int[checkedItemCount];
        int j = 0;
        for (int i = 0; i< Modes.getMODES().length; i++) {
            if (SPBoolArray.get(i)) {
                checkedModesPositions[j++] = i;
            }
        }
        M.d(Arrays.toString(checkedModesPositions));
        M.d(checkedModesPositions.toString());

        Intent intentToLearningActivity = new Intent(this, LearningActivity.class);
        intentToLearningActivity.putExtra("toLearningActivity_dictName", dictName);
        intentToLearningActivity.putExtra("toLearningActivity_checkedModesPositions", checkedModesPositions);

        startActivity(intentToLearningActivity);
    }

    public void initLearningConstants() {
        //Если человек будет всё-таки учить словарь, то в ModeBase class нужно подгрузить все режимы в виде
        //  статической констант
        Modes.setModes(getResources().getStringArray(R.array.modes_names));
        TaskTypeTranslation.setID(0); //Написать перевод
        TaskChooseTranslation.setID(1); //Написать перевод
        TaskTrustCard.setID(2); //Написать перевод
    }
}
