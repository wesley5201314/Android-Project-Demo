package com.example.services;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author wesley
 * @version 
 * @date 2015��1��26�� ����3:40:51
 * 
 */
public class ShardPreferencesServices {

	private Context context;

	public ShardPreferencesServices(Context context) {
		this.context = context;
	}
	
	/**
	 * �����û�����ƫ��
	 * @param name
	 * @param age
	 */
	public void save(String name,Integer age){
		//�õ�SharedPreferences����
		SharedPreferences preferences = context.getSharedPreferences("test",Context.MODE_PRIVATE);
		//�õ��༭��
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.putInt("age", age);
		//�ύ
		editor.commit();
	}

	/**
	 * ��ȡƫ������
	 * @return
	 */
	public Map<String, String> getPreferences(){
		//������������
		Map<String, String> params = new HashMap<String, String>();
		//�õ�SharedPreferences����
		SharedPreferences preferences = context.getSharedPreferences("test", Context.MODE_PRIVATE);
		//��������ֵ
		params.put("name", preferences.getString("name", ""));
		params.put("age", String.valueOf(preferences.getInt("age", 0)));
		return params;
	}
	
}
