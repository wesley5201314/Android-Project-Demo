package com.example.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * @author wesley
 * @version 
 * @date 2015��1��22�� ����10:56:31
 * 
 */
public class FileService {
	
	private Context context;

	public FileService(Context context) {
		this.context = context;
	}
	
	/**
	 * �����ļ�
	 * @param file  �ļ���
	 * @param content  �ļ�����
	 * @throws Exception
	 */
	public void save(String file,String content) throws Exception{
		//��ȡ����� openFileOutput��һ���������ļ�������������ھ��Զ�����һ����
		FileOutputStream out = context.openFileOutput(file, Context.MODE_PRIVATE);
		//д��
		out.write(content.getBytes());
		//�ر�
		out.close();
	}
	
	/**
	 * �����ļ���sd��
	 * @param fileName  �ļ���
	 * @param content  �ļ�����
	 * @throws Exception
	 */
	public void saveSd(String fileName,String content) throws Exception{
		File file = new File(Environment.getExternalStorageDirectory(),fileName);
		/*if(!file.exists()){
			file.createNewFile();
		}*/
		FileOutputStream out = new FileOutputStream(file);
		out.write(content.getBytes());
		out.close();
	}
	
	/**
	 * ��ȡsd���ϵ��ļ�
	 * @param fileName  �ļ���
	 * @param content  �ļ�����
	 * @throws Exception
	 */
	public String readSd(String fileName) throws Exception{
		File file = new File(Environment.getExternalStorageDirectory(),fileName);
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//��ȡ����ʼλ��
		int len = 0;
		//���������ַ�����
		byte[] buffer = new byte[1024];
		while((len = in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		};
		//�õ���ȡ������
		byte[] data = out.toByteArray();
		return new String(data);
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @param file �ļ���
	 * @return �ļ�����
	 * @throws Exception
	 */
	public String read(String file) throws Exception{
		//�ļ�������
		FileInputStream in = context.openFileInput(file);
		//�ֽ������
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//��ȡ����ʼλ��
		int len = 0;
		//���������ַ�����
		byte[] buffer = new byte[1024];
		while((len = in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		};
		//�õ���ȡ������
		byte[] data = out.toByteArray();
		return new String(data);
	}

}
