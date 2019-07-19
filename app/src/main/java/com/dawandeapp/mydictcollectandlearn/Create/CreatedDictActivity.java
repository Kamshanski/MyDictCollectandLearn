package com.dawandeapp.mydictcollectandlearn.Create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Dict;
import com.dawandeapp.mydictcollectandlearn.DictsClasses.Pair;
import com.dawandeapp.mydictcollectandlearn.MainActivity;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.nio.file.Paths;
import java.util.Locale;

public class CreatedDictActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_dict);
        Intent intentFromCreateWords = getIntent();
        M.wL(this, getResources().getString(R.string.warn_DictCreatedSuccessfully));

        String nameOfNewDict = intentFromCreateWords.getStringExtra("toCreatedDict_nameOfNewDict");
        int typeOfNewDict = intentFromCreateWords.getIntExtra("toCreatedDict_typeOfNewDict", 2);
        int posOfFirstLang = intentFromCreateWords.getIntExtra("toCreatedDict_posOfFirstLang", 1);
        int posOfSecondLang = intentFromCreateWords.getIntExtra("toCreatedDict_posOfSecondLang", 0);

        LinearLayout LL_CreatedDict_pairsList = findViewById(R.id.LL_CreatedDict_pairsList);
        TextView tx_CreatedDict_DictWasCreatedSuccessfully = findViewById(R.id.tx_CreatedDict_DictWasCreatedSuccessfully);
        tx_CreatedDict_DictWasCreatedSuccessfully.setText(String.format(Locale.ROOT, getResources().getString(R.string.tx_CreatedDict_DictWasCreatedSuccessfully), nameOfNewDict));

        //Загрузка словаря
        Dict createdDict = Dict.open(Paths.get(M.appPath().concat(Dict.json(nameOfNewDict))));

        //Показ количества слоов в словаре и язков словаря
        TextView tx_CreatedDict_pairAmount = findViewById(R.id.tx_CreatedDict_pairAmount);
        String s = String.format(getResources().getString(R.string.tx_CreatedDict_pairAmount),
                createdDict.size(),
                getResources().getStringArray(R.array.languages_long_prep)[createdDict.getForeignLangCode()],
                getResources().getStringArray(R.array.languages_long_prep)[createdDict.getTranslationLangCode()]);
        tx_CreatedDict_pairAmount.setText(s);

        //Создаём Вью, которые записываются в СкролВью для вывода всех записанных слов
        for (Pair pair: createdDict.getPairs()) {
            //(@ Для убирания ошибки на null) Раздуваем ряд для СкролВью, в котором сиди ЛинерЛэйаут
            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.scroll_view_completed_dict, null);

            //Записываем в раздутый Вью все штуки
            TextView tx_createdDict_foreignWord = view.findViewById(R.id.tx_createdDict_foreignWord);
            tx_createdDict_foreignWord.setText(pair.getForeign());
            TextView tx_createdDict_translationWord = view.findViewById(R.id.tx_createdDict_translationWord);
            tx_createdDict_translationWord.setText(pair.getTranslation());

            //Добавляем Вью в ЛинеарЛэйаут
            LL_CreatedDict_pairsList.addView(view);
        }
    }

    //Обработка назад
    @Override
    public void onBackPressed() {
        Intent intentToMainActivity = new Intent(this, MainActivity.class);
        intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToMainActivity);
    }
}
