package com.qianyan.chat.share;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.qianyan.chat.R;

public class BluetoothUtility implements StaticVar {
	private Context context = null;
	private BluetoothAdapter bluetoothAdapter = null;
	private ArrayList<BluetoothDevice> drivers = null; // �����豸
	private ProgressDialog progressDialog = null;
	private Handler uiHandler = null;
	private BluetoothSocket btSocket = null;

	public BluetoothUtility(Context context, BluetoothAdapter bluetoothAdapter,
			ProgressDialog progressDialog, Handler uiHandler) {
		this.context = context;
		this.bluetoothAdapter = bluetoothAdapter;
		this.progressDialog = progressDialog;
		this.drivers = new ArrayList<BluetoothDevice>();
		this.uiHandler = uiHandler;
	}

	/** ������ */
	public void openBluetooth() {
		if (!bluetoothAdapter.isEnabled()) {
			// ֻҪȨ�޹���ֱ�Ӵ򿪲���Ҫѯ��
			bluetoothAdapter.enable();
			/* ��Ҫѯ���û����� @Deprecated */
			// Intent enableBtIntent = new
			// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// ((Activity)context).startActivityForResult(enableBtIntent,
			// REQUEST_ENABLE_BT);
			// // ���ö���ɼ�, 300�������ʱ��
			// Intent discoverableIntent = new Intent(BluetoothAdapter.
			// ACTION_REQUEST_DISCOVERABLE );
			// discoverableIntent.putExtra(BluetoothAdapter.
			// EXTRA_DISCOVERABLE_DURATION , 300);
			// ((Activity)context).startActivity(discoverableIntent);
		}
	}

	/** �ر����� */
	public void closeBluetooth() {
		if (this.bluetoothAdapter.isEnabled())
			this.bluetoothAdapter.disable();
	}

	/** �������� */
	public void searchBluetooth() {
		this.progressDialog.show();
		// ��ʼ��Ѱ�豸
		if (!bluetoothAdapter.isEnabled())
			this.openBluetooth();

		// ע�����������ȡ�����豸��Ϣ�� �ǵö�ע��һ�� ACTION_DISCOVERY_FINISHED
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		// test
		filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");

		this.context.registerReceiver(mReceiver, filter);
		Log.d("Debug", "registerReceiver receiver");
		// bluetoothAdapter�򿪵�׼���׶λ��޷�����
		// ����ֱ��ֱ���򿪳ɹ�
		while (!this.bluetoothAdapter.startDiscovery())
			;
		// ����֮ǰ���������豸
		this.drivers.clear();
		Log.d("Debug", "startDiscovery");
	}

	/**
	 * �����ļ�
	 * 
	 * @param macAddress
	 *            ������ַ
	 */
	public void sendFile(String macAddress, String path) {
		BluetoothDevice bluetoothDevice = this.bluetoothAdapter
				.getRemoteDevice(macAddress);
		try {
			Method method = bluetoothDevice.getClass().getMethod(
					"createRfcommSocket", new Class[] { int.class });
			// this.btSocket = (BluetoothSocket) method.invoke(bluetoothDevice,
			// 1);
			method.invoke(bluetoothDevice, 1);
			ContentValues cv = new ContentValues();
			// �ļ������� file:// + �ļ���������ط���Ҫע�� ��� /
			// eg: cv.put("uri", "file:///system/app/Contacts.apk");
			// socket���Բ�������
			/* this.btSocket.connect(); */
			cv.put("uri", PATH + path);
			cv.put("destination", macAddress);
			cv.put("direction", 0);
			cv.put("timestamp", System.currentTimeMillis());
			this.context.getContentResolver().insert(
					Uri.parse("content://com.android.bluetooth.opp/btopp"), cv);
			// �������ȡ������
			//btSocket.close();
		} catch (Exception e) {
			// ��������
			e.printStackTrace();
			Toast.makeText(context,
					context.getResources().getString(R.string.title_send_fail),
					Toast.LENGTH_LONG).show();
		} finally {
			Toast.makeText(context,
					context.getResources().getString(R.string.title_sending),
					Toast.LENGTH_LONG).show();
			if (btSocket != null) {
				try {
					btSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** ���������豸 */
	public ArrayList<BluetoothDevice> getDrivers() {
		return this.drivers;
	}

	/** ��ɲ��ң��ͷ���Դ */
	public void finishSearch() {
		// ���������
		this.context.unregisterReceiver(mReceiver);
		// ȡ�������������������Ч��
		this.bluetoothAdapter.cancelDiscovery();
		Log.d("Debug", "unregisterReceiver receiver an cancelDiscovery");
	}

	/** ������Ѱ�����豸��Ϣ */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("Debug", action.toString());
			// ����һ���豸
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// �ó������豸��Ϣ
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// ������Ϣ�����б���ʾ
				// ��ֹ�ظ����
				if (drivers.contains(device))
					return;
				drivers.add(device);
				Log.d("Debug", "ACTION_FOUND:" + drivers.size());
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				progressDialog.dismiss();
				// δ�ҵ��豸
				uiHandler.sendEmptyMessage(CHANG_LIST);
				Log.d("Debug", "ACTION_DISCOVERY_FINISHED");
			} else if (action
					.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
				BluetoothDevice btDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				String tmp = "123";
				Method setPsd;
				try {
					setPsd = btDevice.getClass().getDeclaredMethod("setPin",
							new Class[] { byte[].class });
					setPsd.invoke(btDevice, new Object[] { tmp.getBytes() });
					Method createBondMethod = btDevice.getClass().getMethod(
							"createBond");
					createBondMethod.invoke(btDevice);
					// return returnValue.booleanValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out
						.println("android.bluetooth.device.action.PAIRING_REQUEST");
			}
		}
	};
}
