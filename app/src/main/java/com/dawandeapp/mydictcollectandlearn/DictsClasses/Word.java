package com.dawandeapp.mydictcollectandlearn.DictsClasses;

public class Word {
    private String word;
    private int langCode;
    private String reading;

    //Конструкторы
    Word(String word, int lang) {
        this.word = word; //Слово
        this.langCode = lang; //Код языка слова
    }

    String getWord() {
        return word;
    } //Слово
    void setWord(String newWord) {
        word = newWord;
    } //Установить новое слово
    public int getLangCode() {
        return langCode;
    } //Получить код ИнЯза
    public String getReading() {
        return reading;
    } //Получить код РодЯза
}
