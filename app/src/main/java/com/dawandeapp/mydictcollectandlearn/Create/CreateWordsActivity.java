package com.dawandeapp.mydictcollectandlearn.Create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Dict;
import com.dawandeapp.mydictcollectandlearn.MainActivity;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.ClearableEditText;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.util.Locale;

public class CreateWordsActivity extends AppCompatActivity {
    ClearableEditText edt_newForeignWord;
    ClearableEditText edt_newTranslationWord;
    TextView tx_CreateWords_wordsCounter;
    LinearLayout LL_CreateWords_wordsCounter;

    String nameOfNewDict;
    int typeOfNewDict;
    int posOfFirstLang;
    int posOfSecondLang;

    Dict newDict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_words);
        Intent intentFromCreateDict = getIntent();

        //Получаем донные о словаре
        nameOfNewDict = intentFromCreateDict.getStringExtra("toCreateWords_nameOfNewDict");
        typeOfNewDict = intentFromCreateDict.getIntExtra("toCreateWords_typeOfNewDict", 2);
        posOfFirstLang = intentFromCreateDict.getIntExtra("toCreateWords_posOfFirstLang", 1);
        posOfSecondLang = intentFromCreateDict.getIntExtra("toCreateWords_posOfSecondLang", 0);

        tx_CreateWords_wordsCounter = findViewById(R.id.tx_CreateWords_wordsCounter);
        LL_CreateWords_wordsCounter = findViewById(R.id.LL_CreateWords_wordsCounter);

        //Устанавливаем подсказки на основе выбранных языков и фильтры
        edt_newForeignWord = findViewById(R.id.edt_NewForeignWord);
        String hintForForeignWord = String.format(getResources().getString(R.string.edtHint_ForeignWordIn), getResources().getStringArray(R.array.languages_long_prep)[posOfFirstLang]);
        edt_newForeignWord.setHint(hintForForeignWord);
        edt_newForeignWord.setFilters(new InputFilter[] { M.jsonFilter() });

        edt_newTranslationWord = findViewById(R.id.edt_NewTranslationWord);
        String hintForTranslationWord = String.format(getResources().getString(R.string.edtHint_TranslationWordIn), getResources().getStringArray(R.array.languages_long_prep)[posOfSecondLang]);
        edt_newTranslationWord.setHint(hintForTranslationWord);
        edt_newTranslationWord.setFilters(new InputFilter[] { M.jsonFilter() });

        //Создаём новый словарь
        newDict = new Dict(nameOfNewDict, typeOfNewDict, posOfFirstLang, posOfSecondLang);

    }

    //Добавление слова в словарь
    public void onAddNewWords(View view) {
        //Если хоть одно слово пустое, то Не добавляем
        if (edt_newForeignWord.getText().toString().trim().isEmpty() || edt_newTranslationWord.getText().toString().trim().isEmpty()) {
            //Если слово пустое, выдать предупреждение
            M.wSh(this, getResources().getString(R.string.warn_EmptyFields));
        }
        else {
            //Если всё норм, то добаляем в словарь, удалив пробелы, и меняем счётчик и его цвет
            newDict.append(edt_newForeignWord.getText().toString().trim(), edt_newTranslationWord.getText().toString().trim());
            if (newDict.size() >=5) { //Цвет ЗЕЛЁНЫЙ, т.к. слов достаточно
                tx_CreateWords_wordsCounter.setText(String.format(Locale.ROOT, getResources().getString(R.string.tx_CreateWords_EnoughWordsAreWritten), newDict.size()));
                LL_CreateWords_wordsCounter.setBackgroundColor(ContextCompat.getColor(this, R.color.correct));
            }
            else {  //Цвет КРАСНЫЙ, т.к. слов НЕ достаточно
                tx_CreateWords_wordsCounter.setText(String.format(Locale.ROOT, getResources().getString(R.string.tx_CreateWords_FewWordsAreWritten), newDict.size()));
                LL_CreateWords_wordsCounter.setBackgroundColor(ContextCompat.getColor(this, R.color.wrong));
            }
        }
    }

    //Переход на CreatedDictActivity
    public void onContinue(View view) {
        //Если хватает слов
        if (newDict.size() >= 5) {
            //Сохраняем словарь
            newDict.save();

            //Предаём данные о словаре в заключительный Активити
            Intent intentToCreatedDict = new Intent(this, CreatedDictActivity.class);
            intentToCreatedDict.putExtra("toCreatedDict_nameOfNewDict", nameOfNewDict);
            intentToCreatedDict.putExtra("toCreatedDict_typeOfNewDict", typeOfNewDict);
            intentToCreatedDict.putExtra("toCreatedDict_posOfFirstLang", posOfFirstLang);
            intentToCreatedDict.putExtra("toCreatedDict_posOfSecondLang", posOfSecondLang);

            startActivity(intentToCreatedDict);
        }
        else {
            //Если нехватает слов (меньше 5), то требуем щ слов
            M.wSh(this, getResources().getString(R.string.warn_NotEnoughWords));
        }
    }

    //Обработка перехода назад
    @Override
    public void onBackPressed() {
        //TODO: сделать диал окно для подтверждения утраты словаря
        Intent intentToMainActivity = new Intent(this, MainActivity.class);
        intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToMainActivity);
    }
}
