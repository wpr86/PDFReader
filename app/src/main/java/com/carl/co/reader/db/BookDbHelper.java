package com.carl.co.reader.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.carl.co.reader.book.BookBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Host-0 on 2017/3/17.
 */

public class BookDbHelper {
    public static List<BookBean> getBookInfo(ContentResolver resolver) {
        List<BookBean> bookList = new ArrayList<BookBean>();
        BookBean bookBean;

        Cursor cursor = resolver.query(Uri.parse(DbTags.URI_TABLE_BOOK_INFO),
                null, null, null, null);
        if(cursor == null)
            return bookList;
        while (cursor.moveToNext()) {
            long id = Long.parseLong(getFieldContent(cursor, DbTags.FIELD_BOOK_ID));
            String title = getFieldContent(cursor, DbTags.FIELD_BOOK_TITLE);
            String imagePath = getFieldContent(cursor, DbTags.FIELD_BOOK_IMAGE);
            float speed = Float.parseFloat(getFieldContent(cursor, DbTags.FIELD_BOOK_SPEED));
            String path = getFieldContent(cursor, DbTags.FIELD_BOOK_PATH);
            bookBean = new BookBean(id, title, imagePath, speed, path);
            bookList.add(bookBean);
        }
        cursor.close();

        return bookList;
    }

    public static void saveBookInfo(ContentResolver resolver, List<BookBean> bookBeanList){
        deleteAllBooks(resolver);
        for (BookBean bookBean:bookBeanList){
            insertToBookInfo(resolver,bookBean);
        }
    }

    public static void insertToBookInfo(ContentResolver resolver, BookBean bookbean) {
        ContentValues values = new ContentValues();
        values.put(DbTags.FIELD_BOOK_ID, bookbean.getId());
        values.put(DbTags.FIELD_BOOK_TITLE, bookbean.getTitle());
        values.put(DbTags.FIELD_BOOK_IMAGE, bookbean.getImagePath());
        values.put(DbTags.FIELD_BOOK_SPEED, bookbean.getSpeed());
        values.put(DbTags.FIELD_BOOK_PATH, bookbean.getPath());
        resolver.insert(Uri.parse(DbTags.URI_TABLE_BOOK_INFO), values);
    }


    public static String getFieldContent(Cursor cursor, String fieldName) {
        return cursor.getString(cursor.getColumnIndex(fieldName));
    }

    public static void deleteAllBooks(ContentResolver resolver){
        resolver.delete(Uri.parse(DbTags.URI_TABLE_BOOK_INFO),DbTags.FIELD_BOOK_PATH+"!=-1",null);
    }

    public static void deleteBook(ContentResolver resolver, String bookPath) {
        resolver.delete(Uri.parse(DbTags.URI_TABLE_BOOK_INFO),
                DbTags.FIELD_BOOK_PATH + "=?", new String[] { bookPath + "" });
    }
}
