package com.dawandeapp.mydictcollectandlearn.DictsClasses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class DictCoverDeserializer implements JsonDeserializer<DictCover> {
    private String dictName; //Название словаря
    //Конструктор, который САМ я ПЕРЕОПРЕДЕЛИЛ , чтобы вставить в словарь ИМЯ, полученное ИЗ ПУТИ
    public DictCoverDeserializer(String name) {
        super(); //Родительский конструктор
        dictName = name; //Название словаря
    }

    //ОБЯЗАТЕЛЬНОЕ переопределение десериализации
    @Override
    public DictCover deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        //Предсталяем json в виде объекта
        JsonObject jsonObject = json.getAsJsonObject();

        int dictType = jsonObject.get("type").getAsInt(); //Тип
        int L1 = jsonObject.get("1L").getAsInt(); //ИнЯз
        int L2 = jsonObject.get("2L").getAsInt(); //РодЯз
        //Массив пар, для записи размера словаря
        Pair[] arrOfPairs = context.deserialize(jsonObject.getAsJsonArray("pairs"), Pair[].class);
        //Итоговая Обложка с рамером
        return new DictCover(dictName, dictType, L1, L2, arrOfPairs.length);
    }
}
