package com.example.providerObserver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.view.View;

/**
 * @author wesley
 * @version 
 * @date 2015��1��29�� ����11:43:24
 * 
 */
public class Demo {
	//AӦ�õ�ִ�� �����ṩ�� �������
	/*public void insert(View v){
    	Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person");
		ContentResolver resolver = this.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "A-App");
		values.put("phone", "18607687688");
		values.put("amount", "700000");
		resolver.insert(uri, values);
    }*/
	
	//BӦ�ü��������ṩ�� ��ȡʵʱ����
	
	 /** Called when the activity is first created. */
    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person");
        this.getContentResolver().registerContentObserver(uri, true, new PersonContentObserver(new Handler()));
    }
    
    private class PersonContentObserver extends ContentObserver{

		public PersonContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			// select * from person order by personid desc limit 1
			Uri uri = Uri.parse("content://cn.itcast.providers.personprovider/person");
			Cursor cursor = getContentResolver().query(uri, null, null, null, "personid desc limit 1");
			if(cursor.moveToFirst()){
				String name = cursor.getString(cursor.getColumnIndex("name"));
				Log.i("MainActivity", name);
			}
		}
    	
    }*/
}
