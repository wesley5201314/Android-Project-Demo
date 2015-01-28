package com.example.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author wesley
 * @version 
 * @date 2015��1��27�� ����10:04:11
 * 
 */
public class DBHelper extends SQLiteOpenHelper {


	public DBHelper(Context context) {
		super(context, "test.db", null, 3);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//�������ݿ�ÿһ�α�������ʱ����õ�
		//db.execSQL("CREATE TABLE person(personid integer primary key autoincrement, name varchar(20), phone VARCHAR(12) NULL)");
		db.execSQL("CREATE TABLE person(personid integer primary key autoincrement, name varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("ALTER TABLE person ADD phone integer");
		db.execSQL("ALTER TABLE person ADD amount integer");
	}

}
