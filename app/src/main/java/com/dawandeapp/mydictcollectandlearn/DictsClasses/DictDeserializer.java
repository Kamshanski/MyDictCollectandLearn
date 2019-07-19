package com.dawandeapp.mydictcollectandlearn.DictsClasses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DictDeserializer implements JsonDeserializer<Dict> {
    private String dictName; //Название словаря
    //Конструктор, который САМ я ПЕРЕОПРЕДЕЛИЛ для вставки ИМЕНИ из ПУТИ
    public DictDeserializer(String name) {
        super(); //РОдительский конструктор
        dictName = name; //Имя словря из пути
    }

    @Override
    public Dict deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //Json в виде объекта
        JsonObject jsonObject = json.getAsJsonObject();

        int dictType = jsonObject.get("type").getAsInt(); //Тип
        int L1 = jsonObject.get("1L").getAsInt(); //ИнЯз
        int L2 = jsonObject.get("2L").getAsInt(); //РодЯз
        //Массив пар, который преобразуется в список
        Pair[] arrOfPairs = context.deserialize(jsonObject.getAsJsonArray("pairs"), Pair[].class);
        List<Pair> listOfPairs = new ArrayList<>(Arrays.asList(arrOfPairs));
        //Итоговый ПОЛНЫЙ словарь
        return new Dict(dictName, dictType, L1, L2, listOfPairs);
    }
}
