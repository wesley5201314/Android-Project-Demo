package com.qianyan.chat.share;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class FileUtil {

	private static String PATH = "/data/app/";
	private static String FILE_TYPE = ".apk";

	/**
	 * 通过apk文件获取包名
	 * 
	 * @param context
	 *            上下文
	 * @param path
	 *            文件路径
	 * @return
	 */
	public static String getPackageName(Context context, String path) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
		ApplicationInfo appInfo = null;
		if (info != null) {
			appInfo = info.applicationInfo;
			return appInfo.packageName;
		}
		return null;
	}

	/**
	 * 通过包名获取路径/data/app/ **.apk
	 * 
	 * @param packageName
	 * @return
	 */
	public static String getFilePath(String packageName) {
		/* /data/app/{package_name}-[1-9].apk */
		String fileName = PATH + packageName;
		if (new File(fileName + FILE_TYPE).exists()) {
			return packageName + FILE_TYPE;
		}
		for (int i = 1; i <= 9; i++) {
			if (new File(fileName + "-" + i + FILE_TYPE).exists()) {
				return packageName + "-" + i + FILE_TYPE;
			}
		}
		return null;
	}
}
