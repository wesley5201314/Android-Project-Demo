package com.example.nethtml;

import com.example.services.PageService;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText pagePath;
	private Button button;
	private TextView codeView;
	private String code = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pagePath = (EditText) this.findViewById(R.id.editText1);
		button = (Button) this.findViewById(R.id.button1);
		codeView = (TextView)this.findViewById(R.id.textView2);
		button.setOnClickListener(new ButtonListener());
	}
	
	private final class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final String path = pagePath.getText().toString();
			Log.i("codeView", path);
			final Handler handler = new Handler(){
				public void handleMessage(Message msg){
					switch (msg.what) {
					case 0:
						Log.i("codeView", code);
						codeView.setText(code);;
						break;
					}
				}
			};
			
			new Thread(){
				public void run(){
					try {
						code = PageService.getPage(path);
						handler.sendEmptyMessage(0);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), R.string.error, 1)
								.show();
					}
				}
			}.start();
		}
		
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
