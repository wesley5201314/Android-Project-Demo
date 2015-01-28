package com.example.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.domain.Person;
import com.example.service.XmlService;

/**
 * @author wesley
 * @version 
 * @date 2015年1月26日 上午11:53:54
 * 
 */
public class XmlTest extends AndroidTestCase{
	
private static final String TAG = "XmlServiceTest";
	
	public void testRead() throws Exception{
		InputStream xml = this.getClass().getClassLoader().getResourceAsStream("person.xml");
		List<Person> persons = XmlService.readXml(xml);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testSave() throws Exception{
		List<Person> persons = new ArrayList<Person>();
		persons.add(new Person(1, "zhang", 22));
		persons.add(new Person(2, "lili", 20));
		persons.add(new Person(3, "xiaoxiao", 8));
		//获取文件名
		File xmlFile = new File(getContext().getFilesDir(), "test.xml");
		FileOutputStream outStream = new FileOutputStream(xmlFile);
		XmlService.saveXml(persons, outStream);
	}

}
