package com.example.services;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpInetConnection;

import com.example.utils.StreamUtils;

/**
 * @author wesley
 * @version 
 * @date 2015年1月29日 下午4:08:16
 * 
 */
public class ImageService {

	public static byte[] getImage(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("GET");
		if(connection.getResponseCode() == 200){
			InputStream in = connection.getInputStream();
			return StreamUtils.readStream(in);
		}
		return null;
	}

}
