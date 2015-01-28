package com.example.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import android.annotation.SuppressLint;
import android.util.Xml;
import com.example.domain.Person;

/**
 * @author wesley
 * @version 
 * @date 2015年1月26日 上午11:32:09
 * 
 */
public class XmlService {
	
	/**
	 * 读取数据
	 * @param xml 要读取的数据
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("UseValueOf") 
	public static List<Person> readXml(InputStream xml) throws Exception{
		List<Person> persons = null;
		Person person = null;
		//创建一个Pull实例
		XmlPullParser pullParser = Xml.newPullParser();
		//为Pull解析器设置要解析的XML数据
		pullParser.setInput(xml, "UTF-8");
		//得到第一个事件
		int event = pullParser.getEventType();
		//判断是不是文档结束事件
		while(event != XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				persons = new ArrayList<Person>();
				break;
				
			case XmlPullParser.START_TAG:
				if("person".equals(pullParser.getName())){
					int id = new Integer(pullParser.getAttributeValue(0));
					person = new Person();
					person.setId(id);
				}
				if("name".equals(pullParser.getName())){
					String name = pullParser.nextText();
					person.setName(name);
				}
				if("age".equals(pullParser.getName())){
					int age = new Integer(pullParser.nextText());
					person.setAge(age);
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("person".equals(pullParser.getName())){
					persons.add(person);
					person = null;
				}
				break;
			}
			event = pullParser.next();
		}
		return persons;
	}
	
	/**
	 * 保存数据
	 * @param persons 数据
	 * @param out  输入方向
	 * @throws Exception
	 */
	public static void saveXml(List<Person> persons,OutputStream out) throws Exception{
		//创建一个序列化实例
		XmlSerializer serializer = Xml.newSerializer();
		//设置输出流
		serializer.setOutput(out, "UTF-8");
		//设置文档
		serializer.startDocument("UTF-8", true);
		//设置开始标签
		serializer.startTag(null, "persons");
		for(Person person : persons){
			serializer.startTag(null, "person");
			serializer.attribute(null, "id", person.getId().toString());
			
			serializer.startTag(null, "name");
			serializer.text(person.getName());
			serializer.endTag(null, "name");
			
			serializer.startTag(null, "age");
			serializer.text(person.getAge().toString());
			serializer.endTag(null, "age");
			
			serializer.endTag(null, "person");
		}
		//创建结束标签
		serializer.endTag(null, "persons");
		//结束文档
		serializer.endDocument();
		out.flush();
		out.close();
	}

}
