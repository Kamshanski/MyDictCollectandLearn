package com.dawandeapp.mydictcollectandlearn.DictsClasses;

import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class DictCover {
    transient protected String dictName;
    @SerializedName("1L") protected int foreignLangCode;
    @SerializedName("2L") protected int translationLangCode;
    @SerializedName("type") protected int dictType = 0;
    transient private int SIZE; //Специальная переменная, для знания длины словаря, без дополнительного обращения к Json-file
    transient protected final static String JSON = ".json"; //Расширение .json

    //Конструкторы
    public DictCover(String dictName, int dictType, int fLangPos, int tLangPos) {
        //Конструктор НОВОГО словаря
        this.dictName = dictName;
        this.foreignLangCode = fLangPos;
        this.translationLangCode = tLangPos;
        this.dictType = dictType;
    }

    public DictCover(String dictName, int dictType, int fLangPos, int tLangPos, int size) {
        //Конструктор ОТКРЫТОГО словаря с размером SIZE
        this.dictName = dictName;
        this.foreignLangCode = fLangPos;
        this.translationLangCode = tLangPos;
        this.dictType = dictType;
        this.SIZE = size;
    }

    //Открывает все Обложки в папке
    public static List<DictCover> openAll(Path path) {
        List<DictCover> result = new ArrayList<>(); //Список всех Обложек
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) { //Создаём поток в папку
            for (Path entry: stream) {
                result.add(DictCover.open(entry)); // Добавляем пути к файлам в список
            }
        } catch (IOException ex) { M.d("createPATH has problems pypypy");}
        return result;
    }

    //Октрытие Обложки по пути
    public static DictCover open(Path path) {
        try {
            String jString = new String(Files.readAllBytes(path)); //Json-file в виде строки
            String dictName = path.getFileName().getName(0).toString().replace(JSON, ""); //Название словаря из пути
            //Gson через Десериализатор обложки
            Gson gson = new GsonBuilder().registerTypeAdapter(DictCover.class, new DictCoverDeserializer(dictName)).create();
            return gson.fromJson(jString, DictCover.class);
        }
        catch (IOException ex) {
            M.d("Crash while OPENING Dict");
            return new Dict("dictName", 1, 1, 1);
        }
    }

    public void deleteDict() {
        try {
            Files.deleteIfExists(getPath());
        }
        catch (IOException ex) {
            M.d("Error while delete dict".concat(dictName));
        }
    }

    //Переименование файла (Статический метод)
    public static void rename(String oldName, String newName) {
        try {
            Path oldPath = Paths.get(M.appPath().concat(Dict.json(oldName))); //Старый путь
            Path newPath = Paths.get(oldPath.getParent().toString().concat(Dict.json(newName))); //Новый путь (с новым названием)
            Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING); //Переименование через move
        }
        catch (IOException ex) {
            M.d("Error while rename Dict ".concat(oldName).concat(" to ").concat(newName));
        }
    }

    //Переименование файла
    public void renameWith(String newName) {
        rename(dictName, newName);
        dictName = newName;
    }

    public int size() { return SIZE; } //Размер словаря
    public String getDictName() {return dictName;} //Имя
    public int getForeignLangCode() {return foreignLangCode;} //Код иностранного языка
    public int getTranslationLangCode() {return translationLangCode;} //Код родного языка
    public int getDictType() {return dictType;} //Тип языка
    public Path getPath() { return Paths.get(M.appPath().concat(Dict.json(dictName)));} //Путь к словарю

    //Обложка в виде строки
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dictName.concat(" : ")); //Имя
        s.append(String.valueOf(dictType).concat(" : ")); //Тип
        s.append(String.valueOf(foreignLangCode).concat(" : ")); //ИнЯз
        s.append(String.valueOf(translationLangCode).concat(" : ")); //РодЯз
        s.append(String.valueOf(SIZE)); //Размер
        //Итоговая строка
        return s.toString();
    }
}
