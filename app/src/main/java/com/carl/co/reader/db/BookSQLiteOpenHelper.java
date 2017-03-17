package com.carl.co.reader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Host-0 on 2017/3/17.
 */

public class BookSQLiteOpenHelper extends SQLiteOpenHelper {

    public BookSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBookInfoSql = "CREATE TABLE [" + DbTags.TABLE_BOOK_INFO + "] ("
                + "[" + DbTags.FIELD_BOOK_ID + "] INTEGER,"
                + "[" + DbTags.FIELD_BOOK_TITLE + "] VARCHAR,"
                + "[" + DbTags.FIELD_BOOK_IMAGE + "] VARCHAR, "
                + "[" + DbTags.FIELD_BOOK_PATH + "] VARCHAR, "
                + "[" + DbTags.FIELD_BOOK_SPEED + "] REAL);";
        db.execSQL(createBookInfoSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropBookInfoSql = "drop table if exists book_info";
        db.execSQL(dropBookInfoSql);
        onCreate(db);
    }
}
