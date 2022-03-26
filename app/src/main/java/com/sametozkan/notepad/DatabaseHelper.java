package com.sametozkan.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database
    private static final String DATABASE_NAME = "notepadDatabase";
    private static final int DATABASE_VERSION = 1;

    //Table
    public static final String TABLE_NOTE = "note";

    //Columns of TABLE_NOTE
    public static final String NOTE_ID = "id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_TEXT = "text";
    public static final String NOTE_DATE = "date";
    public static final String NOTE_FAV = "favorite";
    public static final String NOTE_LABEL = "label";

    //Create TABLE_NOTE
    String CREATE_TABLE_NOTE = "CREATE TABLE " + TABLE_NOTE + " ( " +
            NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOTE_DATE + " TEXT, " +
            NOTE_FAV + " TEXT, " +
            NOTE_TITLE + " TEXT, " +
            NOTE_TEXT + " TEXT, " +
            NOTE_LABEL + " LABEL " +
            ")";

    public DatabaseHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //
    }

    public void addNote(String title, String text, int favorite, String label) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues note = new ContentValues();

        note.put(NOTE_TITLE, title);
        note.put(NOTE_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        note.put(NOTE_TEXT, text);
        note.put(NOTE_FAV, favorite);
        note.put(NOTE_LABEL, label);
        sqLiteDatabase.insert(TABLE_NOTE, null, note);

        sqLiteDatabase.close();
    }

    public ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + NOTE_LABEL + " FROM " + TABLE_NOTE +
                " ORDER BY " + NOTE_LABEL + " ASC ", null);

        int LABEL_COLUMN_ID = cursor.getColumnIndex(NOTE_LABEL);

        try {
            if (cursor.moveToFirst()) {
                do {
                    labels.add(cursor.getString(LABEL_COLUMN_ID));
                }
                while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        sqLiteDatabase.close();
        return labels;
    }

    public ArrayList<NoteModel> getNotes(@Nullable String label, int favorite) {
        System.out.println(favorite);
        ArrayList<NoteModel> list = new ArrayList<>();
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (label == null && favorite == 0) {
            Log.i("cursor", "Cursor has specified for all notes.");
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTE +
                    " ORDER BY " + NOTE_DATE + " DESC ", null);
        }
        else if (label == null && favorite == 1) {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTE + " WHERE " +
                    NOTE_FAV + " = 1" + " ORDER BY " + NOTE_DATE + " DESC ", null);
            Log.i("cursor", "Cursor has specified for favorite notes.");
        }
        else if (label != null && favorite == 0) {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NOTE + " WHERE " +
                    NOTE_LABEL + " = ?" + " ORDER BY " + NOTE_DATE + " DESC ", new String[]{label});
            Log.i("cursor", "Cursor has specified for label searching.");
        }
        else {
            Log.e("cursor", "Cursor is null.");
            return null;
        }
        int ID_COLUMN_INDEX = cursor.getColumnIndex(NOTE_ID);
        int TITLE_COLUMN_INDEX = cursor.getColumnIndex(NOTE_TITLE);
        int TEXT_COLUMN_INDEX = cursor.getColumnIndex(NOTE_TEXT);
        int DATE_COLUMN_INDEX = cursor.getColumnIndex(NOTE_DATE);
        int FAV_COLUMN_INDEX = cursor.getColumnIndex(NOTE_FAV);
        int LABEL_COLUMN_INDEX = cursor.getColumnIndex(NOTE_LABEL);

        try {
            if (cursor.moveToFirst()) {
                do {
                    NoteModel note = new NoteModel(
                            cursor.getInt(ID_COLUMN_INDEX),
                            cursor.getString(TITLE_COLUMN_INDEX),
                            cursor.getString(TEXT_COLUMN_INDEX),
                            cursor.getString(DATE_COLUMN_INDEX),
                            cursor.getInt(FAV_COLUMN_INDEX),
                            cursor.getString(LABEL_COLUMN_INDEX));

                    list.add(note);
                }
                while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        sqLiteDatabase.close();

        return list;
    }

}