package com.dawandeapp.mydictcollectandlearn.Learning;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.DictCover;
import com.dawandeapp.mydictcollectandlearn.R;

import java.nio.file.Path;
import java.util.List;

public class RVAdapterChooseDict extends RecyclerView.Adapter<RVAdapterChooseDict.ViewHolder> {

    private Context dContext;
    private List<DictCover> dictCovers;

    //Конструктор, который тупо записывает все данные
    public RVAdapterChooseDict(Context context, Path path) {
        this.dContext = context; //Это Контекст активити
        this.dictCovers = DictCover.openAll(path); //Это все найденные словари в папке активити
    }

    //Это создаётся класс, который хранит инфу и виджеты, в которые суётся инфа. Потом этот класс используется в методе onBind~~~
    public class ViewHolder extends RecyclerView.ViewHolder {
        //Всякие виджетики
        TextView tx_ChooseDict_dictName, tx_ChooseDict_langTypeSize;
        CardView cardView;

        //Конструктор хранителя виджетов(view). Здесь же и Лисенер, но его можно щ как-т по др сделать
        public ViewHolder(final View viewHolder){
            super(viewHolder);
            cardView = viewHolder.findViewById(R.id.CardView_ChooseDict); //Сама карточка
            tx_ChooseDict_dictName = viewHolder.findViewById(R.id.tx_ChooseDict_dictName); //Текст Имя словаря
            tx_ChooseDict_langTypeSize = viewHolder.findViewById(R.id.tx_ChooseDict_langTypeSize); //Текст Тип, Языки, Размер
        }
    }

    @Override
    //Этот метод для создания ВьюХолдера с Вью в виде КардВью
    public RVAdapterChooseDict.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_dict_cardview, parent, false);
        return new RVAdapterChooseDict.ViewHolder(itemView);
    }

    @Override
    //Этод метод для связвания информации по каждому(position) элементу с его местом на карточке (по шаблону выше ^)
    public void onBindViewHolder(final RVAdapterChooseDict.ViewHolder holder, int position) {
        DictCover dCover = dictCovers.get(position); //Поулчаем Обложку из списка
        holder.tx_ChooseDict_dictName.setText(dCover.getDictName()); //Название словаря
        //Строка с информацией о словаре
        String s = String.format(dContext.getResources().getString(R.string.tx_ViewDicts_dictInfo),
                dCover.size(),
                dContext.getResources().getStringArray(R.array.languages_short)[dCover.getForeignLangCode()],
                dContext.getResources().getStringArray(R.array.tx_CreateDict_Arrow)[dCover.getDictType()],
                dContext.getResources().getStringArray(R.array.languages_short)[dCover.getTranslationLangCode()]);
        holder.tx_ChooseDict_langTypeSize.setText(s);
        //Лисенер на карточку (открываем словарь)
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vCard) {
                //Передаём название словаря, чтобы его открыть
                Intent intent = new Intent(dContext, ChooseModesActivity.class);
                intent.putExtra("toChooseModes_dictName", dictCovers.get(position).getDictName());
                dContext.startActivity(intent);
            }
        });
    }

    //Вроде возвращает количество карточек в RecyclerView
    @Override
    public int getItemCount() {
        return dictCovers.size();
    }
}
