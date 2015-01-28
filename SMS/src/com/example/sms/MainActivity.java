package com.example.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private Button button;
	private EditText numText;
	private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)this.findViewById(R.id.button1);
        numText = (EditText)this.findViewById(R.id.editText1);
        content = (EditText)this.findViewById(R.id.editText2);
        button.setOnClickListener(new ButtonListener());
    }
    
    private final class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String num = numText.getText().toString();
			String text = content.getText().toString();
			SmsManager sms = SmsManager.getDefault();
			//∑÷∏Ó–≈œ¢
			ArrayList<String> lists = sms.divideMessage(text);
			for(String str:lists){
				sms.sendTextMessage(num, null, str, null, null);
			}
			Toast.makeText(MainActivity.this,R.string.success,Toast.LENGTH_LONG).show();
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
