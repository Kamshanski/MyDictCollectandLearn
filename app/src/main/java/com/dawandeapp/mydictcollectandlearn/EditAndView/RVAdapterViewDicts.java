package com.dawandeapp.mydictcollectandlearn.EditAndView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.dawandeapp.mydictcollectandlearn.DictsClasses.DictCover;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.ClearableEditText;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

import java.nio.file.Path;
import java.util.List;

public class RVAdapterViewDicts extends RecyclerView.Adapter<RVAdapterViewDicts.ViewHolder> {

    private Context dContext;
    private List<DictCover> dictCovers;

    //Конструктор, который тупо записывает все данные
    public RVAdapterViewDicts(Context context, Path path) {
        this.dContext = context; //Это Контекст активити
        this.dictCovers = DictCover.openAll(path); //Это все найденные словари в папке активити
    }

    //Это создаётся класс, который хранит инфу и виджеты, в которые суётся инфа. Потом этот класс используется в методе onBind~~~
    public class ViewHolder extends RecyclerView.ViewHolder {
        //Всякие виджетики
        TextView tx_ViewDicts_dictName, tx_ViewDicts_langTypeSize;
        CardView cardView;
        ImageButton imb_ViewDicts_popUpMenu;
        //Хранение данных одной карточки
        //DictCover  dictCover;
        //int position;
        //private Dialog mDialog;

        //Конструктор хранителя виджетов(view). Здесь же и Лисенер, но его можно щ как-т по др сделать
        public ViewHolder(final View viewHolder){
            super(viewHolder);
            cardView = viewHolder.findViewById(R.id.CardView_ViewDicts); //Сама карточка
            tx_ViewDicts_dictName = viewHolder.findViewById(R.id.tx_ViewDicts_dictName); //Текст Имя словаря
            tx_ViewDicts_langTypeSize = viewHolder.findViewById(R.id.tx_ViewDicts_langTypeSize); //Текст Тип, Языки, Размер
            imb_ViewDicts_popUpMenu = viewHolder.findViewById(R.id.imb_ViewDicts_popUpMenu); //КнопкаКартинка всплывающего МЕНЮ

            //Лисенер на карточку (открываем словарь)
            cardView.setOnClickListener(vCard -> {
                //Передаём название словаря, чтобы его открыть
                Intent intent = new Intent(dContext, ViewWordsActivity.class);
                intent.putExtra("toViewWords_dictName", dictCovers.get(getAdapterPosition()).getDictName());
                dContext.startActivity(intent);
            });

            //Лисенер для КнопкиКартинки
            imb_ViewDicts_popUpMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View vImBtn) {
                    //Всплывающее меню, привязанное к контексту и КнопкеКартинке
                    PopupMenu popUp = new PopupMenu(dContext, imb_ViewDicts_popUpMenu);
                    popUp.inflate(R.menu.view_dicts_popup); //Раздувание лэйаута меню
                    popUp.show();//Показ картинки
                    //Лисенер для всплывающего меню
                    popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //Текущая обложка (final чтобы не псоздавать новый элемент, только изменяем поле переменной по ссыдлке)
                            final DictCover dictCover = dictCovers.get(getAdapterPosition());
                            switch (item.getItemId()) {
                                //Пункт меню ПЕРЕИМЕНОВАТЬ
                                case R.id.menuItem_ViewDicts_rename:
                                    //Старое название, записанное в Тэг ВьюХолдера (потом менятемт при notifyItemChange
                                    final String oldName = vImBtn.getTag().toString();
                                    //Диалоговое окно
                                    final Dialog renameDialog = new Dialog(dContext);
                                    //Заголовок(, которого нет))) ) (Переименовать словарь)
                                    renameDialog.setTitle(
                                            dContext.getResources().getString(R.string.btn_rename)
                                                    .concat(" ")
                                                    .concat(dContext.getResources().getString(R.string.tx_dict)));
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
                                    cedt_newName.setText(oldName);
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
                                            dictCover.renameWith(newName);
                                            notifyItemChanged(getAdapterPosition());
                                            renameDialog.cancel();
                                            renameDialog.dismiss();
                                        }
                                    });
                                    return true;

                                //Пункт меню ИЗМЕНИТЬ
                                case R.id.menuItem_ViewDicts_change:
                                    //Передаём название словаря, чтобы его открыть
                                    Intent intent = new Intent(dContext, ViewWordsActivity.class);
                                    intent.putExtra("toViewWords_dictName", dictCovers.get(getAdapterPosition()).getDictName());
                                    dContext.startActivity(intent);
                                    return true;

                                //Пункт меню УДАЛИТЬ
                                case R.id.menuItem_ViewDicts_delete:
                                    dictCover.deleteDict();
                                    dictCovers.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    //Этот метод для создания ВьюХолдера с Вью в виде КардВью
    public RVAdapterViewDicts.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_dicts_cardview, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    //Этод метод для связвания информации по каждому(position) элементу с его местом на карточке (по шаблону выше ^)
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DictCover dCover = dictCovers.get(position); //Поулчаем Обложку из списка
        holder.tx_ViewDicts_dictName.setText(dCover.getDictName()); //Название словаря
        //Строка с информацией о словаре
        String s = String.format(dContext.getResources().getString(R.string.tx_ViewDicts_dictInfo),
                dCover.size(),
                dContext.getResources().getStringArray(R.array.languages_short)[dCover.getForeignLangCode()],
                dContext.getResources().getStringArray(R.array.tx_CreateDict_Arrow)[dCover.getDictType()],
                dContext.getResources().getStringArray(R.array.languages_short)[dCover.getTranslationLangCode()]);
        holder.tx_ViewDicts_langTypeSize.setText(s);
        //Добавление название словаря в Тэг этой КнопкиКартинки, для последующего использования
        holder.imb_ViewDicts_popUpMenu.setTag(dCover.getDictName());
    }

    //Вроде возвращает количество карточек в RecyclerView
    @Override
    public int getItemCount() {
        return dictCovers.size();
    }
}
