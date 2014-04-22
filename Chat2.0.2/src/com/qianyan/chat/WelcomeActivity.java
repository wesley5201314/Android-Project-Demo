package com.qianyan.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		Start();
	}

	public void Start() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, BluetoothChat.class);
				startActivity(intent);
				finish();
			}
		}.start();
	}
}
