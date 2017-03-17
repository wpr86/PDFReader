package com.carl.co.reader.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Host-0 on 2017/3/17.
 */

public class BookContentProvider extends ContentProvider {
    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        BookSQLiteOpenHelper bookSQLiteOpenHelper = new BookSQLiteOpenHelper(this.getContext(),DbTags.DB_NAME, null, 1);
        db = bookSQLiteOpenHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = db.insert(uri.getLastPathSegment(), null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return db.delete(uri.getLastPathSegment(), selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
    }
}
