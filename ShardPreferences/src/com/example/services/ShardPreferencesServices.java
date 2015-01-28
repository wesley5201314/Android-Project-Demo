package com.example.services;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author wesley
 * @version 
 * @date 2015年1月26日 下午3:40:51
 * 
 */
public class ShardPreferencesServices {

	private Context context;

	public ShardPreferencesServices(Context context) {
		this.context = context;
	}
	
	/**
	 * 保存用户设置偏好
	 * @param name
	 * @param age
	 */
	public void save(String name,Integer age){
		//得到SharedPreferences对象
		SharedPreferences preferences = context.getSharedPreferences("test",Context.MODE_PRIVATE);
		//得到编辑器
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.putInt("age", age);
		//提交
		editor.commit();
	}

	/**
	 * 读取偏好配置
	 * @return
	 */
	public Map<String, String> getPreferences(){
		//创建参数集合
		Map<String, String> params = new HashMap<String, String>();
		//得到SharedPreferences对象
		SharedPreferences preferences = context.getSharedPreferences("test", Context.MODE_PRIVATE);
		//给参数赋值
		params.put("name", preferences.getString("name", ""));
		params.put("age", String.valueOf(preferences.getInt("age", 0)));
		return params;
	}
	
}
