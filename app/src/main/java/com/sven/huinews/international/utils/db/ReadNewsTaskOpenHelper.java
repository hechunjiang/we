package com.sven.huinews.international.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReadNewsTaskOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "readNewsDb";
    public static final String NEWS_TABLE = "readNewsTable";
    public static final String HISTORY_SEARCH_TABLE = "historySearchTable";
    public static final String COLUMN_NEWS_ID_NAME = "newsId";
    public static final String HISTORY_SEARCH_KEYWORD = "search_id";

    String stu_table="create table "+NEWS_TABLE+"(_id integer primary key autoincrement,"+COLUMN_NEWS_ID_NAME+" text)";
    String history_tab="create table "+HISTORY_SEARCH_TABLE+"(_id integer primary key autoincrement,"+HISTORY_SEARCH_KEYWORD+" text)";

    public ReadNewsTaskOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(stu_table);
        db.execSQL(history_tab);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
