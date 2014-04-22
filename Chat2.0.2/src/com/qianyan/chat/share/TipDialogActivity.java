package com.qianyan.chat.share;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qianyan.chat.R;

public class TipDialogActivity extends Activity implements OnClickListener,
		StaticVar {

	private final static int BTN_CANCLE = R.id.btn_cancle;
	private final static int BTN_SEARCH = R.id.btn_search;

	private String packName = null; // 传递过来的文件包名

	private ListView driversList = null;// 匹配设备
	private TextView txtTip = null; // 提示文本
	private Button btnCancle = null; // 取消
	private Button btnSearch = null; // 搜索

	private ArrayList<BluetoothDevice> driverDatas = null; // 配对的设备
	private ArrayList<String> dataStrings = null; // 用于列表显示
	private BluetoothAdapter bluetoothAdapter = null; // 蓝牙
	private BluetoothUtility bluetoothUtility = null; // 蓝牙工具类
	private AlertDialog.Builder builder = null; // 提示对话框
	private ProgressDialog processDialog = null; // 搜索进度框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置窗口模式
		this.setContentView(R.layout.dialog);
		// 初始化相关信息
		this.init();
		Log.d("Debug", "onCreate");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("Debug", "onStart");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_ENABLE_BT == requestCode) {
			// 0 打开，-1未打开 TODO很奇怪 跟文档写的不一样
			switch (resultCode) {
			case RESULT_OK:

				// break;
			case RESULT_CANCELED:
				this.builder.setPositiveButton(R.string.title_ok, null)
						.setMessage(R.string.title_unuse).show();
				break;
			}
		}
	}

	// ui变更
	private Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (CHANG_LIST == msg.what) { // 搜索设备完成
				bluetoothUtility.finishSearch();
				driverDatas = bluetoothUtility.getDrivers();
				if (driverDatas == null || driverDatas.size() <= 0) {
					// builder.setMessage(R.string.title_unfound).show();
					txtTip.setText(getResources().getString(
							R.string.title_unfound));
					driversList.setVisibility(View.GONE); // 列表可见
					driversList.setAdapter(null);
				} else {
					// 展现数据
					initString(dataStrings, driverDatas);
					txtTip.setText(getResources().getString(
							R.string.title_found));
					driversList.setVisibility(View.VISIBLE); // 列表可见
					driversList.setAdapter(new ArrayAdapter<String>(
							TipDialogActivity.this,
							android.R.layout.simple_expandable_list_item_1,
							dataStrings));
				}
			}
		}
	};

	// ====== 以下是自定义方法 ======//

	/** 初始化相关信息 */
	private void init() {
		// 传递过来的程序包名
		this.packName = this.getIntent().getStringExtra("pack_name");
		this.processDialog = new ProgressDialog(this);
		// 旋转style
		this.processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.processDialog.setMessage(this.getResources().getString(
				R.string.title_searching));
		this.processDialog.setCancelable(false);

		// 获取bluetooth adapter
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (this.bluetoothAdapter != null) {
			this.bluetoothUtility = new BluetoothUtility(this,
					bluetoothAdapter, this.processDialog, this.uiHandler);
			this.dataStrings = new ArrayList<String>();
			// this.driverDatas = new HashMap<String, String>();
			this.driverDatas = new ArrayList<BluetoothDevice>();

			// 初始化string,ListView 添加设备
			Set<BluetoothDevice> devices = this.bluetoothAdapter
					.getBondedDevices();
			for (BluetoothDevice item : devices) {
				this.driverDatas.add(item);
				// 名字为空默认是mac地址
				if (item.getName().trim().equals("")) {
					this.dataStrings.add(item.getAddress());
				} else {
					this.dataStrings.add(item.getName());
				}
			}
		}

		this.builder = new AlertDialog.Builder(this);
		// 配对列表
		this.driversList = (ListView) this.findViewById(R.id.list_bound_driver);
		this.driversList.setOnItemClickListener(new ListClick(this,
				this.builder));
		this.txtTip = (TextView) this.findViewById(R.id.txt_tip);
		this.btnCancle = (Button) this.findViewById(R.id.btn_cancle);
		this.btnSearch = (Button) this.findViewById(R.id.btn_search);
		// 按钮监听事件
		this.btnCancle.setOnClickListener(this);
		this.btnSearch.setOnClickListener(this);

		// 适配器
		if (this.dataStrings != null && this.dataStrings.size() > 0) {
			// 找到配对设备
			this.txtTip.setText(this.getResources().getString(
					R.string.title_bound));
			this.driversList.setVisibility(View.VISIBLE); // 列表可见
			this.driversList.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_expandable_list_item_1,
					this.dataStrings));
		} else {
			// 未找到配对设备
			this.txtTip.setText(getResources()
					.getString(R.string.title_unfound));
			this.driversList.setVisibility(View.GONE); // 列表隐藏
			this.driversList.setAdapter(null);
		}
	}

	/** view的单击事件 */
	public void onClick(View v) {
		switch (v.getId()) {
		case BTN_CANCLE:
			this.finish(); // 取消
			break;
		case BTN_SEARCH: // 搜索
			this.doSearch();
			break;
		}
	}

	/** 找寻设备 */
	private void doSearch() {
		this.bluetoothUtility.searchBluetooth();
	}

	/** 初始化列表string */
	private void initString(ArrayList<String> dataStrings,
			ArrayList<BluetoothDevice> driverDatas) {
		if (driverDatas == null)
			return;
		dataStrings.clear();
		@SuppressWarnings("unused")
		String name = null;
		for (BluetoothDevice item : driverDatas) {
			// 如果名字为空, 默认是用mac地址
			if ((name = item.getName()).trim().equals("")) {
				dataStrings.add(item.getAddress());
			} else {
				dataStrings.add(item.getName());
			}
		}
	}

	/** 配对设备的单击事件 */
	private class ListClick implements OnItemClickListener {

		private Context context = null;
		private AlertDialog.Builder builder = null;

		public ListClick(Context context, AlertDialog.Builder builder) {
			this.context = context;
			this.builder = builder;
		}

		public void onItemClick(AdapterView<?> arg0, final View view,
				int position, long arg3) {
			final String deviceName = ((TextView) view).getText().toString();
			builder.setTitle(R.string.title_tip)
					.setIcon(android.R.drawable.ic_menu_help)
					.setMessage(
							String.format(
									context.getResources().getString(
											R.string.title_send), deviceName))
					.setPositiveButton(R.string.title_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// 打开蓝牙设备
									if (!bluetoothAdapter.isEnabled()) {
										bluetoothUtility.openBluetooth();
									}
									// 发送apk文件
									BluetoothDevice bd = getDevice(deviceName,
											driverDatas);
									// 获取路径
									String path = FileUtil
											.getFilePath(packName);
									if (path == null) {
										Toast.makeText(
												context,
												getResources()
														.getString(
																R.string.title_apk_nofound),
												Toast.LENGTH_SHORT).show();
										return;
									}
									// 如果没有名字，直接用mac地址
									if (bd == null && isAddress(deviceName)) {
										// bluetoothUtility.sendFile(deviceName,
										// getFileName(packName));
										bluetoothUtility.sendFile(deviceName,
												path);
									} else if (bd != null) {
										// bluetoothUtility.sendFile(bd.getAddress(),
										// getFileName(packName));
										bluetoothUtility.sendFile(
												bd.getAddress(), path);
									} else {
										// 不是地址，没用设备
									}
								}
							}).setNegativeButton(R.string.title_cancle, null)
					.create().show();
		}
	}

	/**
	 * 获取特定的 device
	 * 
	 * @param key
	 *            索引
	 * @param devices
	 *            配对手机信息
	 * @return
	 */
	private BluetoothDevice getDevice(String key,
			ArrayList<BluetoothDevice> devices) {
		for (BluetoothDevice device : devices) {
			if (device.getName().equals(key)) {
				return device;
			}
		}
		return null;
	}

	/**
	 * 判断是不是一个蓝牙地址
	 * 
	 * @param address
	 * @return
	 */
	private boolean isAddress(String address) {
		// 超过有4个：就算是mac地址
		String[] tmp = address.split(":");
		if (tmp.length >= 4) {
			return true;
		}
		return false;
	}

	/**
	 * 通过包名获取名字
	 * 
	 * @param pkgName
	 *            包名
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getFileName(String pkgName) {
		String tmpString = ShareFile.apkInfo.get(packName);
		return ShareFile.apkInfo.get(packName);
	}

	/** 获取 “/system/app” 下apk文件信息 */
	@SuppressWarnings("unused")
	private class InitApkInfo extends Thread {
		private Context context = null;

		public InitApkInfo(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			File[] files = new File(DIRECTORY).listFiles();
			if (files != null) {
				ShareFile.apkInfo = new HashMap<String, String>();
				for (File file : files) {
					if (file.getName().endsWith("apk")) {
						// 保存：包名 文件名
						ShareFile.apkInfo.put(FileUtil.getPackageName(context,
								file.getPath()), file.getName());
					}
				}
			}
			// 处理完成，通知ui变更
			// uiHandler.sendEmptyMessage(INIT_APK_FINISH);
		}
	}
}
