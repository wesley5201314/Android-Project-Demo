package com.example.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.file.FileService;

/**
 * @author wesley
 * @version 
 * @date 2015年1月22日 上午11:40:40
 * 
 */
public class FileReadTest extends AndroidTestCase{
	
	
	
	public void ReadTest(){
		FileService f = new FileService(this.getContext());
		try {
			String str = f.read("test.text");
			Log.i("result", str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void Read(){
		FileService f = new FileService(this.getContext());
		try {
			 f.readSd("sdtest.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
