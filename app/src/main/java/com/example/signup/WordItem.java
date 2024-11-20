package com.example.signup;

public class WordItem {
    private String word;
    private String meaning;
    private boolean isSaved;

    public WordItem(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
        this.isSaved = false;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}

