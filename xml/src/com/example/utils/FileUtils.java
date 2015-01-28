package com.example.utils;

import java.io.File;

import android.os.Environment;
import android.widget.Toast;

/**
 * @author wesley
 * @version 
 * @date 2015年1月26日 下午2:26:31
 * 
 */
public class FileUtils {

	public File getFilePath(){
		File filePath = null;
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			filePath = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),"/test/");
			if(!filePath.exists()){
				filePath.mkdirs();
			}
		} else {
			//Toast.makeText(Main.this, "sdCard not exits!", 1).show();
		}
		return filePath;
	}
	
}
