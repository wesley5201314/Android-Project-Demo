package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author wesley
 * @version 
 * @date 2015��1��29�� ����4:12:28
 * 
 */
public class StreamUtils {

	/**
	 * ��ȡ���е�����
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream in) throws Exception{
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] data = new byte[1024];
		while((len = in.read(data))!=-1){
			bStream.write(data, 0, len);
		}
		in.close();
		return bStream.toByteArray();
	}

}
