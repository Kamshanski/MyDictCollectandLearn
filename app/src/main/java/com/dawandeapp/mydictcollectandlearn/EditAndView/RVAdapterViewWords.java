package com.dawandeapp.mydictcollectandlearn.EditAndView;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Dict;
import com.dawandeapp.mydictcollectandlearn.DictsClasses.Pair;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.ClearableEditText;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.nio.file.Path;

public class RVAdapterViewWords extends RecyclerView.Adapter<RVAdapterViewWords.ViewHolder> {
    final private Dict dict;
    private Context dContext;

    //Конструктор, который тупо записывает все данные
    public RVAdapterViewWords(Context context, Path path) {
        this.dContext = context; //Это Контекст активити
        this.dict = Dict.open(path); //Это нужный словарь
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tx_ViewWords_foreignWord, tx_ViewWords_translationWord; //Текст Пара слов
        CardView cardView;
        ImageButton imb_ViewWords_popUpMenu;

        //Конструктор хранителя виджетов(view). Здесь же и Лисенер, но его можно щ как-т по др сделать
        public ViewHolder(@NonNull final View viewHolder) {
            super(viewHolder);
            cardView = viewHolder.findViewById(R.id.CardView_ViewWords);
            tx_ViewWords_foreignWord = viewHolder.findViewById(R.id.tx_ViewWords_foreignWord);
            tx_ViewWords_translationWord = viewHolder.findViewById(R.id.tx_ViewWords_translationWord);
            imb_ViewWords_popUpMenu = viewHolder.findViewById(R.id.imb_ViewWords_popUpMenu);

            //Лисенер на карточку (открываем слово для редактирования)
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vCard) {
                    //Пара слов для этой карточки (final чтобы не псоздавать новый элемент, только изменяем поле переменной по ссыдлке)
                    final Pair pair = dict.getPair(getAdapterPosition());
                    changePair(pair);
                }
            });

            imb_ViewWords_popUpMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Всплывающее меню, привязанное к контексту и КнопкеКартинке
                    PopupMenu popUp = new PopupMenu(dContext, imb_ViewWords_popUpMenu);
                    popUp.inflate(R.menu.view_words_popup); //Раздувание лэйаута меню
                    popUp.show();//Показ картинки
                    //Лисенер для всплывающего меню
                    popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //Пара слов для этой карточки (final чтобы не псоздавать новый элемент, только изменяем поле переменной по ссыдлке)
                            final Pair pair = dict.getPair(getAdapterPosition());
                            switch (item.getItemId()) {
                                //Пункт меню ИЗМЕНИТЬ
                                case R.id.menuItem_ViewWords_change: changePair(pair); return true;
                                //Пункт меню УДАЛИТЬ
                                case R.id.menuItem_ViewWords_delete:
                                    if (dict.size()-1 >= 5) {
                                        dict.deletePair(pair);
                                        notifyItemRemoved(getAdapterPosition());
                                        return true;
                                    }
                                    else {
                                        M.wSh(dContext, "В словаре должно быть не меньше 5 пар");
                                        return false;
                                    }

                                //Отмена меню
                                default: return false;
                            }
                        }
                    });
                }
            });
        }

        //Вынесенная отдельно функция (при нажати на cardView и меню Изменить)
        private void changePair(final Pair oldPair) {
            //Диалоговое окно
            final Dialog changeDialog = new Dialog(dContext);
            //Заголово (Изменить слова)
            changeDialog.setTitle(dContext.getResources().getString(R.string.btn_change)
                    .concat(" ")
                    .concat(dContext.getResources().getString(R.string.tx_words)));
            //Лэйаут, который внутри Диалога
            changeDialog.setContentView(R.layout.view_words_dialog_change);
            changeDialog.create(); //Создание Диалога
            changeDialog.show(); //Показ Диалога

            //Заполнение всех полей Диалога, установка лисенеров

            final ClearableEditText cedt_foreignName = changeDialog.findViewById(R.id.cedt_ViewWords_dialog_foreignName);
            final ClearableEditText cedt_translationName = changeDialog.findViewById(R.id.cedt_ViewWords_dialog_translationName);
            //Установка фильтров для КЭдитТекстов
            cedt_foreignName.setFilters(new InputFilter[] { M.jsonFilter() });
            cedt_translationName.setFilters(new InputFilter[] { M.jsonFilter() });
            //Установка старых слов в КЭдитТекст
            cedt_foreignName.setText(oldPair.getForeign());
            cedt_translationName.setText(oldPair.getTranslation());
            //Установка подсказок для КЭдитТекст
            cedt_foreignName.setHint(String.format(
                    dContext.getResources().getString(R.string.edtHint_ForeignWordIn),
                    dContext.getResources().getStringArray(R.array.languages_long_prep)[dict.getForeignLangCode()]));
            cedt_translationName.setHint(String.format(
                    dContext.getResources().getString(R.string.edtHint_TranslationWordIn),
                    dContext.getResources().getStringArray(R.array.languages_long_prep)[dict.getTranslationLangCode()]));

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
                    String newTWord = cedt_translationName.getText().toString().trim();
                    //Если хоть одно слово отличается, то нужно записать его в newPair
                    if (newFWord.isEmpty() || newTWord.isEmpty()) {
                        M.wSh(dContext, dContext.getResources().getString(R.string.warn_EmptyFields));
                    }
                    else if (!oldPair.getForeign().equals(newFWord) || !oldPair.getTranslation().equals(newTWord)) {
                        //Создаю новую Пару
                        Pair newPair = new Pair(cedt_foreignName.getText().toString().trim(),
                                dict.getForeignLangCode(),
                                cedt_translationName.getText().toString().trim(),
                                dict.getTranslationLangCode());
                        //Заменяю слова в старой паре на слова новой пары
                        dict.changePair(oldPair, newPair);
                    }
                    notifyItemChanged(getAdapterPosition());
                    changeDialog.cancel();
                    changeDialog.dismiss();
                }
            });
        }
    }

    @Override
    //Этот метод для создания ВьюХолдера с Вью в виде КардВью
    public RVAdapterViewWords.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_words_cardview, parent, false);
        return new RVAdapterViewWords.ViewHolder(itemView);
    }

    @Override
    //Этод метод для связвания информации по каждому(position) элементу с его местом на карточке (по шаблону выше ^)
    public void onBindViewHolder(@NonNull final RVAdapterViewWords.ViewHolder holder, int position) {
        holder.tx_ViewWords_foreignWord.setText(dict.getPair(position).getForeign()); //Текст ИнЯз слово
        holder.tx_ViewWords_translationWord.setText(dict.getPair(position).getTranslation()); //Текст РодЯз слово
    }

    @Override
    //Возвращает количество карточек в RecyclerView
    public int getItemCount() {
        return dict.size();
    }

    //Возвращает коды языков словаря
    public int getForeignLangCode() { return dict.getForeignLangCode(); }
    public int getTranslationLangCode() { return dict.getTranslationLangCode(); }
    public void addPairToDict(String newFWord, String newTWord) {
        dict.append(newFWord, newTWord);
        notifyItemInserted(dict.size()-1);
        dict.deleteDict();
        dict.save();
    }
    public void changeDictName(String newFWord) {
        dict.renameWith(newFWord);
    }
}
