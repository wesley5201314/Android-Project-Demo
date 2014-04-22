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

	private String packName = null; // ���ݹ������ļ�����

	private ListView driversList = null;// ƥ���豸
	private TextView txtTip = null; // ��ʾ�ı�
	private Button btnCancle = null; // ȡ��
	private Button btnSearch = null; // ����

	private ArrayList<BluetoothDevice> driverDatas = null; // ��Ե��豸
	private ArrayList<String> dataStrings = null; // �����б���ʾ
	private BluetoothAdapter bluetoothAdapter = null; // ����
	private BluetoothUtility bluetoothUtility = null; // ����������
	private AlertDialog.Builder builder = null; // ��ʾ�Ի���
	private ProgressDialog processDialog = null; // �������ȿ�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ô���ģʽ
		this.setContentView(R.layout.dialog);
		// ��ʼ�������Ϣ
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
			// 0 �򿪣�-1δ�� TODO����� ���ĵ�д�Ĳ�һ��
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

	// ui���
	private Handler uiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (CHANG_LIST == msg.what) { // �����豸���
				bluetoothUtility.finishSearch();
				driverDatas = bluetoothUtility.getDrivers();
				if (driverDatas == null || driverDatas.size() <= 0) {
					// builder.setMessage(R.string.title_unfound).show();
					txtTip.setText(getResources().getString(
							R.string.title_unfound));
					driversList.setVisibility(View.GONE); // �б�ɼ�
					driversList.setAdapter(null);
				} else {
					// չ������
					initString(dataStrings, driverDatas);
					txtTip.setText(getResources().getString(
							R.string.title_found));
					driversList.setVisibility(View.VISIBLE); // �б�ɼ�
					driversList.setAdapter(new ArrayAdapter<String>(
							TipDialogActivity.this,
							android.R.layout.simple_expandable_list_item_1,
							dataStrings));
				}
			}
		}
	};

	// ====== �������Զ��巽�� ======//

	/** ��ʼ�������Ϣ */
	private void init() {
		// ���ݹ����ĳ������
		this.packName = this.getIntent().getStringExtra("pack_name");
		this.processDialog = new ProgressDialog(this);
		// ��תstyle
		this.processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.processDialog.setMessage(this.getResources().getString(
				R.string.title_searching));
		this.processDialog.setCancelable(false);

		// ��ȡbluetooth adapter
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (this.bluetoothAdapter != null) {
			this.bluetoothUtility = new BluetoothUtility(this,
					bluetoothAdapter, this.processDialog, this.uiHandler);
			this.dataStrings = new ArrayList<String>();
			// this.driverDatas = new HashMap<String, String>();
			this.driverDatas = new ArrayList<BluetoothDevice>();

			// ��ʼ��string,ListView ����豸
			Set<BluetoothDevice> devices = this.bluetoothAdapter
					.getBondedDevices();
			for (BluetoothDevice item : devices) {
				this.driverDatas.add(item);
				// ����Ϊ��Ĭ����mac��ַ
				if (item.getName().trim().equals("")) {
					this.dataStrings.add(item.getAddress());
				} else {
					this.dataStrings.add(item.getName());
				}
			}
		}

		this.builder = new AlertDialog.Builder(this);
		// ����б�
		this.driversList = (ListView) this.findViewById(R.id.list_bound_driver);
		this.driversList.setOnItemClickListener(new ListClick(this,
				this.builder));
		this.txtTip = (TextView) this.findViewById(R.id.txt_tip);
		this.btnCancle = (Button) this.findViewById(R.id.btn_cancle);
		this.btnSearch = (Button) this.findViewById(R.id.btn_search);
		// ��ť�����¼�
		this.btnCancle.setOnClickListener(this);
		this.btnSearch.setOnClickListener(this);

		// ������
		if (this.dataStrings != null && this.dataStrings.size() > 0) {
			// �ҵ�����豸
			this.txtTip.setText(this.getResources().getString(
					R.string.title_bound));
			this.driversList.setVisibility(View.VISIBLE); // �б�ɼ�
			this.driversList.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_expandable_list_item_1,
					this.dataStrings));
		} else {
			// δ�ҵ�����豸
			this.txtTip.setText(getResources()
					.getString(R.string.title_unfound));
			this.driversList.setVisibility(View.GONE); // �б�����
			this.driversList.setAdapter(null);
		}
	}

	/** view�ĵ����¼� */
	public void onClick(View v) {
		switch (v.getId()) {
		case BTN_CANCLE:
			this.finish(); // ȡ��
			break;
		case BTN_SEARCH: // ����
			this.doSearch();
			break;
		}
	}

	/** ��Ѱ�豸 */
	private void doSearch() {
		this.bluetoothUtility.searchBluetooth();
	}

	/** ��ʼ���б�string */
	private void initString(ArrayList<String> dataStrings,
			ArrayList<BluetoothDevice> driverDatas) {
		if (driverDatas == null)
			return;
		dataStrings.clear();
		@SuppressWarnings("unused")
		String name = null;
		for (BluetoothDevice item : driverDatas) {
			// �������Ϊ��, Ĭ������mac��ַ
			if ((name = item.getName()).trim().equals("")) {
				dataStrings.add(item.getAddress());
			} else {
				dataStrings.add(item.getName());
			}
		}
	}

	/** ����豸�ĵ����¼� */
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
									// �������豸
									if (!bluetoothAdapter.isEnabled()) {
										bluetoothUtility.openBluetooth();
									}
									// ����apk�ļ�
									BluetoothDevice bd = getDevice(deviceName,
											driverDatas);
									// ��ȡ·��
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
									// ���û�����֣�ֱ����mac��ַ
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
										// ���ǵ�ַ��û���豸
									}
								}
							}).setNegativeButton(R.string.title_cancle, null)
					.create().show();
		}
	}

	/**
	 * ��ȡ�ض��� device
	 * 
	 * @param key
	 *            ����
	 * @param devices
	 *            ����ֻ���Ϣ
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
	 * �ж��ǲ���һ��������ַ
	 * 
	 * @param address
	 * @return
	 */
	private boolean isAddress(String address) {
		// ������4����������mac��ַ
		String[] tmp = address.split(":");
		if (tmp.length >= 4) {
			return true;
		}
		return false;
	}

	/**
	 * ͨ��������ȡ����
	 * 
	 * @param pkgName
	 *            ����
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getFileName(String pkgName) {
		String tmpString = ShareFile.apkInfo.get(packName);
		return ShareFile.apkInfo.get(packName);
	}

	/** ��ȡ ��/system/app�� ��apk�ļ���Ϣ */
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
						// ���棺���� �ļ���
						ShareFile.apkInfo.put(FileUtil.getPackageName(context,
								file.getPath()), file.getName());
					}
				}
			}
			// ������ɣ�֪ͨui���
			// uiHandler.sendEmptyMessage(INIT_APK_FINISH);
		}
	}
}
