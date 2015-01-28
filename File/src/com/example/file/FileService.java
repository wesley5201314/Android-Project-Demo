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
 * @date 2015年1月22日 上午10:56:31
 * 
 */
public class FileService {
	
	private Context context;

	public FileService(Context context) {
		this.context = context;
	}
	
	/**
	 * 保存文件
	 * @param file  文件名
	 * @param content  文件内容
	 * @throws Exception
	 */
	public void save(String file,String content) throws Exception{
		//获取输出流 openFileOutput第一个参数是文件名，如果不存在就自动创建一个！
		FileOutputStream out = context.openFileOutput(file, Context.MODE_PRIVATE);
		//写入
		out.write(content.getBytes());
		//关闭
		out.close();
	}
	
	/**
	 * 保存文件到sd卡
	 * @param fileName  文件名
	 * @param content  文件内容
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
	 * 读取sd卡上的文件
	 * @param fileName  文件名
	 * @param content  文件内容
	 * @throws Exception
	 */
	public String readSd(String fileName) throws Exception{
		File file = new File(Environment.getExternalStorageDirectory(),fileName);
		FileInputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//读取的起始位置
		int len = 0;
		//创建缓冲字符数组
		byte[] buffer = new byte[1024];
		while((len = in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		};
		//得到读取的数据
		byte[] data = out.toByteArray();
		return new String(data);
	}
	
	/**
	 * 读取文件内容
	 * @param file 文件名
	 * @return 文件内容
	 * @throws Exception
	 */
	public String read(String file) throws Exception{
		//文件输入流
		FileInputStream in = context.openFileInput(file);
		//字节输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//读取的起始位置
		int len = 0;
		//创建缓冲字符数组
		byte[] buffer = new byte[1024];
		while((len = in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		};
		//得到读取的数据
		byte[] data = out.toByteArray();
		return new String(data);
	}

}
