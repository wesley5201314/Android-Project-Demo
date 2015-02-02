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
 * @date 2015��1��30�� ����5:29:44
 * 
 */
public class VideoService {

	/**
	 * ��ȡ���µ���Ƶ��Ѷ
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
	 * ��ȡ���µ���Ƶ��Ѷ
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
	 * ����json����
	 * @param inStream
	 * @return
	 */
	private static List<Video> parserJSON(InputStream inStream) throws Exception{
		// TODO Auto-generated method stub
		List<Video> list = new ArrayList<Video>();
		//��ȡ��Դ����������
		byte[] data = StreamUtils.readStream(inStream);
		//�Ѷ���������ת���ı�json���Ǽ򵥵��ı��ļ�
		String json = new String(data);
		//����һ��json�������
		JSONArray jsonArray = new JSONArray(json);
		for(int i=0;i<jsonArray.length();i++){
			//��ȡjson����
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Video video = new Video(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getInt("time"));
			list.add(video);
		}
		return list;
	}

	/**
	 * ����xml������
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	private static List<Video> parserXml(InputStream inStream) throws Exception{
		List<Video> list = new ArrayList<Video>();
		Video video = null;
		XmlPullParser pullParser = Xml.newPullParser();
		//ΪPull����������Ҫ������XML����
		pullParser.setInput(inStream, "UTF-8");
		//�õ���һ���¼�
		int event = pullParser.getEventType();
		//�ж��ǲ����ĵ������¼�
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
