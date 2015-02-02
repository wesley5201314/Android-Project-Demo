package com.example.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.utils.StreamUtils;

/**
 * @author wesley
 * @version 
 * @date 2015年1月30日 下午2:28:12
 * 
 */
public class PageService {

	public static String getPage(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("GET");
		if(connection.getResponseCode()==200){
			InputStream in = connection.getInputStream();
			byte[] data = StreamUtils.readStream(in);
			return new String(data,"UTF-8");
		}
		return null;
	}

}
