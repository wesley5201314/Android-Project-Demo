package com.example.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author wesley
 * @version 
 * @date 2015年1月28日 下午5:08:55
 * 测试内容提供者
 * 
 */
public class TestProvider extends AndroidTestCase {
	private static final String TAG = "AccessContentProviderTest";
	
	public void testInsert() throws Exception{
		Uri uri = Uri.parse("content://com.example.providers.personprovider/person");
		ContentResolver resolver = this.getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "person");
		values.put("phone", "123456789");
		values.put("amount", "50000000");
		resolver.insert(uri, values);
	}
	
	
	public void testDelete() throws Exception{
		Uri uri = Uri.parse("content://com.example.providers.personprovider/person/25");
		ContentResolver resolver = this.getContext().getContentResolver();
		resolver.delete(uri, null, null);
	}
	
	public void testUpdate() throws Exception{
		Uri uri = Uri.parse("content://com.example.providers.personprovider/person/1");
		ContentResolver resolver = this.getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "zhangxiaoxiao12321");
		resolver.update(uri, values, null, null);
	}
	
	public void testQuery() throws Exception{
		Uri uri = Uri.parse("content://com.example.providers.personprovider/person");
		ContentResolver resolver = this.getContext().getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, "personid asc");
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			Log.i(TAG, name);
		}
		cursor.close();
	}

}
