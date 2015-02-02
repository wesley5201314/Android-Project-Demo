package com.example.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * @author wesley
 * @version 
 * @date 2015年2月2日 下午12:29:42
 * 
 */
public class VideoService {
	
	private Context context;

	public VideoService(Context context) {
		this.context = context;
	}
	
	/**
	 * 发送并保存数据
	 * @param name
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public static boolean send(String name, String time) throws Exception{
		// TODO Auto-generated method stub
		String url = "http://192.168.2.110:8080/LeYuCheng/ManagerServlet";
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("time", time);
		try {
			return sendGetRequest(url, params, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 发送get请求
	 * @param path
	 * @param params
	 * @param string
	 * @return
	 * @throws Exception
	 */
	private static boolean sendGetRequest(String path,
			Map<String, String> params, String coding) throws Exception{
		StringBuilder url = new StringBuilder(path);
		url.append("?");
		for(Map.Entry<String, String> entry : params.entrySet()){
			url.append(entry.getKey()).append("=");
			url.append(URLEncoder.encode(entry.getValue(), coding));
			url.append("&");
		}
		url.deleteCharAt(url.length() - 1);
		HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			return true;
		}
		return false;
	}

}
