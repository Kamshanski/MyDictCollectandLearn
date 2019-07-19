package com.dawandeapp.mydictcollectandlearn.DictsClasses;

import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


//      Это, если хранить во ВНУТРЕННЕМ хранилище
//Path dictPath = Paths.get(context.getFilesDir().getPath().concat(json(dictName)));
//      Это, - если во ВНЕШНЕМ хранилице
//Paths.get(M.appPath().concat(Dict.json(dictName)));

public class Dict extends DictCover {
    //Списов всех пар слов
    @SerializedName("pairs") private List<Pair> pairsList = new ArrayList<>();

    //Конструкторы
    public Dict(String dictName, int dictType, int fLangPos, int tLangPos) {
        //Конструктор пустого словаря
        super(dictName, dictType, fLangPos, tLangPos);
    }
    public Dict(String dictName, int dictType, int fLangPos, int tLangPos, List<Pair> pairs) {
        //Конструктор полного словаря
        super(dictName, dictType, fLangPos, tLangPos);
        pairsList = pairs;
    }

    //Открыть словарь из файла через path
    public static Dict open(Path path) {
        try {
            String jString = new String(Files.readAllBytes(path)); //Json-file в виде строки
            String dictName = path.getFileName().getName(0).toString().replace(JSON, ""); //Получение названия словаря из файла
            //Получение gson с помощью Десериалайзера
            Gson gson = new GsonBuilder().registerTypeAdapter(Dict.class, new DictDeserializer(dictName)).create();
            return gson.fromJson(jString, Dict.class); //Возврат словаря
        }
        catch (IOException ex) {
            M.d("Crash while OPENING Dict");
            return new Dict("dictName", 1, 1, 1);
        }
    }

    //Сохранить словарь
    public void save() {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setPrettyPrinting().create();
            Path dictPath = Paths.get(M.appPath().concat(json(dictName))); //Путь к словарю
            if (Files.exists(dictPath)) {
                Files.createFile(dictPath); //Создать файл
            }
            Files.write(dictPath, gson.toJson(this).getBytes()); //Записать в файл Json-стоку
        }
        catch (IOException ex) {
            M.d("Crash while SAVING Dict");
        }
    }

    public void deletePair(final Pair pair) {
        //Пока так, потому что можно просто открыть поток, считать строку, переделать json, записать файл, закрыть поток
        pairsList.remove(pair);
        deleteDict();
        save();
    }

    public void changePair(final Pair oldPair, final Pair newPair) {
        //Пока так, потому что можно просто открыть поток, считать строку, переделать json, записать файл, закрыть поток
        pairsList.set(pairsList.indexOf(oldPair), newPair);
        deleteDict();
        save();
    }

    //Добавление новой пары слов
    public void append(String foreign, String translation) {
        pairsList.add(new Pair(foreign, foreignLangCode, translation, translationLangCode));
    }

    public static String json(String dictName) {return "/".concat(dictName.concat(JSON));} //Имя словаря "/ИМЯ_СЛОВАРЯ.json"
    public List<Pair> getPairs() { return pairsList;} //Получить список пар
    public Pair getPair(int i) { return pairsList.get(i);} //получать пару по номеру
    @Override public int size() {return pairsList.size();} //Размер словаря

    //Словарь в ивде строки, если его нужно посмотреть
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        //Имя
        stringBuilder.append(dictName.concat("\n"));
        //Тип
        stringBuilder.append(String.format("Dict type: %s", (dictType == 1) ? "unilateral\n" : "bilateral\n"));
        //Языки
        stringBuilder.append(String.format(Locale.ROOT, "First lang: %d, second lang: %d\n", foreignLangCode, translationLangCode));
        //Слова
        for (Pair pair: pairsList) {
            stringBuilder.append(pair.toString().concat("\n"));
        }
        //Итоговая строка
        return stringBuilder.toString();
    }
}