package com.dawandeapp.mydictcollectandlearn.EditAndView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.nio.file.Paths;

public class ViewDictsActivity extends Activity {
    RecyclerView RV_ViewDicts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dicts);

        getIntent().getBooleanExtra("to_ViewDictsActivity_dictNameChanged", false);

        RV_ViewDicts = findViewById(R.id.RV_ViewDicts);
        RVAdapterViewDicts RVAdapter = new RVAdapterViewDicts(this, Paths.get(M.appPath()));
        RV_ViewDicts.setAdapter(RVAdapter);
        M.d(String.valueOf(RV_ViewDicts.hasPendingAdapterUpdates()));

        TextView tx_ViewDicts_dictsAmount = findViewById(R.id.tx_ViewDicts_dictsAmount);
        tx_ViewDicts_dictsAmount.setText(String.format(getResources().getString(R.string.tx_ViewDicts_dictsAmount), RVAdapter.getItemCount()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        M.d("Wellcome back to ViewDicts");
        boolean dictNameChanged = intent.getBooleanExtra("toViewDictsActivity_dictNameChanged", false);
        if (dictNameChanged) {
            RVAdapterViewDicts RVAdapter = new RVAdapterViewDicts(this, Paths.get(M.appPath()));
            RV_ViewDicts.setAdapter(RVAdapter);
            M.d("Adapter was changed");
        }
    }
}
