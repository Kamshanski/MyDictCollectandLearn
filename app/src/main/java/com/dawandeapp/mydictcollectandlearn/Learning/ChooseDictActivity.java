package com.dawandeapp.mydictcollectandlearn.Learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.nio.file.Paths;

public class ChooseDictActivity extends AppCompatActivity {

    RecyclerView RV_ChooseDict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dict);

        RV_ChooseDict = findViewById(R.id.RV_ChooseDict);
        RVAdapterChooseDict RVAdapter = new RVAdapterChooseDict(this, Paths.get(M.appPath()));
        RV_ChooseDict.setAdapter(RVAdapter);
    }
}
