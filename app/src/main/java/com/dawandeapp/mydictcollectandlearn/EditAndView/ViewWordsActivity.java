package com.dawandeapp.mydictcollectandlearn.EditAndView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Dict;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.ClearableEditText;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.nio.file.Paths;

public class ViewWordsActivity extends AppCompatActivity {
    private RecyclerView RV_ViewWords;
    String dictName;
    String oldDictName;
    boolean dictNameChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_words);
        dictName = getIntent().getStringExtra("toViewWords_dictName");

        TextView tx_ViewWords_dictName = findViewById(R.id.tx_ViewWords_dictName);
        tx_ViewWords_dictName.setText(String.format(getResources().getString(R.string.tx_ViewWords_dictName), dictName));

        RV_ViewWords = findViewById(R.id.RV_ViewWords);
        RV_ViewWords.setAdapter(new RVAdapterViewWords(this, Paths.get(M.appPath().concat(Dict.json(dictName)))));

    }

    public void onAddNewPair(View view) {
        //Диалоговое окно
        final Dialog changeDialog = new Dialog(this);
        //Заголово (Изменить слова)
        changeDialog.setTitle(getResources().getString(R.string.btn_change)
                .concat(" ")
                .concat(getResources().getString(R.string.tx_words)));
        //Лэйаут, который внутри Диалога
        changeDialog.setContentView(R.layout.view_words_dialog_change);
        changeDialog.create(); //Создание Диалога
        changeDialog.show(); //Показ Диалога

        //Заполнение всех полей Диалога, установка лисенеров

        @NonNull final ClearableEditText cedt_foreignName = changeDialog.findViewById(R.id.cedt_ViewWords_dialog_foreignName);
        @NonNull final ClearableEditText cedt_translationName = changeDialog.findViewById(R.id.cedt_ViewWords_dialog_translationName);
        final int foreignLangCode = ((RVAdapterViewWords) RV_ViewWords.getAdapter()).getForeignLangCode();
        final int translationLangCode = ((RVAdapterViewWords) RV_ViewWords.getAdapter()).getTranslationLangCode();

        //Установка фильтров для КЭдитТекстов
        cedt_foreignName.setFilters(new InputFilter[] { M.jsonFilter() });
        cedt_translationName.setFilters(new InputFilter[] { M.jsonFilter() });
        //Установка подсказок для КЭдитТекст
        cedt_foreignName.setHint(String.format(
                getResources().getString(R.string.edtHint_ForeignWordIn),
                getResources().getStringArray(R.array.languages_long_prep)[foreignLangCode]));
        cedt_translationName.setHint(String.format(
                getResources().getString(R.string.edtHint_TranslationWordIn),
                getResources().getStringArray(R.array.languages_long_prep)[translationLangCode]));

        //Установка Лисенеров на кнопки
        final Button btn_cancel = changeDialog.findViewById(R.id.btn_ViewWords_dialog_cancel);
        final Button btn_OK = changeDialog.findViewById(R.id.btn_ViewWords_dialog_OK);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDialog.cancel();
                changeDialog.dismiss();
            }
        });
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFWord = cedt_foreignName.getText().toString().trim();
                String newNWord = cedt_translationName.getText().toString().trim();
                //Добавляем пару
                ((RVAdapterViewWords) RV_ViewWords.getAdapter()).addPairToDict(newFWord, newNWord);
                changeDialog.cancel();
                changeDialog.dismiss();
            }
        });
    }

    public void onEditDictName(View view) {
        //TextView, который отображает текущее название словаря
        final TextView tx_ViewWords_dictName = findViewById(R.id.tx_ViewWords_dictName);
        //Диалоговое окно
        final Dialog renameDialog = new Dialog(this);
        //Заголовок(, которого нет))) ) (Переименовать словарь)
        renameDialog.setTitle(
                getResources().getString(R.string.btn_rename).
                concat(" ").
                concat(getResources().getString(R.string.tx_dict)));
        //Лэйаут, который внутри Диалога
        renameDialog.setContentView(R.layout.view_dicts_dialog_rename);
        renameDialog.create(); //Создание Диалога
        renameDialog.show(); //Показ Диалога

        //Заполнение всех полей Диалога, установка лисенеров

        //КЭдитТекст с именем, кнопки Ок и Отмена
        final ClearableEditText cedt_newName = renameDialog.findViewById(R.id.cedt_ViewDitcs_dialog_newName);
        final Button btn_cancel = renameDialog.findViewById(R.id.btn_ViewDitcs_dialog_cancel);
        final Button btn_OK = renameDialog.findViewById(R.id.btn_ViewDitcs_dialog_OK);
        //Фильтр для КЭдитТекста
        cedt_newName.setFilters(new InputFilter[] { M.jsonFilter() });
        //В КЭдитТекст сразу пишем страое название
        cedt_newName.setText(dictName);
        //Лисенер для кнопки Отмена
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameDialog.cancel();
                renameDialog.dismiss();
            }
        });
        //Лисенер для кнопки ОК
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = cedt_newName.getText().toString();
                M.d(newName);
                //Не знаю зачем так делаю, но пусть будет (пока)
                oldDictName = String.copyValueOf(dictName.toCharArray());
                dictName = newName;
                M.d(dictName);
                ((RVAdapterViewWords) RV_ViewWords.getAdapter()).changeDictName(dictName);
                dictNameChanged = true;
                //TextView, который отображает текущее название словаря
                tx_ViewWords_dictName.setText(String.format(getResources().getString(R.string.tx_ViewWords_dictName), dictName));
                renameDialog.cancel();
                renameDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToViewDictsActivity = new Intent(this, ViewDictsActivity.class);
        intentToViewDictsActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (dictNameChanged) {
            intentToViewDictsActivity.putExtra("toViewDictsActivity_dictNameChanged", dictNameChanged);
        }
        startActivity(intentToViewDictsActivity);
    }
}
