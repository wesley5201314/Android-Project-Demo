package com.qianyan.chat.share;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.qianyan.chat.R;

public class ShareFile extends Activity{
	
	private ListView listView = null;
	private PackageManager pManager = null;
	// 软件信息
	private ArrayList<PackageInfo> packageInfos = null;
	private AlertDialog.Builder alterBuilder = null;
	// 提示对话框
	private BluetoothAdapter mBluetoothAdapter = null;
	// apk文件信息，保存包名 路径
	public static HashMap<String, String> apkInfo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_main);
		// 初始化必要信息
		this.init();
	}

	@Override
	protected void onDestroy() {
		// 退出回收
		apkInfo = null;
		super.onDestroy();

	}

	/** init event */
	private void init() {
		this.listView = (ListView) this.findViewById(R.id.list_app);
		this.listView.setOnItemClickListener(new ListItemClick());
		// PackageManager 获取安装信息
		this.pManager = (PackageManager) this.getPackageManager();
		this.packageInfos = new ArrayList<PackageInfo>();
		this.getAppInfo(this.packageInfos, this.pManager);
		// 设置 adapter
		this.listView.setAdapter(new AppAdapter(this, this.packageInfos));

		// 获取蓝牙设备器
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// 本机蓝牙不可用
		if (this.mBluetoothAdapter == null) {
			this.alterBuilder = new AlertDialog.Builder(this);
			this.alterBuilder.setMessage(R.string.title_unuse)
					.setNegativeButton(R.string.title_ok, null);
		}
	}

	/** 列表单击事件 */
	private class ListItemClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 蓝牙设备不可用
			if (mBluetoothAdapter == null) {
				alterBuilder.create().show();
			} else {
				Intent intent = new Intent(ShareFile.this,
						TipDialogActivity.class);
				// 点击item的包名
				intent.putExtra("pack_name",
						packageInfos.get(position).applicationInfo.packageName);
				startActivity(intent);
			}
		}
	}

	@SuppressWarnings("static-access")
	private void getAppInfo(ArrayList<PackageInfo> infos,
			PackageManager packageManager) {
		infos.clear();
		// 获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			// 只保存用户安装软件，非系统应用
			if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
				infos.add(pak);
			}
		}
	}
}
