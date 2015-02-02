package com.example.services;

import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.example.domain.Video;
import com.example.utils.StreamUtils;

/**
 * @author wesley
 * @version 
 * @date 2015年1月30日 下午5:29:44
 * 
 */
public class VideoService {

	/**
	 * 获取最新的视频资讯
	 * @return
	 * @throws Exception
	 */
	public static List<Video> getAll() throws Exception{
		String urlString = "http://192.168.2.110:8080/LeYuCheng/VideoListServlet";
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200){
			InputStream inStream = conn.getInputStream();
			return parserXml(inStream);
		}
		return null;
	}
	
	/**
	 * 获取最新的视频资讯
	 * @return
	 * @throws Exception
	 */
	public static List<Video> getJSONAll() throws Exception{
		String urlString = "http://192.168.2.110:8080/LeYuCheng/VideoListServlet?format=json";
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200){
			InputStream inStream = conn.getInputStream();
			return parserJSON(inStream);
		}
		return null;
	}

	/**
	 * 解析json数据
	 * @param inStream
	 * @return
	 */
	private static List<Video> parserJSON(InputStream inStream) throws Exception{
		// TODO Auto-generated method stub
		List<Video> list = new ArrayList<Video>();
		//获取资源二进制数据
		byte[] data = StreamUtils.readStream(inStream);
		//把二进制数据转成文本json就是简单的文本文件
		String json = new String(data);
		//创建一个json数组对象
		JSONArray jsonArray = new JSONArray(json);
		for(int i=0;i<jsonArray.length();i++){
			//获取json对象
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Video video = new Video(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getInt("time"));
			list.add(video);
		}
		return list;
	}

	/**
	 * 解析xml的数据
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	private static List<Video> parserXml(InputStream inStream) throws Exception{
		List<Video> list = new ArrayList<Video>();
		Video video = null;
		XmlPullParser pullParser = Xml.newPullParser();
		//为Pull解析器设置要解析的XML数据
		pullParser.setInput(inStream, "UTF-8");
		//得到第一个事件
		int event = pullParser.getEventType();
		//判断是不是文档结束事件
		while(event != XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_TAG:
				if("video".equals(pullParser.getName())){
					int id = new Integer(pullParser.getAttributeValue(0));
					video = new Video();
					video.setId(id);
				}
				if("name".equals(pullParser.getName())){
					String name = pullParser.nextText();
					video.setName(name);
				}
				if("time".equals(pullParser.getName())){
					int time = new Integer(pullParser.nextText());
					video.setTimelength(time);
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("video".equals(pullParser.getName())){
					list.add(video);
					video = null;
				}
				break;
			}
			event = pullParser.next();
		}
		return list;
	}
}
