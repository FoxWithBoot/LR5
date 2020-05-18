package com.example.testretrofit;

import android.provider.BaseColumns;

import java.util.Date;

public class FeedEntry implements BaseColumns {
    public static final String DB_NAME = "BD";
    public static final String TITLE_COLUMN = "Col";
    public static final String TITLE_ID = "id";
    public static final String TITLE_DATE = "date";
    public static final String TABLE_NAME = "Save";
    public static final int DB_VERS = 1;

    public static final String CREATE =
            "CREATE TABLE "+
                    TABLE_NAME+"("+
                    TITLE_ID+
                    " INTEGER,"+
                    TITLE_COLUMN+
                    " TEXT, "+
                    TITLE_DATE+" INTEGER"+");";
    public static final String CHANGE_NAME0 =
            "UPDATE "+TABLE_NAME+
                    " SET "+TITLE_COLUMN+"='";
    public static final String CHANGE_NAME1 = "' WHERE "+TITLE_ID+"=";
    public static final String CHANGE_NAME2 = ";";
    public static final String CHANGE_DATE0 = "UPDATE "+TABLE_NAME+" SET "+TITLE_DATE+"="+Long.toString(new Date().getTime())+" WHERE "+TITLE_ID+"=";
    public static final String CHANGE_DATE1 = ";";
}
