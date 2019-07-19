package com.dawandeapp.mydictcollectandlearn.Create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dawandeapp.mydictcollectandlearn.MainActivity;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.ClearableEditText;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

public class CreateDictActivity extends AppCompatActivity {

    private ClearableEditText edt_NameOfNewDict;
    private TextView tx_dictTypeHint;
    private TextView tx_dictTypeArrow;
    private RadioGroup radioGroup;
    private Spinner spin_lang1;
    private Spinner spin_lang2;

    //Начальные положения для выпадающих списков с языками
    private int curPos1 = 2;
    private int curPos2 = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dict);
        edt_NameOfNewDict = findViewById(R.id.edt_NameOfNewDict);
        tx_dictTypeHint = findViewById(R.id.tx_dictTypeHint);
        tx_dictTypeArrow = findViewById(R.id.tx_dictTypeArrow);
        spin_lang1 = findViewById(R.id.spin_lang1);
        spin_lang2 = findViewById(R.id.spin_lang2);

        //Начальный крестик между языками
        tx_dictTypeArrow.setText(getResources().getStringArray(R.array.tx_CreateDict_Arrow)[0]);

        //Установка фильтра для CEditText'a
        edt_NameOfNewDict.setFilters(new InputFilter[] { M.jsonFilter() });

        //Установка Лисенера на РадиоКнопка
        radioGroup = findViewById(R.id.radgr_dictType);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        //(На случай ядерной войны) обработка пустого выбора
                        Toast.makeText(CreateDictActivity.this, "Ничего не выбрано", Toast.LENGTH_SHORT).show();
                        tx_dictTypeHint.setText(getResources().getString(R.string.tx_CreateDict_hintNone));
                        tx_dictTypeArrow.setText(getResources().getStringArray(R.array.tx_CreateDict_Arrow)[0]);
                        break;
                    case R.id.radbtn_unilateral:
                        //Обработка выбора Одностороннего словаря (подсказка по типу и стрелочка)
                        tx_dictTypeHint.setText(getResources().getString(R.string.tx_CreateDict_hint1));
                        tx_dictTypeArrow.setText(getResources().getStringArray(R.array.tx_CreateDict_Arrow)[1]);
                        break;
                    case R.id.radbtn_bilateral:
                        //Обработка выбора Двустороннего словаря (подсказка по типу и стрелочка)
                        tx_dictTypeHint.setText(getResources().getString(R.string.tx_CreateDict_hint2));
                        tx_dictTypeArrow.setText(getResources().getStringArray(R.array.tx_CreateDict_Arrow)[2]);
                        break;
                }

            }
        });

        //Адаптер для выпадающего списка (Спинера)
        LangSpinnerAdapter adapter1 = new LangSpinnerAdapter(this, R.layout.spin_lang_one_row, //Разметка строки в Спинере
                getResources().getStringArray(R.array.languages_long), //Массив коротких языков
                getResources().getStringArray(R.array.languages_short)); //Массив длинных языков
        spin_lang1.setAdapter(adapter1); //Вставка адаптера в спинер
        spin_lang1.setPromptId(R.string.tx_Language2); //Какой-то Айди
        spin_lang1.setSelection(curPos1-1, true); //Начальный выбор (Английский)

        //Аналогично второй адаптери и спинер
        LangSpinnerAdapter adapter2 = new LangSpinnerAdapter(this, R.layout.spin_lang_one_row,
                getResources().getStringArray(R.array.languages_long),
                getResources().getStringArray(R.array.languages_short));
        spin_lang2.setAdapter(adapter2);
        spin_lang2.setPromptId(R.string.tx_Language2);
        spin_lang2.setSelection(curPos2-1, true); //Начальный выбор (Русский)

        //Лисенер для тыканья кнопок
        spin_lang1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Если выбор одинаковый, на место второго нужно поставить то, что было в первом
                if (pos==spin_lang2.getSelectedItemPosition()) {
                    spin_lang2.setSelection(curPos1);
                    curPos2=curPos1;
                }
                curPos1=pos;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //Ничего, ничего, пусто
            }
        });

        //Аналогичный Лисенер для второго спинера
        spin_lang2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos==spin_lang1.getSelectedItemPosition()) {
                    spin_lang1.setSelection(curPos2);
                    curPos1=curPos2;
                }
                curPos2=pos;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });
    }


    //Кнопка продолжить для перехода к CreateWordsActivity
    public void onContinue(View view) {
        //Если навание словаря пустое и не выбран тип, то НЕ переходим
        if (edt_NameOfNewDict.getText().toString().trim().isEmpty() || radioGroup.getCheckedRadioButtonId()==-1) {
            M.wSh(this, getResources().getString(R.string.warn_EmptyFields));
        }
        else {
            //Переход вместе Навзванием, Типом, 1 и 2 Языками
            Intent intentToCreateWords = new Intent(this, CreateWordsActivity.class);
            intentToCreateWords.putExtra("toCreateWords_nameOfNewDict", edt_NameOfNewDict.getText().toString());
            intentToCreateWords.putExtra("toCreateWords_typeOfNewDict", (radioGroup.getCheckedRadioButtonId() == R.id.radbtn_unilateral) ? 1 : 2);
            intentToCreateWords.putExtra("toCreateWords_posOfFirstLang", spin_lang1.getSelectedItemPosition());
            intentToCreateWords.putExtra("toCreateWords_posOfSecondLang", spin_lang2.getSelectedItemPosition());

            startActivity(intentToCreateWords);
        }
    }

    //Если нажали назад
    @Override
    public void onBackPressed() {
        //TODO: сделать диал окно для подтверждения утраты словаря
        Intent intentToMainActivity = new Intent(this, MainActivity.class);
        intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intentToMainActivity);
    }

    //Адаптер для спиннера
    public class LangSpinnerAdapter extends ArrayAdapter<String> {
        String[] strArrayLong, strArrayShort;
        int layoutId;

        //Тут определяется контекст. Разметка каждой штуки, список с диннным и короткими словами сказу вписан в код
        public LangSpinnerAdapter(Context context, int layoutId, String[] strArrayLong, String[] strArrayShort) {
            super(context, layoutId, strArrayLong);
            this.strArrayLong = strArrayLong;
            this.strArrayShort = strArrayShort;
            this.layoutId = layoutId;
        }


        @Override //Это метод для получения Вью полосок для большого выпадающего окна
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, 1);
        }

        @Override //Это метод для получения Вью полосок для выбранной полоски
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent, 0);
        }
        //Это собственный метод для создания Вью полосок
        public View getCustomView(int position, View convertView, ViewGroup parent, int size) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(layoutId, parent, false);
            TextView label = row.findViewById(R.id.tx_spinLangName);
            ImageView icon = (ImageView) row.findViewById(R.id.im_spinLangIcon);
            //Если 0, то выводится короткое название языка. Если 1, - длинное
            if (size==0) {
                label.setText(strArrayShort[position]);
            }
            else {
                label.setText(strArrayLong[position]);
            }
            icon.setImageResource(R.drawable.ic_launcher_background);
            return row;
        }
    }
}
