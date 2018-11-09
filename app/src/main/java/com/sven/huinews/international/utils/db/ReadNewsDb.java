package com.sven.huinews.international.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ReadNewsDb {
    private Context mContext;
    SQLiteDatabase mSQLiteDatabase;
    private static ReadNewsDb mInstance;
    private ReadNewsDb(Context context) {
        mContext = context;
    }
    public static ReadNewsDb getInstance(Context context){
        if (mInstance==null){
            synchronized (ReadNewsDb.class){
                if (mInstance==null){
                    mInstance = new ReadNewsDb(context);
                }
            }
        }
        return mInstance;
    }

    /**
     *  插入历史搜索
     * @param keyword
     */
    public void insertInSearchKeyWord(String keyword){
        mSQLiteDatabase = new ReadNewsTaskOpenHelper(mContext).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReadNewsTaskOpenHelper.HISTORY_SEARCH_KEYWORD,keyword);
        mSQLiteDatabase.insert(ReadNewsTaskOpenHelper.HISTORY_SEARCH_TABLE,null,contentValues);
        mSQLiteDatabase.close();
    }

    /**
     * 获取历史搜索
     * @return
     */
    public ArrayList<String> getAllHistroySearchList(){
        mSQLiteDatabase = new ReadNewsTaskOpenHelper(mContext).getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(ReadNewsTaskOpenHelper.HISTORY_SEARCH_TABLE,null,null,null,null,null,null,"21");
        while (cursor.moveToNext()){
            String keyword = cursor.getString(cursor.getColumnIndex(ReadNewsTaskOpenHelper.HISTORY_SEARCH_KEYWORD));
            if (list.contains(keyword)){
                continue;
            }
            list.add(keyword);
        }
        mSQLiteDatabase.close();
        return list;
    }

    /**
     * 插入新闻id
     * @param newsId
     */
    public void insertInReadNewsId(String newsId){
        mSQLiteDatabase = new ReadNewsTaskOpenHelper(mContext).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReadNewsTaskOpenHelper.COLUMN_NEWS_ID_NAME,newsId);
        mSQLiteDatabase.insert(ReadNewsTaskOpenHelper.NEWS_TABLE,null,contentValues);
        mSQLiteDatabase.close();
    }

    /**
     * 读取新闻数据
     * @return
     */
    public ArrayList<String> getAllReadNewsList(){
        mSQLiteDatabase = new ReadNewsTaskOpenHelper(mContext).getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(ReadNewsTaskOpenHelper.NEWS_TABLE,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String newsId = cursor.getString(cursor.getColumnIndex(ReadNewsTaskOpenHelper.COLUMN_NEWS_ID_NAME));
            list.add(newsId);
        }
        mSQLiteDatabase.close();
        return list;

    }

    public void deleteDb(){
        mSQLiteDatabase = new ReadNewsTaskOpenHelper(mContext).getWritableDatabase();
        mSQLiteDatabase.execSQL("delete from "+ReadNewsTaskOpenHelper.NEWS_TABLE);
        mSQLiteDatabase.close();
    }

    public void closeDb(){
        mSQLiteDatabase.close();
    }
}
