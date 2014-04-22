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
	// �����Ϣ
	private ArrayList<PackageInfo> packageInfos = null;
	private AlertDialog.Builder alterBuilder = null;
	// ��ʾ�Ի���
	private BluetoothAdapter mBluetoothAdapter = null;
	// apk�ļ���Ϣ��������� ·��
	public static HashMap<String, String> apkInfo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_main);
		// ��ʼ����Ҫ��Ϣ
		this.init();
	}

	@Override
	protected void onDestroy() {
		// �˳�����
		apkInfo = null;
		super.onDestroy();

	}

	/** init event */
	private void init() {
		this.listView = (ListView) this.findViewById(R.id.list_app);
		this.listView.setOnItemClickListener(new ListItemClick());
		// PackageManager ��ȡ��װ��Ϣ
		this.pManager = (PackageManager) this.getPackageManager();
		this.packageInfos = new ArrayList<PackageInfo>();
		this.getAppInfo(this.packageInfos, this.pManager);
		// ���� adapter
		this.listView.setAdapter(new AppAdapter(this, this.packageInfos));

		// ��ȡ�����豸��
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// ��������������
		if (this.mBluetoothAdapter == null) {
			this.alterBuilder = new AlertDialog.Builder(this);
			this.alterBuilder.setMessage(R.string.title_unuse)
					.setNegativeButton(R.string.title_ok, null);
		}
	}

	/** �б����¼� */
	private class ListItemClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// �����豸������
			if (mBluetoothAdapter == null) {
				alterBuilder.create().show();
			} else {
				Intent intent = new Intent(ShareFile.this,
						TipDialogActivity.class);
				// ���item�İ���
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
		// ��ȡ�ֻ�������Ӧ��
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = (PackageInfo) paklist.get(i);
			// ֻ�����û���װ�������ϵͳӦ��
			if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
				infos.add(pak);
			}
		}
	}
}
