package com.example.file;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private Button button;
	private EditText filename;
	private EditText fileContent;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)this.findViewById(R.id.button1);
        filename = (EditText)this.findViewById(R.id.editText1);
        fileContent = (EditText)this.findViewById(R.id.editText2);
        button.setOnClickListener(new ButtonListener());
    }
    
    private final class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String file = filename.getText().toString();
			String content = fileContent.getText().toString();
			FileService fileService = new FileService(getApplicationContext());
			try {
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					fileService.saveSd(file, content);
					Toast.makeText(getApplicationContext(), R.string.success, 1).show();
				}else{
					Toast.makeText(getApplicationContext(), R.string.sderror, 1).show();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), R.string.fail, 1).show();
				e.printStackTrace();
			}
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
