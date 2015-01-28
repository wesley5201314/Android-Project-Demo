package com.example.shardpreferences;

import java.util.Map;

import com.example.services.ShardPreferencesServices;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText edit1;
	private EditText edit2;
	private ShardPreferencesServices shardPreferencesServices;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edit1 = (EditText)this.findViewById(R.id.edit1);
		edit2 = (EditText)this.findViewById(R.id.edit2);
		shardPreferencesServices = new ShardPreferencesServices(this);
		Map<String, String> params = shardPreferencesServices.getPreferences();
		edit1.setText(params.get("name"));
		edit2.setText(params.get("age"));
		
	}
	
	public void save(View v){
		String name = edit1.getText().toString();
		String age = edit2.getText().toString();
		shardPreferencesServices.save(name, Integer.valueOf(age));
		Toast.makeText(MainActivity.this, R.string.success, 1).show();
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
