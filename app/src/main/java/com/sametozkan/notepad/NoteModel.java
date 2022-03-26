package com.sametozkan.notepad;

public class NoteModel {
    private int id;
    private String title;
    private String text;
    private String date;
    private int favorite;
    private String label;

    NoteModel(int id, String title, String text, String date, int favorite, String label) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.favorite = favorite;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
