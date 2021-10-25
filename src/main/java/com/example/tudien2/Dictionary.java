package com.example.tudien2;

import java.util.ArrayList;

public class Dictionary {
    private ArrayList<Word> words = new ArrayList<>();

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words){
        this.words = words;
    }
}