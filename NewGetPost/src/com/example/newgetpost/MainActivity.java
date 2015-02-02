package com.example.newgetpost;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.service.VideoService;

public class MainActivity extends Activity {
	
	private EditText edit1;
	private EditText edit2;
	private VideoService videoService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		videoService = new VideoService(this);
		edit1 = (EditText)this.findViewById(R.id.editText1);
		edit2 = (EditText)this.findViewById(R.id.editText2);
	}

	
	public void send(View v){
		final String name = edit1.getText().toString();
		final String time = edit2.getText().toString();
		final Handler handler = new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 0:
					Toast.makeText(getApplicationContext(), R.string.success, 1)
					.show();
					break;
				case 1:
					Toast.makeText(getApplicationContext(), R.string.error, 1)
					.show();
					break;
				default:
					break;
				}
			}
		};
		
		new Thread(new Runnable() {
				@Override
				public void run() {
					  try {
						  boolean result = VideoService.send(name,time);
							if(result){
								handler.sendEmptyMessage(0);
							} else {
								handler.sendEmptyMessage(1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}).start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
