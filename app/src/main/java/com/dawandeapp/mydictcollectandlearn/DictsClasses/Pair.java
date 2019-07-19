package com.dawandeapp.mydictcollectandlearn.DictsClasses;

public class Pair {

    private Word foreignWord;
    private Word translationWord;

    //Констурктор пары
    public Pair(String foreign, int lang1, String translation, int lang2) {
        //Конструктор пары
        foreignWord = new Word(foreign, lang1); //Слово на ИнЯзе
        translationWord = new Word(translation, lang2); //Слово на РодЯзе
    }

    public String getForeign() { return foreignWord.getWord(); } //ИнЯз слово
    public String getForeignReading() { return foreignWord.getWord(); } //Чтение ИнЯз слова
    public String getTranslation() { return translationWord.getWord(); } //РодЯз слово
    public String getTranslationReading() { return translationWord.getWord(); } //Чтение РодЯза слова


    //Пара в виде строки
    @Override
    public String toString() {
        return foreignWord.getWord().concat(" : ").concat(translationWord.getWord());
    }
}
