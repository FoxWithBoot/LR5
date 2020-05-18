package com.example.testretrofit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class SQLHelper extends SQLiteOpenHelper {

    public SQLHelper(Context context, int ver) { //конструктор
        super(context, FeedEntry.DB_NAME, null, ver);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //для создания БД
        ContentValues cv = new ContentValues(); // объект данных
        db.execSQL(FeedEntry.CREATE);
        for(int i=0; i<11; i++) {
            cv.put(FeedEntry.TITLE_ID, i);
            cv.put(FeedEntry.TITLE_COLUMN, "No inf");
            cv.put(FeedEntry.TITLE_DATE, new Date().getTime());
            db.insert(FeedEntry.TABLE_NAME, null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //для обновления
    }
}
