package com.carl.co.reader.db;

public class DbTags
{	
	//
	public final static String DB_NAME = "book.db";
	
	//
	public final static String TABLE_BOOK_INFO = "book_info";
	
	//
	public final static String FIELD_BOOK_ID = "book_id";
	public final static String FIELD_BOOK_TITLE = "book_title";
	public final static String FIELD_BOOK_IMAGE = "book_image";
	public final static String FIELD_BOOK_PATH = "book_path";
	public final static String FIELD_BOOK_SPEED = "book_speed";
	
	//provider
	public final static String DB_PROVIDER = "content://com.carl.co.reader.BookContentProvider";
	public final static String URI_TABLE_BOOK_INFO = DB_PROVIDER+"/"+TABLE_BOOK_INFO;
}
