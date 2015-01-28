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
 * @date 2015��1��26�� ����11:32:09
 * 
 */
public class XmlService {
	
	/**
	 * ��ȡ����
	 * @param xml Ҫ��ȡ������
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("UseValueOf") 
	public static List<Person> readXml(InputStream xml) throws Exception{
		List<Person> persons = null;
		Person person = null;
		//����һ��Pullʵ��
		XmlPullParser pullParser = Xml.newPullParser();
		//ΪPull����������Ҫ������XML����
		pullParser.setInput(xml, "UTF-8");
		//�õ���һ���¼�
		int event = pullParser.getEventType();
		//�ж��ǲ����ĵ������¼�
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
	 * ��������
	 * @param persons ����
	 * @param out  ���뷽��
	 * @throws Exception
	 */
	public static void saveXml(List<Person> persons,OutputStream out) throws Exception{
		//����һ�����л�ʵ��
		XmlSerializer serializer = Xml.newSerializer();
		//���������
		serializer.setOutput(out, "UTF-8");
		//�����ĵ�
		serializer.startDocument("UTF-8", true);
		//���ÿ�ʼ��ǩ
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
		//����������ǩ
		serializer.endTag(null, "persons");
		//�����ĵ�
		serializer.endDocument();
		out.flush();
		out.close();
	}

}
